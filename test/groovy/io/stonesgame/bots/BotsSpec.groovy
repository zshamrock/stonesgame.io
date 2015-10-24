package io.stonesgame.bots

import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.ArrayBlockingQueue


class BotsSpec extends Specification {
    @Unroll
    def "verify the correct pool size of #actualPoolSize is used if #poolSize was provided"() {
        given: "players queue to keep enough bots"
        def players = new ArrayBlockingQueue(Bots.MAX_POOL_SIZE + 1)
        def bots = new Bots(players, poolSize)

        when: '"running" #poolSize bots'
        (poolSize > 0 ? poolSize : Bots.MAX_POOL_SIZE).times {
            bots.run()
        }

        then: "actual number of run bots should be between [$Bots.MIN_POOL_SIZE, $Bots.MAX_POOL_SIZE]"
        players.size() == actualPoolSize

        where:
        poolSize               || actualPoolSize
        Bots.MAX_POOL_SIZE + 1 || Bots.MAX_POOL_SIZE
        0                      || Bots.MIN_POOL_SIZE
        -1                     || Bots.MIN_POOL_SIZE
        Bots.MIN_POOL_SIZE + 5 || Bots.MIN_POOL_SIZE + 5

    }
}
