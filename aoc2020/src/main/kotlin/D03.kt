import utils.IoHelper
import kotlin.math.ceil

class D03 {
    fun getSolution1(): Int {
        return getInputs()
            .mapIndexed { index, s -> getCharAtSlope(s, getZeroBasedX(index, 3 to 1)) }
            .count { it == '#' }
    }

    fun getSolution2(): Long {
        return getXYSteps().map { getCount(it) }.reduce { acc, i -> acc * i }
    }

    private fun getCount(steps: Pair<Int, Int>): Long {
        return getInputs()
            .mapIndexed { index, s -> getCharAtSlope(s, getZeroBasedX(index, steps)) }
            .count { it == '#' }
            .toLong()
    }

    private fun getInputs(): List<String> {
        return IoHelper().getLines("d03.in")
    }

    private fun getZeroBasedX(zeroBasedDepth: Int, steps: Pair<Int, Int>): Int {
        return if (zeroBasedDepth % steps.second != 0) -1 else
            steps.first * (zeroBasedDepth / steps.second)
    }

    private fun getCharAtSlope(basePattern: String, zeroBasedX: Int): Char {
        if (zeroBasedX == -1) {
            return '?'
        }

        val repeatTimes = ceil((zeroBasedX + 1.0) / basePattern.length).toInt()

        return basePattern.repeat(repeatTimes)[zeroBasedX]
    }

    private fun getXYSteps(): List<Pair<Int, Int>> {
        return listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
    }
}
