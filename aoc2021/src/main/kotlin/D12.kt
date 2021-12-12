class D12(override val inputs: String) : D0 {
    override fun s1() = solve()

    private fun solve(allowDoubleLowerCase: Boolean = false): Int {
        var pathCount = 0
        val meToNeighbours = buildNeighbourhood()
        val subPathes = mutableListOf(listOf("start"))
        while (subPathes.isNotEmpty()) {
            val current = subPathes.removeFirst()
            val tail = current.last()
            meToNeighbours[tail]?.forEach { neighbour ->
                if (neighbour == "end") {
                    pathCount++
                } else if (neighbour.isUpperCase()
                    || (neighbour.isLowerCase() && (neighbour !in current || (allowDoubleLowerCase && !current.hasDoubleVisitedLowerCase() && neighbour != "start")))
                ) {
                    subPathes.add(current + neighbour)
                }
            }
        }

        return pathCount
    }

    private fun buildNeighbourhood(): MutableMap<String, MutableList<String>> {
        val map = mutableMapOf<String, MutableList<String>>()
        IoHelper.getLines(inputs).forEach {
            val (me, neighbour) = it.parseConnection()
            map.addOrExtend(me, neighbour)
            map.addOrExtend(neighbour, me)
        }
        return map
    }

    override fun s2() = solve(true)
}

fun String.isEnd() = this == "end"
fun String.isUpperCase() = this == this.toUpperCase()
fun String.isLowerCase() = this == this.toLowerCase()
fun String.parseConnection() = this.split("-").let { it[0] to it[1] }
fun MutableMap<String, MutableList<String>>.addOrExtend(me: String, neighbour: String) {
    if (me in this) {
        this[me]?.add(neighbour)
    } else {
        this[me] = mutableListOf(neighbour)
    }
}

fun List<String>.hasDoubleVisitedLowerCase() = this.filter { it.isLowerCase() }.groupBy { it }.any { it.value.size > 1 }

fun main() {
    println(D12("d12sample.txt").s1() == 226)
    println(D12("d12.txt").s1() == 3679)
    println(D12("d12sample.txt").s2() == 3509)
    println(D12("d12.txt").s2() == 107395)
}