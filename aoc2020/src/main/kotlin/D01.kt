import java.lang.IllegalArgumentException

class D01 {
    private fun getInputs(): List<Int> {
        return utils.getInts(this, "d01.in")
    }

    private fun getPair(): Pair<Int, Int> {
        val inputs = getInputs()
        for (i in 0 until inputs.size - 1)
            for (j in i + 1 until inputs.size)
                if (inputs[i] + inputs[j] == 2020) {
                    return inputs[i] to inputs[j]
                }

        throw IllegalArgumentException("No pair sum up to 2020")
    }

    private fun getTriple(): Triple<Int, Int, Int> {
        val inputs = getInputs()
        for (i in 0 until inputs.size - 2)
            for (j in i + 1 until inputs.size - 1)
                for (k in j + 1 until inputs.size)
                    if (inputs[i] + inputs[j] + inputs[k] == 2020) {
                        return Triple(inputs[i], inputs[j], inputs[k])
                    }
        throw IllegalArgumentException("No triple sum up to 2020")
    }

    fun getSolution1(): Int {
        val (a, b) = getPair()
        return a * b
    }

    fun getSolution2(): Int {
        val (a, b, c) = getTriple()
        return a * b * c
    }
}
