class D9(override val inputs: String) : D0 {
    private fun getMap(): HeightMap {
        val lines = IoHelper.getLines(inputs).map { it.toList().map { digit -> digit.toString().toInt() } }
        return HeightMap(lines[0].size, lines.size).init(lines)
    }

    override fun s1(): Int {
        val map = getMap()
        return map.getLowPoints().sumOf { map.getHeight(it) + 1 }
    }

    override fun s2() = getMap().getBasins().map { it.size }.subList(0, 3).reduce(Int::times)
}

class HeightMap(private val width: Int, private val height: Int) {
    private val heights = mutableMapOf<Pair<Int, Int>, Int>()

    fun getNeighbours(pos: Pair<Int, Int>): List<Pair<Int, Int>> = listOf(
        pos.first to pos.second + 1,
        pos.first to pos.second - 1,
        pos.first + 1 to pos.second,
        pos.first - 1 to pos.second
    ).filter { it.first in 0 until width && it.second in 0 until height }

    fun init(values: List<List<Int>>): HeightMap {
        values.withIndex().forEach { row ->
            row.value.withIndex().forEach { col ->
                heights[col.index to row.index] = col.value
            }
        }
        return this
    }

    fun getLowPoints(): List<Pair<Int, Int>> = heights.entries.filter { me ->
        getNeighbours(me.key).all { neighbour ->
            heights[neighbour]!! > heights[me.key]!!
        }
    }.map { it.key }

    fun getHeight(pos: Pair<Int, Int>): Int = heights[pos]!!

    fun getBasins(): List<List<Pair<Int, Int>>> = getLowPoints().map { low ->
        val basin = mutableListOf(low)
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(low)
        while (queue.isNotEmpty()) {
            val head = queue.removeFirst()
            getNeighbours(head).filter { getHeight(it) != 9 && it !in basin }.forEach {
                queue.add(it)
                basin.add(it)
            }
        }
        basin
    }.sortedByDescending { it.size }.toList()
}

fun main() {
    val sample = "d9sample.txt"
    val inputs = "d9.txt"
    println(D9(sample).s1() == 15)
    println(D9(inputs).s1() == 475)
    println(D9(sample).s2() == 1134)
    println(D9(inputs).s2() == 1092012)
}