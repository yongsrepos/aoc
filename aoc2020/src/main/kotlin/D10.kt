import utils.IoHelper
import kotlin.math.pow

class D10 {
    fun getSolution1(): Int {
        val sortedAdapters = getInputs()
        var onesCount = 1 // the 1st, connecting to ground
        var threesCount = 1 // the last, connecting to device
        for (i in 1 until sortedAdapters.size) {
            when (sortedAdapters[i] - sortedAdapters[i - 1]) {
                1 -> onesCount += 1
                3 -> threesCount += 1
            }
        }

        return onesCount * threesCount
    }

    fun getSolution2(): Long {
        val diffString = getDiffString()

        val p1 = "3113" to 2
        val p2 = "31113" to 4
        val p3 = "311113" to 7
        val p1Count = getOccurrences(diffString, p1.first)
        val p2Count = getOccurrences(diffString, p2.first)
        val p3Count = getOccurrences(diffString, p3.first)

        val chainHeadChoices = 4L
        val p1Combinations = p1.second.toDouble().pow(p1Count.toDouble()).toLong()
        val p2Combinations = p2.second.toDouble().pow(p2Count.toDouble()).toLong()
        val p3Combinations = p3.second.toDouble().pow(p3Count.toDouble()).toLong()

        return chainHeadChoices * p1Combinations * p2Combinations * p3Combinations
    }

    private fun getDiffString(): String {
        var str = ""
        val sortedAdapters = getInputs()
        for (i in 1 until sortedAdapters.size) {
            str = str.plus(sortedAdapters[i] - sortedAdapters[i - 1])
        }
        return str.plus(3) // the final to connect computer
    }

    private fun getOccurrences(wholeStr: String, subStr: String): Int {
        return wholeStr.windowed(subStr.length).count { it == subStr }
    }

    private fun getInputs(): MutableList<Int> {
        return IoHelper().getInts("d10.in").sorted().toMutableList()
    }
}
