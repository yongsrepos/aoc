import utils.IoHelper

class D02 {
    fun getSolution1(): Int {
        return this.getInputs().count { isValid(parseRecord(it)) }
    }

    fun getSolution2(): Int {
        return this.getInputs().count { isValid2(parseRecord(it)) }
    }

    private fun getInputs(): List<String> {
        return IoHelper().getLines("d02.in")
    }

    private fun parseRecord(record: String): Pair<Triple<Int, Int, Char>, String> {
        val splitted = record.split(":").map { it.trim() }
        val policyStrs = splitted[0].split(" ")
        val minCount = policyStrs[0].split("-")[0].toInt()
        val maxCount = policyStrs[0].split("-")[1].toInt()
        val policyTriple = Triple(minCount, maxCount, policyStrs[1][0])
        val pwd = splitted[1]
        return policyTriple to pwd
    }

    private fun isValid(policyAndPwd: Pair<Triple<Int, Int, Char>, String>): Boolean {
        val requiredChar = policyAndPwd.first.third
        val count = policyAndPwd.second.count { it == requiredChar }
        return count in policyAndPwd.first.first..policyAndPwd.first.second
    }

    private fun isValid2(policyAndPwd: Pair<Triple<Int, Int, Char>, String>): Boolean {
        val requiredChar = policyAndPwd.first.third
        val matchFirst = policyAndPwd.second[policyAndPwd.first.first - 1] == requiredChar
        val matchSecond = policyAndPwd.second[policyAndPwd.first.second - 1] == requiredChar
        return matchFirst.xor(matchSecond)
    }
}
