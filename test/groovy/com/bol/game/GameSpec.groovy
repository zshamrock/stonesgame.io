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
        def players = game.turn(p1before as int[], pit, p2before as int[])

        then:
        players[0] == p1after as int[]
        players[1] == p2before as int[]

        where:
        p1before              | pit | p2before              || p1after
        [6, 6, 6, 6, 6, 6, 0] | 0   | [6, 6, 6, 6, 6, 6, 0] || [0, 7, 7, 7, 7, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 1   | [6, 6, 6, 6, 6, 6, 0] || [7, 0, 7, 7, 7, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 2   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 0, 7, 7, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 3   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 0, 7, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 4   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 7, 0, 7, 1]
        [6, 6, 6, 6, 6, 6, 0] | 5   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 7, 7, 0, 1]
    }
}