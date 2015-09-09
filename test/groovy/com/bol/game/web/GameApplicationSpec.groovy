package com.bol.game.web
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WebSocketAdapter
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Stepwise
class GameApplicationSpec extends Specification {
    @Subject
    @Shared
    def app = new GameApplication()

    @Shared
    def client = new WebGameClient()

    def setupSpec() {
        println "Start server"
        app.run("server", "game.yaml")

        // wait for server to start up
        Thread.sleep(TimeUnit.SECONDS.toMillis(5))

        client.start()
    }

    def cleanupSpec() {
        println "Shutdown server"
        client.stop()
    }

    def "be able to connect to the server"() {
        setup:
        def connectLatch = new CountDownLatch(1)
        def socket = new WebSocketAdapter() {
            @Override
            void onWebSocketConnect(Session session) {
                super.onWebSocketConnect(session)
                connectLatch.countDown()
            }
        }

        when:
        client.connect(socket)

        then:
        connectLatch.await(5, TimeUnit.SECONDS)

        cleanup:
        socket.getSession().close()
    }

    def "be able join a game"() {
        setup:
        def joinLatch = new CountDownLatch(4)
        def player1 = new JoinGameWebSocket(num: 1, joinLatch: joinLatch)
        def player2 = new JoinGameWebSocket(num: 2, joinLatch: joinLatch)
        def json = new JsonSlurper()

        when:
        client.connect(player1)

        and:
        client.connect(player2)

        then:
        joinLatch.await(1, TimeUnit.MINUTES)
        verifyJoinMessages(json.parseText(player1.messages[0] as String), json.parseText(player2.messages[0] as String))
        verifyGoAndWaitMessages(json.parseText(player1.messages[1] as String), json.parseText(player2.messages[1] as String))

        cleanup:
        player1.getSession().close()
        player2.getSession()?.close()
    }

    void verifyJoinMessages(msg1, msg2) {
        // Sample message:
        // {"action":"join","board":[[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]],"player":1,"id":"028d35a0-754f-408a-a7ef-70faaccc76e5"})
        // joined the same game
        assert msg1.action == "join"
        assert msg2.action == "join"
        assert msg1.id == msg2.id
        assert msg1.board == msg2.board
        assert msg1.board == [[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]]
        assert msg1.player in [0,1]
        assert msg2.player in [0,1]
        assert msg1.player as int == ((msg2.player as int) + 1) % 2
    }

    void verifyGoAndWaitMessages(msg1, msg2) {
        // Sample message:
        // {"action":"go","board":[[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]],"over":false,"won":false,"player":0,"id":"22537dc3-2eb3-40f0-aed5-c06e1626ad7e"}
        // or
        // {"action":"wait","board":[[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]],"over":false,"won":false,"player":1,"id":"22537dc3-2eb3-40f0-aed5-c06e1626ad7e"}
        def go = msg1.action == "go" ? msg1 : msg2
        def wait = msg1.action == "wait" ? msg1 : msg2
        go.with {
            assert action == "go"
            assert board == wait.board
            assert board == [[6, 6, 6, 6, 6, 6, 0], [6, 6, 6, 6, 6, 6, 0]]
            assert over == false
            assert won == false
            assert player == 0
            assert id == wait.id
        }

        wait.with {
            assert action == "wait"
            assert over == false
            assert won == false
            assert player == 1
        }
    }

    private class JoinGameWebSocket extends WebSocketAdapter {
        def volatile messages = []
        CountDownLatch joinLatch
        def num

        @Override
        void onWebSocketText(String message) {
            // {"action":"join","board":[[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]],"player":0,"id":"22537dc3-2eb3-40f0-aed5-c06e1626ad7e"}
            // {"action":"go","board":[[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]],"over":false,"won":false,"player":0,"id":"22537dc3-2eb3-40f0-aed5-c06e1626ad7e"}
            this.messages << message
            println "Player$num received $message"
            joinLatch.countDown()
        }
    }

    def "be able to do multiple turns"() {
        setup:
        def turnLatch = new CountDownLatch(12)
        def player1 = new TurnGameWebSocket(num: 1, maxTurns: 2, turnLatch: turnLatch)
        def player2 = new TurnGameWebSocket(num: 2, maxTurns: 2, turnLatch: turnLatch)
        def json = new JsonSlurper()

        when:
        client.connect(player1)
        // be sure player1 joined as a first player
        TimeUnit.SECONDS.sleep(5)

        and:
        client.connect(player2)

        then:
        turnLatch.await(1, TimeUnit.MINUTES)
        player1.messages.size() == 6
        player2.messages.size() == 6

        def msg1 = json.parseText(player1.messages[-1] as String)
        def msg2 = json.parseText(player2.messages[-1] as String)

        msg1.board == msg2.board
        msg1.id == msg2.id
        msg1.player == 0
        msg2.player == 1
        msg1.board == [[1, 0, 8, 8, 8, 8, 9],[0, 0, 8, 7, 7, 7, 1]]
        player1.actions == ['join', 'go', 'go', 'wait', 'wait', 'go']
        player2.actions == ['join', 'wait', 'wait', 'go', 'go', 'wait']

        cleanup:
        player1.getSession().close()
        player2.getSession()?.close()
    }

    private class TurnGameWebSocket extends WebSocketAdapter {
        def volatile messages = []
        CountDownLatch turnLatch
        def num
        def maxTurns
        def turns = 0
        def json = new JsonSlurper()
        def volatile actions = []

        @Override
        void onWebSocketText(String message) {
            // {"action":"join","board":[[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]],"player":0,"id":"22537dc3-2eb3-40f0-aed5-c06e1626ad7e"}
            // {"action":"go","board":[[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]],"over":false,"won":false,"player":0,"id":"22537dc3-2eb3-40f0-aed5-c06e1626ad7e"}
            // or
            // {"action":"wait","board":[[6,6,6,6,6,6,0],[6,6,6,6,6,6,0]],"over":false,"won":false,"player":1,"id":"22537dc3-2eb3-40f0-aed5-c06e1626ad7e"}
            this.messages << message
            println "Player$num received $message"
            def msg = json.parseText(message)
            actions << msg.action
            if (turns == maxTurns && msg.action == 'go') {
                turnLatch.countDown()
                return
            }

            def pit = msg.board[msg.player].findIndexOf { it != 0 }
            if (msg.action == 'go') {
                def pick = JsonOutput.toJson([action: 'pick', pit: pit, id: msg.id])
                turns++
                getRemote().sendString(pick)
            }
            turnLatch.countDown()
        }
    }

}