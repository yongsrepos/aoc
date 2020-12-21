import utils.IoHelper

class D19 {
    private val resolved = mutableMapOf<Int, MutableList<String>>()
    private val unresolved = mutableMapOf<Int, List<List<Int>>>()
    fun getSolution1(): Int {
        IoHelper().getSections("d1901.in", "\n\n")[0].lines().map { parse(it.trim()) }
        tryResolve { 0 !in resolved }
        return IoHelper().getSections("d1901.in", "\n\n")[1].lines().count { it.trim() in resolved[0]!! }
    }

    fun getSolution2(): Int {
        IoHelper().getSections("d1902.in", "\n\n")[0].lines().map { parse(it.trim()) }
        tryResolve { 42 !in resolved || 31 !in resolved }
        return IoHelper().getSections("d1902.in", "\n\n")[1].lines().count { isForSolution2(it) }
    }

    private fun tryResolve(shouldContinue: () -> Boolean) {
        while (shouldContinue()) {
            for (toSolve in unresolved.toMutableMap()) {
                var isMessageResolvable = true
                for (ruleIndexes in toSolve.value) {
                    if (ruleIndexes.any { it !in resolved.keys }) {
                        isMessageResolvable = false
                        break
                    }
                }
                if (isMessageResolvable) {
                    resolve(toSolve)
                }
            }
        }
    }

    private fun resolve(toResolve: MutableMap.MutableEntry<Int, List<List<Int>>>) {
        toResolve.value.forEach { ruleIndexes ->
            val listOfRuleChoices = ruleIndexes.map { resolved[it]!! }
            val listOfRuleChoiceCombinations = genCombo(listOfRuleChoices)
            val resolutions =
                listOfRuleChoiceCombinations.map { it.joinToString(separator = "") } as MutableList<String>
            if (toResolve.key !in resolved) {
                resolved[toResolve.key] = resolutions
            } else {
                resolved[toResolve.key]!!.addAll(resolutions)
            }
        }
        unresolved.remove(toResolve.key)
    }

    private fun parse(rawRule: String) {
        val splits = rawRule.split(": ")
        val ruleIdx = splits[0].toInt()
        if (rawRule.contains('"')) {
            resolved[ruleIdx] = mutableListOf(splits[1].replace("\"", ""))
        } else {
            unresolved[ruleIdx] = splits[1].split(" | ").map { combo -> combo.split(" ").map { it.toInt() } }
        }
    }

    private fun isForSolution2(message: String): Boolean {
        var cp = message
        var tail = cp.substring(cp.length - 8) // rule 31 & 42 have substr with length = 8
        var tailMatchTimes = 0
        while (tail in resolved[31]!!) {
            tailMatchTimes += 1
            cp = cp.dropLast(8)
            if (cp.isEmpty()) {
                return false
            }
            tail = cp.substring(cp.length - 8)
        }
        if (tailMatchTimes == 0) {
            return false
        }
        var headMatchTimes = 0
        while (cp.isNotEmpty()) {
            val head = cp.slice(0..7)
            if (head !in resolved[42]!!) {
                return false
            }
            cp = cp.drop(8)
            headMatchTimes += 1
        }
        return headMatchTimes >= tailMatchTimes + 1
    }
}

fun <T> genCombo(fields: List<List<T>>): List<List<T>> {
    if (fields.isEmpty()) {
        return listOf()
    }
    if (fields.size == 1) {
        return fields[0].map { listOf(it) }
    }

    return fields[0].flatMap { elem ->
        genCombo(fields.drop(1))
            .map {
                val tmp = mutableListOf(elem) // order matters, elem must be the first
                tmp.addAll(it)
                tmp.toList()
            }
    }
}
