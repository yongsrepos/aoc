import java.util.Collections.max
import kotlin.math.abs

class D7(private val inputs: String) {
    private fun getInputs() = IoHelper.getRawContent(inputs)?.split(",")?.map { it.toInt() } ?: emptyList()
    fun s1(): Int {
        val positions = getInputs()
        val central = positions.sortedBy { pos -> positions.sumOf { abs(it - pos) } }[0]
        return positions.sumOf { abs(it - central) }
    }

    fun s2(): Int {
        val positions = getInputs()
        val central = (0..max(positions)).sortedBy { pos -> positions.sumOf { (0..abs(it - pos)).sum() } }[0]
        return positions.sumOf { (0..abs(it - central)).sum() }
    }
}

fun main() {
    println(D7("d7sample.txt").s1() == 37)
    println(D7("d7.txt").s1() == 325528)
    println(D7("d7sample.txt").s2() == 168)
    println(D7("d7.txt").s2() == 85015836)
}