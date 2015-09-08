package com.bol.game.core

import spock.lang.Specification
import spock.lang.Unroll

class GameSpec extends Specification {

    def "make a turn in the beginning of a game"() {
        given:
        def game = new Game()

        when:
        game.pick(pit)

        then:
        game.players[0] == outcome as int[]
        game.players[1] == pits2 as int[]

        where:
        pit | pits2                 || outcome
        0   | [6, 6, 6, 6, 6, 6, 0] || [0, 7, 7, 7, 7, 7, 1]
        1   | [6, 6, 6, 6, 6, 6, 0] || [7, 0, 7, 7, 7, 7, 1]
        2   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 0, 7, 7, 7, 1]
        3   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 0, 7, 7, 1]
        4   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 7, 0, 7, 1]
        5   | [6, 6, 6, 6, 6, 6, 0] || [7, 7, 7, 7, 7, 0, 1]
    }

    def "multiple rounds"() {
        given:
        def game = new Game()

        when:
        picks.each { pit ->
            game.pick(pit)
        }

        then:
        game.players[0] == pits1 as int[]
        game.players[1] == pits2 as int[]

        where:
        picks = [0, 1, 2, 3, 4, 5]
        pits1 = [4, 2, 10, 2, 1, 1, 12]
        pits2 = [8, 1, 1, 8, 0, 8, 14]
    }

    @Unroll
    def "game over for #players"() {
        given:
        def game = new Game(players as int[][])

        when:
        def isOver = game.pick(pit)

        then:
        isOver == over

        where:
        players                                         | pit || over
        [[0, 0, 0, 0, 0, 1, 10], [6, 5, 4, 3, 2, 1, 5]] | 5   || true
        [[0, 0, 0, 0, 0, 2, 10], [6, 0, 0, 0, 0, 1, 5]] | 5   || true
        [[0, 0, 0, 0, 1, 2, 10], [6, 0, 0, 0, 0, 0, 5]] | 5   || true
        [[1, 0, 0, 0, 0, 2, 10], [6, 5, 4, 3, 2, 1, 6]] | 5   || false
    }

    def "score of the game"() {
        given:
        def game = new Game(players as int[][])

        when:
        game.pick(pit)

        then:
        game.score == score as int[]

        where:
        players                                         | pit || score
        [[0, 0, 0, 0, 0, 1, 10], [6, 5, 4, 3, 2, 1, 5]] | 5   || [11, 26]
        [[0, 0, 0, 0, 0, 2, 10], [6, 0, 0, 0, 0, 1, 5]] | 5   || [18, 6]
        [[1, 0, 0, 0, 0, 2, 10], [6, 5, 4, 3, 2, 1, 6]] | 5   || [11, 6]
        [[0, 0, 0, 0, 0, 2, 10], [6, 1, 7, 2, 3, 1, 5]] | 5   || [18, 19]
    }

    def "score in the beginning of the game"() {
        given:
        def game = new Game()

        expect:
        game.score == [0, 0] as int[]
    }

    def "board after game over"() {
        given:
        def game = new Game([[0, 0, 0, 0, 1, 0, 5], [6, 5, 4, 3, 2, 1, 8]] as int[][])

        when:
        def over = game.pick(4)

        then:
        over
        game.getPlayers()[0] == [0, 0, 0, 0, 0, 0, 7] as int[]
        game.getPlayers()[1] == [0, 0, 0, 0, 0, 0, 28] as int[]
    }
}