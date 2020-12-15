class D15 {
    fun getSolution1(): Int {
        return speak(2020)
    }

    fun getSolution2(): Int {
        return speak(30000000)
    }

    private fun speak(expectedTurn: Int): Int {
        var spokenAnd2Turns = mutableMapOf<Int, Pair<Int, Int>>()
        val inputs = getInputs()
        inputs.forEachIndexed { idx, num -> spokenAnd2Turns[num] = idx + 1 to idx + 1 }
        var spoken = inputs.last()
        for (turn in (inputs.size + 1)..expectedTurn) {
            val latest2Turns = spokenAnd2Turns[spoken]!!
            spoken = if (latest2Turns.second == latest2Turns.first) 0 else latest2Turns.second - latest2Turns.first
            spokenAnd2Turns[spoken] = if (spoken !in spokenAnd2Turns) turn to turn
            else spokenAnd2Turns[spoken]!!.second to turn
        }
        return spoken
    }

    private fun getInputs(): List<Int> {
        return "0,6,1,7,2,19,20".split(",").map { it.toInt() }
    }
}
