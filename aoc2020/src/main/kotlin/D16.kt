import utils.IoHelper

class D16 {
    fun getSolution1(): Long {
        var invalidFields = mutableListOf<Long>()
        filterTickets(invalidFields = invalidFields)
        return invalidFields.sum()
    }

    fun getSolution2(): Long {
        val validTickets = getValidTickets()
        var ruleToCandidateFields = removeUnfitFieldsFromRule(validTickets)
        val ruleToField = mapRuleToField(ruleToCandidateFields)
        val myTicket = getMyTicket()
        var result = 1L
        getDepartureRuleIndice().forEach { result *= myTicket[ruleToField[it]!!] }
        return result
    }

    private fun removeUnfitFieldsFromRule(validTickets: List<List<Int>>): Map<Int, MutableSet<Int>> {
        var ruleToCandidateFields = IntRange(0, 19).map { it to IntRange(0, 19).toMutableSet() }.toMap()

        val rules = getRules()
        validTickets.forEach {
            it.forEachIndexed { fieldIndex, fieldInTicket ->
                rules.forEachIndexed { ruleIdx, rule ->
                    if (!isValidField(rule, fieldInTicket)) {
                        ruleToCandidateFields[ruleIdx]!!.remove(fieldIndex)
                    }
                }
            }
        }
        return ruleToCandidateFields
    }

    private fun mapRuleToField(ruleToCandidateIndice: Map<Int, MutableSet<Int>>): MutableMap<Int, Int> {
        val ruleToField = mutableMapOf<Int, Int>()
        for (len in 20 downTo 2) {
            val longer = ruleToCandidateIndice.filter { it.value.size == len }.toList()[0]
            val shorter = ruleToCandidateIndice.filter { it.value.size == len - 1 }.toList()[0]
            ruleToField[longer.first] = longer.second.subtract(shorter.second).toList()[0]
            if (len == 2) {
                ruleToField[shorter.first] = shorter.second.toList()[0]
            }
        }
        return ruleToField
    }

    private fun getValidTickets(): List<List<Int>> {
        var invalidTicketIdx = mutableListOf<Int>()
        filterTickets(invalidTicketIdx = invalidTicketIdx)
        return getNearbyTickets().filterIndexed { idx, _ -> idx !in invalidTicketIdx }
    }

    private fun filterTickets(
        invalidTicketIdx: MutableList<Int> = mutableListOf(),
        invalidFields: MutableList<Long> = mutableListOf()
    ) {
        getNearbyTickets().forEachIndexed { idx, ticket ->
            ticket.forEach {
                if (!isValidField(it)) {
                    invalidTicketIdx.add(idx)
                    invalidFields.add(it.toLong())
                }
            }
        }
    }

    private fun getDepartureRuleIndice(): List<Int> {
        return getInputs()[0].lines().mapIndexed { index, s -> if (s.contains("departure")) index else null }
            .filterNotNull()
    }

    private fun isValidField(field: Int): Boolean {
        return getRules().any { isValidField(it, field) }
    }

    private fun isValidField(rule: List<IntRange>, field: Int): Boolean {
        return rule.any { field in it }
    }

    private fun getRules(): List<List<IntRange>> {
        return getInputs()[0].lines().map { rule ->
            rule.split(": ")[1]
                .split(" or ")
                .map { it.toRange() }
        }
    }

    private fun getMyTicket(): List<Int> {
        return getInputs()[1].lines()[1].split(",").map { it.toInt() }
    }

    private fun getNearbyTickets(): List<List<Int>> {
        return getInputs()[2].lines().drop(1).map { ticket -> ticket.split(",").map { it.toInt() } }
    }

    private fun getInputs(): List<String> {
        return IoHelper().getSections("d16.in", "\n\n")
    }
}

fun String.toRange(): IntRange {
    val minAndMax = this.split("-").map { it.toInt() }
    return IntRange(minAndMax[0], minAndMax[1])
}
