class D6(private val inputs: String) {
    fun solve(day: Int): Double {
        val memory = mutableMapOf<Pair<Int, Int>, Long>()
        val origin = IoHelper.getRawContent(inputs)!!.split(",").map { it.toInt() }
        val descendants = origin.sumByDouble { Fish(0, it, false, memory).countDescendants(day).toDouble() }

        return origin.size + descendants
    }
}

class Fish(
    private val birthDay: Int = 0,
    private val internalTimer: Int = 0,
    private val isNewBorn: Boolean = true,
    private val memory: MutableMap<Pair<Int, Int>, Long>
) {
    fun countDescendants(day: Int): Long {
        val key = Pair(birthDay, internalTimer)
        if (key in memory.keys) {
            return memory[key]!!
        }

        val counts = mutableListOf<Long>()
        val children = getChildren(day)
        counts.add(children.size.toLong())
        children.forEach {
            counts.add(it.countDescendants(day))
        }
        memory[key] = counts.sum()
        return counts.sum()
    }

    fun getChildren(day: Int): List<Fish> {
        val rounds = if (isNewBorn) (day - 2 - birthDay) / 7 else (day + (7 - internalTimer)) / 7
        return if (rounds == 0) emptyList() else
            (1..rounds).mapNotNull { round ->
                val childBirthday = if (isNewBorn) round * 7 + birthDay + 2 else round * 7 - (7 - internalTimer)
                if (childBirthday < day) Fish(childBirthday, 8, true, memory) else null
            }
    }

    override fun toString(): String {
        return "Fish(birthDay=$birthDay, internalTimer=$internalTimer, dayAndCount=$memory)"
    }
}

fun main() {
    println(D6("d6sample.txt").solve(80).toInt() == 5934)
    println(D6("d6.txt").solve(80).toInt() == 390011)
    println(D6("d6sample.txt").solve(256).toLong() == 26984457539)
    println(D6("d6.txt").solve(256).toLong() == 1746710169834)
}