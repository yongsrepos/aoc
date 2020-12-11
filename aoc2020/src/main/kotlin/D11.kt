import utils.IoHelper

class D11 {
    fun getSolution1(): Int {
        return getSolution()
    }

    fun getSolution2(): Int {
        return getSolution(5, true)
    }

    private fun getSolution(maxOccupied: Int = 4, ignoreFloor: Boolean = false): Int {
        var current = getInputs()
        val state = State(true)
        while (state.isNotStable) {
            state.isNotStable = false
            current = applyRules(current, state, maxOccupied, ignoreFloor)
        }
        return getOccupied(current)
    }

    private fun applyRules(
        current: List<CharArray>,
        state: State,
        maxOccupied: Int,
        ignoreFloor: Boolean
    ): List<CharArray> {
        val after = current.map { it.copyOf() }
        for (i in current.indices) {
            for (j in current[i].indices) {
                when (current[i][j]) {
                    Pos.FLOOR.rep -> continue
                    Pos.EMPTY.rep -> if (shouldTake(current, i to j, ignoreFloor)) {
                        after[i][j] = Pos.OCCUPIED.rep
                        state.isNotStable = true
                    }
                    Pos.OCCUPIED.rep -> if (shouldLeave(current, i to j, maxOccupied, ignoreFloor)) {
                        after[i][j] = Pos.EMPTY.rep
                        state.isNotStable = true
                    }
                }
            }
        }
        return after
    }

    private fun shouldTake(current: List<CharArray>, pos: Pair<Int, Int>, ignoreFloor: Boolean): Boolean {
        return getSurroundings(current, pos, ignoreFloor).count { it == Pos.OCCUPIED.rep } == 0
    }

    private fun shouldLeave(
        current: List<CharArray>,
        pos: Pair<Int, Int>,
        maxOccupied: Int,
        ignoreFloor: Boolean
    ): Boolean {
        return getSurroundings(current, pos, ignoreFloor).count { it == Pos.OCCUPIED.rep } >= maxOccupied
    }

    private fun getSurroundings(current: List<CharArray>, pos: Pair<Int, Int>, ignoreFloor: Boolean): List<Char?> {
        val up = getPosMark(current, pos.first to pos.second, -1 to 0, ignoreFloor)
        val upRight = getPosMark(current, pos.first to pos.second, -1 to 1, ignoreFloor)
        val right = getPosMark(current, pos.first to pos.second, 0 to 1, ignoreFloor)
        val downRight = getPosMark(current, pos.first to pos.second, 1 to 1, ignoreFloor)
        val down = getPosMark(current, pos.first to pos.second, 1 to 0, ignoreFloor)
        val downLeft = getPosMark(current, pos.first to pos.second, 1 to -1, ignoreFloor)
        val left = getPosMark(current, pos.first to pos.second, 0 to -1, ignoreFloor)
        val upLeft = getPosMark(current, pos.first to pos.second, -1 to -1, ignoreFloor)
        return listOf(up, upRight, right, downRight, down, downLeft, left, upLeft)
    }

    private fun getPosMark(
        current: List<CharArray>,
        pos: Pair<Int, Int>,
        direction: Pair<Int, Int>,
        ignoreFloor: Boolean
    ): Char? {
        var newPos = pos.first + direction.first to pos.second + direction.second
        var mark = getPosMark(current, newPos)
        if (!ignoreFloor) {
            return mark
        }

        while (mark == Pos.FLOOR.rep) {
            newPos = newPos.first + direction.first to newPos.second + direction.second
            mark = getPosMark(current, newPos)
        }
        return mark
    }

    private fun getPosMark(current: List<CharArray>, pos: Pair<Int, Int>): Char? {
        val col = current[0].size
        val row = current.size
        return if (pos.first < 0 || pos.first >= row || pos.second < 0 || pos.second >= col) null
        else current[pos.first][pos.second]
    }

    private fun getOccupied(current: List<CharArray>): Int {
        return current.map { line -> line.count { it == Pos.OCCUPIED.rep } }.sum()
    }

    private fun getInputs(): List<CharArray> {
        return IoHelper().getLines("d11.in").map { it.toCharArray() }
    }
}

enum class Pos(val rep: Char) {
    FLOOR('.'),
    OCCUPIED('#'),
    EMPTY('L')
}

data class State(var isNotStable: Boolean)

