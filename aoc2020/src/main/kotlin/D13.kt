import utils.IoHelper
import java.lang.IllegalStateException

class D13 {
    fun getSolution1(): Int {
        var busWaitTime = mutableMapOf<Int, Int>()
        val inputs = getInputs()
        for (bus in inputs.second) {
            var busLeaveTime = bus
            while (busLeaveTime < inputs.first) {
                busLeaveTime += bus
            }
            busWaitTime[bus] = busLeaveTime - inputs.first
        }
        val earliestBus = busWaitTime.toList().sortedBy { (key, value) -> value }[0].first
        return busWaitTime[earliestBus]!!.times(earliestBus)
    }

    fun getSolution2(): Long {
        // 743 is largest in input, 643 second largest, 612 is the remainder between these two buses for the time t when 743 leaves
        val magicMin = getMin(743, 643, 612)

        // the lower bound if from the hint in the task:
        // "However, with so many bus IDs in your list, surely the actual earliest timestamp will be larger than 100000000000000!"
        for (trial in 100000000..1000000000000000) {
            // 743 is the biggest, 643 second biggest, 109 is the minimum num to satisfy: 743*num%643=612
            var t = 743 * (magicMin!! + 643L * trial)
            if (isValidIntermediateValue(t)) {
                val result = t - 19
                println("Gotcha=$result")
                return result
            }
        }
        throw IllegalStateException("Hejdå!")
    }

    // primeOne * output % primeTwo = remainder
    private fun getMin(primeOne: Int, primeTwo: Int, remainder: Int): Int? {
        for (i in 1..primeTwo) {
            if (primeOne * i % primeTwo == remainder) {
                return i
            }
        }

        return null
    }

    private fun getInputs(): Pair<Int, List<Int>> {
        val lines = IoHelper().getLines("d13.in")
        val departureTime = lines[0].toInt()
        val serviceBuses = lines[1].split(",").filter { it != "x" }.map { it.toInt() }
        return departureTime to serviceBuses
    }

    private fun isValidIntermediateValue(n: Long): Boolean {
        return (n % 19 == 0L)
                && (n % 41 == 10L)
                && (n % 743 == 0L)
                && (n % 13 == 0L)
                && (n % 17 == 3L)
                && (n % 29 == 0L)
                && (n % 643 == 612L)
                && (n % 37 == 0L)
                && (n % 23 == 15L)
    }
}
