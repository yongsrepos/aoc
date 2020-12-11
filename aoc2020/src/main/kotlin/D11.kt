import utils.IoHelper

class D11 {
    fun getSolution1(): Int {
        return getSolution()
    }

    fun getSolution2(): Int {
        return getSolution(5, false)
    }

    private fun getSolution(maxOccupied: Int = 4, evaluateFloor: Boolean = true): Int {
        var current = getInputs()
        val state = State(false)
        while (!state.isStable) {
            state.isStable = true
            current = applyRules(current, state, maxOccupied, evaluateFloor)
        }
        return getOccupied(current)
    }

    private fun applyRules(
        current: List<CharArray>,
        state: State,
        maxOccupied: Int,
        evaluateFloor: Boolean
    ): List<CharArray> {
        val after = current.map { it.copyOf() }

        for (row in current.indices) {
            for (col in current[row].indices) {
                when (current[row][col]) {
                    Pos.FLOOR.mark -> continue
                    Pos.EMPTY.mark -> if (shouldTake(current, row to col, evaluateFloor)) {
                        after[row][col] = Pos.OCCUPIED.mark
                        state.isStable = false
                    }
                    Pos.OCCUPIED.mark -> if (shouldLeave(current, row to col, maxOccupied, evaluateFloor)) {
                        after[row][col] = Pos.EMPTY.mark
                        state.isStable = false
                    }
                }
            }
        }

        return after
    }

    private fun shouldTake(current: List<CharArray>, pos: Pair<Int, Int>, evaluateFloor: Boolean): Boolean {
        return getSurroundings(current, pos, evaluateFloor).count { it == Pos.OCCUPIED.mark } == 0
    }

    private fun shouldLeave(
        current: List<CharArray>,
        pos: Pair<Int, Int>,
        maxOccupied: Int,
        evaluateFloor: Boolean
    ): Boolean {
        return getSurroundings(current, pos, evaluateFloor).count { it == Pos.OCCUPIED.mark } >= maxOccupied
    }

    private fun getSurroundings(current: List<CharArray>, pos: Pair<Int, Int>, evaluateFloor: Boolean): List<Char?> {
        val up = getPosMark(current, pos.first to pos.second, -1 to 0, evaluateFloor)
        val upRight = getPosMark(current, pos.first to pos.second, -1 to 1, evaluateFloor)
        val right = getPosMark(current, pos.first to pos.second, 0 to 1, evaluateFloor)
        val downRight = getPosMark(current, pos.first to pos.second, 1 to 1, evaluateFloor)
        val down = getPosMark(current, pos.first to pos.second, 1 to 0, evaluateFloor)
        val downLeft = getPosMark(current, pos.first to pos.second, 1 to -1, evaluateFloor)
        val left = getPosMark(current, pos.first to pos.second, 0 to -1, evaluateFloor)
        val upLeft = getPosMark(current, pos.first to pos.second, -1 to -1, evaluateFloor)

        return listOf(up, upRight, right, downRight, down, downLeft, left, upLeft)
    }

    private fun getPosMark(
        current: List<CharArray>,
        pos: Pair<Int, Int>,
        direction: Pair<Int, Int>,
        evaluateFloor: Boolean
    ): Char? {
        var newPos = pos.first + direction.first to pos.second + direction.second
        var mark = getPosMark(current, newPos)
        if (evaluateFloor) {
            return mark
        }

        while (mark == Pos.FLOOR.mark) {
            newPos = newPos.first + direction.first to newPos.second + direction.second
            mark = getPosMark(current, newPos)
        }
        return mark
    }

    private fun getPosMark(current: List<CharArray>, pos: Pair<Int, Int>): Char? {
        val rowSize = current.size
        val colSize = current[0].size
        return if (pos.first < 0 || pos.first >= rowSize || pos.second < 0 || pos.second >= colSize) null
        else current[pos.first][pos.second]
    }

    private fun getOccupied(current: List<CharArray>): Int {
        return current.map { line -> line.count { it == Pos.OCCUPIED.mark } }.sum()
    }

    private fun getInputs(): List<CharArray> {
        return IoHelper().getLines("d11.in").map { it.toCharArray() }
    }
}

enum class Pos(val mark: Char) { FLOOR('.'), OCCUPIED('#'), EMPTY('L') }

data class State(var isStable: Boolean)
