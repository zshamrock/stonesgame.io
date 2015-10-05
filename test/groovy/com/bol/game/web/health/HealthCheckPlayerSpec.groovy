package com.bol.game.web.health

import com.bol.game.web.Player
import spock.lang.Specification

import java.util.concurrent.ArrayBlockingQueue

class HealthCheckPlayerSpec extends Specification {
    def "successfully join a game in an acceptable period of time"() {
        given:
        def players = new ArrayBlockingQueue<Player>(10)
        def player = new HealthCheckPlayer(players)

        when:
        player.isReady()
        def result = player.check()

        then:
        result.healthy
        result.message ==
                "Successfully \"joined\" a game in $HealthCheckPlayer.ACCEPTABLE_HEALTH_DURATION_SECONDS seconds" as String
    }

    def "not enough queue capacity to join a game"() {
        given:
        def players = new ArrayBlockingQueue<Player>(1)
        def player = new HealthCheckPlayer(players)

        when:
        def result = player.check()

        then:
        !result.healthy
        result.message == 'Unable to add a new player to the players queue. Remaining queue capacity is 0.'
    }

    def "unable to join a game in the acceptable period of time"() {
        given:
        def players = new ArrayBlockingQueue<Player>(10)
        def player = new HealthCheckPlayer(players)

        when:
        def result = player.check()

        then:
        !result.healthy
        result.message ==
                "Unable to join a game in the acceptable period of time [$HealthCheckPlayer.ACCEPTABLE_HEALTH_DURATION_SECONDS seconds]" as String
    }
}
