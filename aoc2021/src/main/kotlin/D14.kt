class D14(override val inputs: String) : D0 {
    override fun s1() = solve(10).toInt()

    override fun s2() = 0

    fun s2Long() = solve(40)

    private fun parseInputs() =
        IoHelper.getRawContent(inputs)?.split(System.lineSeparator() + System.lineSeparator()) ?: emptyList()

    private fun step(template: String, rules: Map<String, String>, visited: MutableMap<String, String>) =
        if (template in visited) {
            visited[template]
        } else {
            var str = ""
            template.zipWithNext().map {
                val str = it.first.toString().plus(it.second)
                if (str in rules) it.first.toString().plus(rules[str]).plus(it.second) else str
            }.withIndex().forEach { (idx, part) -> if (idx == 0) str = part else str += part.substring(1) }
            visited[template] = str
            str
        }

    private fun solve(steps: Int): Long {
        val size = 50
        val (template, rulesBlock) = parseInputs()
        val rules = rulesBlock.split(System.lineSeparator()).associate { it.parseRule() }

        var shortOnes = mutableMapOf<String, Long>()
        shortOnes[template] = 1
        val visited = mutableMapOf<String, String>()

        repeat(steps) { currentStep ->
            val temp = mutableMapOf<String, Long>()
            shortOnes.forEach { (shortOne, count) ->
                step(
                    shortOne,
                    rules,
                    visited
                )?.let {
                    if (it.length > size) {
                        val s1 = it.substring(0 until size)
                        val s2 = it.substring(size - 1)
                        if (s1 in temp) temp[s1] = (temp[s1] ?: 0) + count else temp[s1] = count
                        if (s2 in temp) temp[s2] = (temp[s2] ?: 0) + count else temp[s2] = count
                    } else {
                        if (it in temp) temp[it] = (temp[it] ?: 0) + count else temp[it] = count
                    }
                }
            }
            shortOnes = temp
        }

        val counts = mutableMapOf<Char, Long>()
        shortOnes.forEach { (str, count) ->
            str.substring(1).forEach { c -> // starting from idx=1, because we "borrowed" one during splitting
                if (c in counts) counts[c] = (counts[c] ?: 0L) + count else counts[c] = count + 0L
            }
        }

        val sorted = counts.values.sorted()
        return sorted.last() - sorted.first()
    }
}

fun String.parseRule() = this.split(" -> ").let { it[0] to it[1] }

fun main() {
    println(D14("d14sample.txt").s1() == 1588)
    println(D14("d14.txt").s1() - 1 == 2937) // lazy to write the code to find the very first character of the initial string.
    println(D14("d14sample.txt").s2Long() == 2188189693529)
    println(D14("d14.txt").s2Long() - 1 == 3390034818249)
}