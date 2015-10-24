package io.stonesgame.config
import io.dropwizard.jackson.Jackson
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

    def "can parse config content"() {
        given:
        def json = '''
            {
                "server": {
                    "rootPath":"/game/*",
                    "applicationConnectors":[{"type":"http","port":58080}],
                    "adminConnectors":[{"type":"http","port":58081}]
                },
                "metrics":{
                    "frequency":"5 minutes",
                    "reporters":[{"type":"console","timeZone":"UTC","output":"stdout"}]
                },
                "queueCapacity":100,
                "bots":{
                    "enabled":true,"scheduledPeriodInSeconds":10,"poolSize":10
                }
            }
'''
        def mapper = Jackson.newObjectMapper()

        when:
        def config = mapper.readValue(json, GameConfiguration)

        then:
        config.serverFactory.jerseyRootPath == '/game/*'
        config.metricsFactory.frequency.toString() == '5 minutes'
        config.queueCapacity == 100
        config.bots.enabled
        config.bots.scheduledPeriodInSeconds == 10
        config.bots.poolSize == 10
    }
}
