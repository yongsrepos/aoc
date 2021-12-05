import java.lang.Integer.min
import kotlin.math.max

class D5(private val inputs: String) {
    fun s1(): Int {
        return solve()
    }

    fun s2(): Int {
        return solve(false)
    }

    private fun solve(isS1: Boolean = true): Int {
        val posCount = mutableMapOf<Pair<Int, Int>, Int>()
        IoHelper.getLines(inputs).map { parseLine(it, isS1) }.forEach {
            it.forEach { pos ->
                if (pos in posCount) {
                    posCount[pos] = posCount[pos]!! + 1
                } else {
                    posCount[pos] = 1
                }
            }
        }
        return posCount.entries.count { it.value >= 2 }
    }

    private fun parseLine(string: String, isS1: Boolean = true): List<Pair<Int, Int>> {
        val pairs = string.split(" -> ").map {
            val splits = it.trim().split(",")
            splits[0].toInt() to splits[1].toInt()
        }
        val from = pairs[0]
        val to = pairs[1]
        return if (from.first == to.first) {
            IntRange(min(from.second, to.second), max(from.second, to.second)).map { Pair(from.first, it) }
                .toList()
        } else if (from.second == to.second) {
            IntRange(min(from.first, to.first), max(from.first, to.first)).map { Pair(it, from.second) }.toList()
        } else if (isS1) {
            emptyList()
        } else {
            parseDiagonalLine(from, to)
        }
    }

    private fun parseDiagonalLine(from: Pair<Int, Int>, to: Pair<Int, Int>): List<Pair<Int, Int>> {
        return if (from.first < to.first) {
            if (from.second < to.second) {
                // south-east
                IntRange(0, to.first - from.first).map { Pair(from.first + it, from.second + it) }.toList()
            } else {
                // north-east
                IntRange(0, to.first - from.first).map { Pair(from.first + it, from.second - it) }.toList()
            }
        } else {
            if (from.second < to.second) {
                // south-west
                IntRange(0, from.first - to.first).map { Pair(from.first - it, from.second + it) }.toList()
            } else {
                // north-west
                IntRange(0, from.first - to.first).map { Pair(from.first - it, from.second - it) }.toList()
            }
        }
    }
}

fun main() {
    println(D5("d05sample.txt").s1() == 5)
    println(D5("d05.txt").s1() == 5835)
    println(D5("d05sample.txt").s2() == 12)
    println(D5("d05.txt").s2() == 17013)
}