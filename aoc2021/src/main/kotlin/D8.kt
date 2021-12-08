class D8(private val inputs: String) {
    fun s1() = IoHelper.getLines(inputs).map { line ->
        line.split(" | ")[1].split(" ")
            .count {
                it.length in listOf(
                    Digit.ONE.segments,
                    Digit.FOUR.segments,
                    Digit.SEVEN.segments,
                    Digit.EIGHT.segments
                )
            }
    }.sum()

    fun s2(): Int {
        return IoHelper.getLines(inputs).sumOf { line ->
            val inAndOut = line.split(" | ")
            val encodings = getEncodings(inAndOut[0].split(" "))
            inAndOut[1].split(" ").map { p ->
                encodings.entries.first { p.toSet() == it.key.toSet() }.value.value }.joinToString("").toInt()
        }
    }

    private fun getEncodings(currentInputs: List<String>): Map<String, Digit> {
        val one = currentInputs.first { it.length == Digit.ONE.segments }
        val four = currentInputs.first { it.length == Digit.FOUR.segments }
        val seven = currentInputs.first { it.length == Digit.SEVEN.segments }
        val eight = currentInputs.first { it.length == Digit.EIGHT.segments }

        val a = seven.first { it !in one }
        val remainingInFour = four.toList().filter { it !in one }
        val nine = currentInputs.first { out ->
            out.length == 6
                && remainingInFour.all { it in out }
                && out.contains(a)
                && one.toList().all { it in out }
        }
        val g = nine.toList().first { it !in one && it !in four && it != a }
        val e = eight.toList().first { it !in nine }

        val two = currentInputs.first { out ->
            out.length == Digit.TWO.segments &&
                listOf(a, e, g).all { it in out }
        }

        val d = two.toList().first {
            it !in one && it !in listOf(a, e, g)
        }
        val c = two.toList().first { it !in listOf(a, d, e, g) }
        val f = one.toList().first { it != c }
        val b = four.toList().first { it !in listOf(c, d, f) }

        return mapOf(
            listOf(a, b, c, e, f, g).joinToString("") to Digit.ZERO,
            listOf(c, f).joinToString("") to Digit.ONE,
            listOf(a, c, d, e, g).joinToString("") to Digit.TWO,
            listOf(a, c, d, f, g).joinToString("") to Digit.THREE,
            listOf(b, c, d, f).joinToString("") to Digit.FOUR,
            listOf(a, b, d, f, g).joinToString("") to Digit.FIVE,
            listOf(a, b, d, e, f, g).joinToString("") to Digit.SIX,
            listOf(a, c, f).joinToString("") to Digit.SEVEN,
            listOf(a, b, c, d, e, f, g).joinToString("") to Digit.EIGHT,
            listOf(a, b, c, d, f, g).joinToString("") to Digit.NINE,
        )
    }
}

enum class Digit(val value: Int, val segments: Int) {
    ZERO(0, 6),
    ONE(1, 2),
    TWO(2, 5),
    THREE(3, 5),
    FOUR(4, 4),
    FIVE(5, 5),
    SIX(6, 6),
    SEVEN(7, 3),
    EIGHT(8, 7),
    NINE(9, 6)
}

fun main() {
    val sample = "d8sample.txt"
    val inputs = "d8.txt"
    println(D8(sample).s1() == 26)
    println(D8(inputs).s1() == 352)
    println(D8(sample).s2() == 61229)
    println(D8(inputs).s2() == 936117)
}