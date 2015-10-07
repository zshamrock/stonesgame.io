package io.stonesgame.web

import spock.lang.Specification


class GameConfigurationSpec extends Specification {
    def "get queue capacity"() {
        given:
        def config = new GameConfiguration()

        when:
        config.setQueueCapacity(inCapacity)

        then:
        config.queueCapacity == outCapacity

        where:
        inCapacity                               || outCapacity
        -1                                       || GameConfiguration.MAX_QUEUE_CAPACITY
        0                                        || 0
        10                                       || 10
        GameConfiguration.MAX_QUEUE_CAPACITY + 1 || GameConfiguration.MAX_QUEUE_CAPACITY
    }
}
