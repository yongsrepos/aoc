class D11(override val inputs: String) : D0 {
    override fun s1() = Grid(10).init(IoHelper.getLines(inputs)).walkSteps(100)

    override fun s2() = (1..10).map {
        val trySteps = it * 100
        try {
            Grid(10).init(IoHelper.getLines(inputs)).walkSteps2(trySteps)
        } catch (e: Exception) {
            println("keep trying")
            0
        }
    }.maxOf { it }
}

class Grid(private val width: Int = 10) {
    private val octopuses = mutableMapOf<Pair<Int, Int>, Octopus>()

    private fun getNeighbours(me: Pair<Int, Int>): List<Pair<Int, Int>> = listOf(
        me.first to me.second + 1,
        me.first to me.second - 1,
        me.first - 1 to me.second - 1,
        me.first + 1 to me.second - 1,
        me.first + 1 to me.second,
        me.first - 1 to me.second,
        me.first - 1 to me.second + 1,
        me.first + 1 to me.second + 1,
    ).filter { it.first in 0 until width && it.second in 0 until width }

    fun init(lines: List<String>): Grid {
        lines.withIndex().forEach { (y, line) ->
            line.withIndex().forEach { (x, c) ->
                octopuses[y to x] = Octopus(c.toString().toInt())
            }
        }
        return this
    }

    fun walkSteps(steps: Int) = (1..steps).sumBy { walkStep() }

    fun walkSteps2(steps: Int) = (1..steps).first {
        walkStep() == 100
    }

    fun walkStep(): Int {
        var flashed = 0
        increaseEnergy()
        while (true) {
            val readyOnes = getReadyOnes()
            if (readyOnes.isEmpty()) {
                break
            }
            flash(readyOnes)
            increaseNeighbours(readyOnes)
            flashed += readyOnes.size
        }
        resetForNextStep()
        return flashed
    }

    fun increaseEnergy() = octopuses.values.forEach { it.increaseEnergy() }
    fun getReadyOnes(): List<Pair<Int, Int>> =
        octopuses.entries.filter { it.value.energy > 9 && !it.value.flashed }.map { it.key }

    fun flash(readyOnes: List<Pair<Int, Int>>) = readyOnes.forEach { octopuses[it]?.flash() }

    fun increaseNeighbours(newlyFlashed: List<Pair<Int, Int>>) = newlyFlashed.forEach { flashed ->
        getNeighbours(flashed).forEach { octopuses[it]?.increaseEnergy() }
    }

    fun resetForNextStep() = octopuses.values.filter { it.flashed }.forEach { it.resetForNextStep() }

}

class Octopus(var energy: Int, var flashed: Boolean = false) {
    fun increaseEnergy() {
        energy += 1
    }

    fun resetForNextStep() {
        energy = 0
        flashed = false
    }

    fun flash() {
        flashed = true
    }
}

fun main() {
    val sample = "d11sample.txt"
    val inputs = "d11.txt"
    println(D11(sample).s1() == 1656)
    println(D11(inputs).s1() == 1719)
    println(D11(sample).s2() == 195)
    println(D11(inputs).s2() == 232)
}