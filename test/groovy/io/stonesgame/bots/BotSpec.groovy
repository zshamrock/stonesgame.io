package io.stonesgame.bots

import io.stonesgame.web.Player
import io.stonesgame.web.WebGame
import spock.lang.Specification

class BotSpec extends Specification {
    def "bot joins the game"() {
        given: "a new bot"
        def lifecycle = Mock(BotLifecycle)
        def bot = new Bot(lifecycle)

        when: "joins a game"
        bot.join(new WebGame(UUID.randomUUID(), Mock(Player), Mock(Player)))

        then: "lifecycle gets notified"
        1 * lifecycle.joined(bot)
        0 * lifecycle._
    }

    def "game is over"() {
        given: "a new bot"
        def lifecycle = Mock(BotLifecycle)
        def bot = new Bot(lifecycle)

        when: "leaves a game"
        bot.over()

        then: "lifecycle gets notified"
        1 * lifecycle.over(bot)
        0 * lifecycle._
    }

    def "bot is always ready"() {
        given:
        def bot = new Bot()

        expect:
        bot.ready
    }

    def "bot is a bot"() {
        given:
        def bot = new Bot()

        expect:
        bot.bot
    }

}
