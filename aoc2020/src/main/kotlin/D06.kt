import utils.IoHelper

class D06 {
    fun getSolution1(): Int {
        return getSumAfterReduction { acc, other -> acc.union(other) }
    }

    fun getSolution2(): Int {
        return getSumAfterReduction { acc, other -> acc.intersect(other) }
    }

    private fun getSumAfterReduction(reductionFun: (acc: Set<Char>, other: Set<Char>) -> Set<Char>): Int {
        return IoHelper().getSections("d06.in", "\n\n")
            .map { it.split("\n") }
            .map { groupAnswers ->
                groupAnswers.map { it.toSet() }.reduce { acc, set -> reductionFun(acc, set) }.size
            }.sum()
    }
}
