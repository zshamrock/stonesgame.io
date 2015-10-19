package io.stonesgame.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.eclipse.jetty.websocket.api.Session
import spock.lang.Specification


class WebPlayerSpec extends Specification {
    def "web players is not a bot"() {
        given: "a web player"
        def player = new WebPlayer(Mock(Session), new ObjectMapper())

        expect: "player is not a bot"
        !player.bot
    }
}
