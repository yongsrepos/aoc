import utils.IoHelper

class D22 {
    fun getSolution1(): Int {
        val winner = Game(getInputs()).playNormal()
        var result = 0
        for (i in 1..winner.cards.size) {
            result += i * winner.cards.removeLast()
        }
        return result
    }

    fun getSolution2(): Int {
        val winner = Game(getInputs()).playAdvanced()
        var result = 0
        for (i in 1..winner.cards.size) {
            result += i * winner.cards.removeLast()
        }
        return result
    }

    private fun getInputs(): Pair<Player, Player> {
        val players = IoHelper().getSections("d22.in", "\n\n")
            .map { it.trim().lines().drop(1) }.mapIndexed { index, rawCards ->
                val cards = ArrayDeque<Int>()
                rawCards.forEach { cards.addLast(it.toInt()) }
                Player(index, cards)
            }
        return players[0] to players[1]
    }
}

data class Player(val id: Int, val cards: ArrayDeque<Int>)

class Game(private val players: Pair<Player, Player>) {
    private val playerA = players.first
    private val playerB = players.second
    private val previousOrders = mutableListOf<Pair<List<Int>, List<Int>>>()

    fun playNormal(): Player {
        while (playerA.cards.isNotEmpty() && playerB.cards.isNotEmpty()) {
            val cardA = playerA.cards.removeFirst()
            val cardB = playerB.cards.removeFirst()
            if (cardA > cardB) {
                playerA.cards.addLast(cardA)
                playerA.cards.addLast(cardB)
            } else {
                playerB.cards.addLast(cardB)
                playerB.cards.addLast(cardA)
            }
        }

        return if (playerA.cards.isNotEmpty()) playerA else playerB
    }

    fun playAdvanced(): Player {
        while (playerA.cards.isNotEmpty() && playerB.cards.isNotEmpty()) {
            val aOrder = playerA.cards.toList()
            val bOrder = playerB.cards.toList()
            if (previousOrders.any { it.first == aOrder && it.second == bOrder }) {
                return playerA
            } else {
                previousOrders.add(aOrder to bOrder)
            }

            val cardA = playerA.cards.removeFirst()
            val cardB = playerB.cards.removeFirst()
            if (shouldPlayNewGame(cardA, cardB)) {
                val aCopy = Player(playerA.id, ArrayDeque(playerA.cards.subList(0, cardA)))
                val bCopy = Player(playerB.id, ArrayDeque(playerB.cards.subList(0, cardB)))
                val winner = Game(aCopy to bCopy).playAdvanced()
                if (winner.id == playerA.id) {
                    playerA.cards.addLast(cardA)
                    playerA.cards.addLast(cardB)
                } else {
                    playerB.cards.addLast(cardB)
                    playerB.cards.addLast(cardA)
                }
            } else {
                // normal play
                if (cardA > cardB) {
                    playerA.cards.addLast(cardA)
                    playerA.cards.addLast(cardB)
                } else {
                    playerB.cards.addLast(cardB)
                    playerB.cards.addLast(cardA)
                }
            }
        }

        return if (playerA.cards.isNotEmpty()) playerA else playerB
    }

    private fun shouldPlayNewGame(cardA: Int, cardB: Int): Boolean {
        return playerA.cards.size >= cardA && playerB.cards.size >= cardB
    }
}
