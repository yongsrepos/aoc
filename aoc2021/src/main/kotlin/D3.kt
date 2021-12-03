object D3 {
    private fun getInputs() = IoHelper.getLines("d03.txt")
    fun s1(): Int {
        val inputs = getInputs()
        var mostCommons = ""
        for (pos in 0 until inputs[0].length) {
            mostCommons += inputs.groupBy { it[pos] }.entries.sortedByDescending { it.value.size }[0].key
        }
        val gammaRate = mostCommons.toInt(2)
        val epsilontRate = Integer.toBinaryString(gammaRate.inv()).takeLast(mostCommons.length).toInt(2)
        return gammaRate * epsilontRate
    }

    fun s2(): Int {
        val inputs = getInputs()
        val oIdx = s2Sub(inputs.withIndex().map { Pair(it.index, it.value) }, false, '1')
        val cIdx = s2Sub(inputs.withIndex().map { Pair(it.index, it.value) }, true, '0')

        return Integer.valueOf(inputs[oIdx], 2) * Integer.valueOf(inputs[cIdx], 2)
    }

    private fun s2Sub(
        numsWithIdx: List<Pair<Int, String>>,
        isAsc: Boolean,
        preferredChar: Char
    ): Int {
        require(numsWithIdx.isNotEmpty())

        if (numsWithIdx.size == 1) {
            return (numsWithIdx[0].first)
        }
        val sorted =
            numsWithIdx.groupBy { it.second[0] }.entries.sortedBy { if (isAsc) it.value.size else -it.value.size }
        var mostCommonNums = sorted[0].value
        if (sorted[0].value.size == sorted[1].value.size) {
            mostCommonNums = sorted.first { it.key == preferredChar }.value
        }
        val subMostCommonNums = mostCommonNums.map { Pair(it.first, it.second.substring(1)) }
        return s2Sub(subMostCommonNums, isAsc, preferredChar)
    }
}

fun main() {
    println(D3.s1() == 741950)
    println(D3.s2() == 903810)
}