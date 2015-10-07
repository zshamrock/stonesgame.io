package io.stonesgame.web.health

import io.stonesgame.web.Player
import spock.lang.Specification

import java.util.concurrent.ArrayBlockingQueue

class PlayersQueueCapacityHealthCheckSpec extends Specification {
    def "check remaining capacity below threshold"() {
        given:
        def capacity = 20
        def players = new ArrayBlockingQueue<Player>(capacity)
        16.times { players.add(Mock(Player)) }

        expect:
        def health = new PlayersQueueCapacityHealthCheck(capacity, players)
        def result = health.check()
        !result.healthy
        result.message == 'Critical players queue remaining capacity: 0.20% [4 out of 20]'
    }

    def "check remaining capacity above threshold"() {
        given:
        def capacity = 20
        def players = new ArrayBlockingQueue<Player>(capacity)
        15.times { players.add(Mock(Player)) }

        expect:
        def health = new PlayersQueueCapacityHealthCheck(capacity, players)
        def result = health.check()
        result.healthy
        result.message == 'Players queue remaining capacity is above critical: 0.25% [5 out of 20]. Everything is ok.'
    }
}
