class D10(override val inputs: String) : D0 {
    private val lines = IoHelper.getLines(inputs)
    private val symbolPairs = mapOf('>' to '<', ')' to '(', '}' to '{', ']' to '[')
    private val points = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    private val points2 = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

    fun getRemaining(origin: String): ArrayDeque<Char> {
        val queue = ArrayDeque<Char>()
        origin.forEach {
            if (it in symbolPairs.values) {
                queue.addLast(it)
            } else {
                if (symbolPairs[it] == queue.last()) {
                    queue.removeLast()
                } else {
                    queue.add(it)
                    return queue
                }
            }
        }
        return queue
    }

    override fun s1() =
        lines.map { getRemaining(it) }.filter { it.last() in symbolPairs.keys }.sumBy { points[it.last()] ?: 0 }

    override fun s2() = s2Long().toInt()

    fun s2Long(): Long {
        val tmp = lines.map { getRemaining(it) }
            .filter { it.last() in symbolPairs.values }
            .map { remaining ->
                remaining.map { c -> symbolPairs.entries.first { entry -> entry.value == c }.key }.reversed()
            }
            .map { it ->
                var sum = 0L
                it.forEach {
                    sum = sum * 5 + (points2[it] ?: 0)
                }
                sum
            }.sorted()

        return tmp[tmp.size / 2]
    }
}

fun main() {
    val sample = "d10sample.txt"
    val inputs = "d10.txt"
    println(D10(sample).s1() == 26397)
    println(D10(inputs).s1() == 315693)
    println(D10(sample).s2() == 288957)
    println(D10(inputs).s2Long() == 1870887234L)
}