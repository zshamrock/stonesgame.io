package com.bol.game

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject


class GameSpec extends Specification {

    @Subject
    @Shared
    def game = new Game()

    def "make a turn in the beginning of a game"() {
        when:
        def p1 = new Player(pits1 as int[])
        def p2 = new Player(pits2 as int[])
        game.turn(p1, pit, p2)

        then:
        p1.pits == outcome as int[]
        p2.pits == pits2 as int[]

        where:
        pits1                 | pit | pits2                 || outcome
        [6, 6, 6, 6, 6, 6, 0] | 0   | [6, 6, 6, 6, 6, 6, 0] || [0, 7, 7, 7, 7, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 1   | [6, 6, 6, 6, 6, 6, 0] || [7, 0, 7, 7, 7, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 2   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 0, 7, 7, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 3   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 0, 7, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 4   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 7, 0, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 5   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 7, 7, 0, 1]
    }

    def "result of a turn"() {

    }
}