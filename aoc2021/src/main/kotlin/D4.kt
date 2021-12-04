class D4(private val inputFileName: String) {
    private fun getNums(): List<Int> {
        return IoHelper.getSections(inputFileName, System.lineSeparator() + System.lineSeparator())[0].split(",")
            .map { it.toInt() }
    }

    private fun getBoards(): List<Board> {
        val sections = IoHelper.getSections(inputFileName, System.lineSeparator() + System.lineSeparator())
        return sections.takeLast(sections.size - 1).map { Board().initFromBlock(it) }
    }

    fun s1(): Int {
        val nums = getNums()
        val boards = getBoards()
        nums.forEach { num ->
            boards.forEach { board ->
                board.markIfExists(num)
                if (board.hasWon()) {
                    return board.getScore(num)
                }
            }
        }
        return 0
    }

    fun s2(): Int {
        val nums = getNums()
        val boards = getBoards().toMutableList()
        nums.forEach { num ->
            with(boards.iterator()) {
                forEach { board ->
                    board.markIfExists(num)
                    if (board.hasWon()) {
                        if (boards.size > 1) {
                            remove()
                        } else {
                            return board.getScore(num)
                        }
                    }
                }
            }
        }
        return 0
    }
}

class Board(private val size: Int = 5) {
    private val origins = mutableMapOf<Pair<Int, Int>, Int>()
    private val marking = mutableListOf<Pair<Int, Int>>()

    fun initFromBlock(block: String): Board {
        block.split(System.lineSeparator()).forEachIndexed { rIdx, str ->
            str.trim().split(" ").filter { it.isNotBlank() }.forEachIndexed { cIdx, v ->
                origins[Pair(rIdx, cIdx)] = v.trim().toInt()
            }
        }
        return this
    }

    fun markIfExists(value: Int): Board {
        origins.entries.find { it.value == value }?.let { marking.add(it.key) }
        return this
    }

    fun hasWon(): Boolean {
        for (i in 0 until size) {
            val rCount = marking.count { it.first == i }
            if (rCount == size) {
                return true
            }
            val cCount = marking.count { it.second == i }
            if (cCount == size) {
                return true
            }
        }
        return false
    }

    fun getScore(latestMarked: Int): Int {
        return origins.entries.filterNot { it.key in marking }.sumOf { it.value } * latestMarked
    }
}

fun main() {
    println(D4("d04sample.txt").s1() == 4512)
    println(D4("d04.txt").s1() == 2496)
    println(D4("d04sample.txt").s2() == 1924)
    println(D4("d04.txt").s2() == 25925)
}
