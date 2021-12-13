class D13(override val inputs: String) : D0 {
    override fun s1(): Int {
        val (lines, instructions) = parseInputs()
        val instruction = instructions[0].parseInstruction()
        return if (instruction.first.endsWith("y"))
            lines.foldingUp(instruction.second).countDots()
        else
            lines.foldingLeft(instruction.second).countDots()
    }

    override fun s2(): Int {
        var (lines, instructions) = parseInputs()
        instructions.forEach { str ->
            str.parseInstruction().let {
                lines = if (it.first.endsWith("y"))
                    lines.foldingUp(it.second)
                else
                    lines.foldingLeft(it.second)
            }
        }

        lines.printme()
        return 0
    }

    private fun parseInputs(): Pair<List<String>, List<String>> {
        val (dotsRaw, instructionsRaw) = IoHelper.getRawContent(inputs)
            ?.split(System.lineSeparator() + System.lineSeparator()) ?: emptyList()
        val dots =
            dotsRaw.split(System.lineSeparator()).map { dot -> dot.split(",").let { it[0].toInt() to it[1].toInt() } }
        val maxX = dots.maxOf { it.first }
        val maxY = dots.maxOf { it.second }
        val lines = (0..maxY).map { y ->
            (0..maxX).map { x ->
                if (x to y in dots) '#' else '.'
            }.joinToString("")
        }
        val instructions = instructionsRaw.split(System.lineSeparator())

        return lines to instructions
    }
}

fun String.folding(other: String) =
    this.toList().zip(other.toList()).map { if (it.first == '#' || it.second == '#') '#' else '.' }.joinToString("")

fun String.foldingAt(pos: Int) = this.substring(0 until pos).folding(this.substring(pos + 1).reversed())
fun List<String>.foldingUp(pos: Int) =
    this.subList(0, pos).withIndex().map { (idx, line) -> line.folding(this[this.size - 1 - idx]) }

fun List<String>.foldingLeft(pos: Int) = this.map { it.foldingAt(pos) }

fun List<String>.countDots() = this.sumBy { line -> line.count { it == '#' } }

fun String.parseInstruction() = this.split("=").let { it[0] to it[1].toInt() }

fun List<String>.printme(): List<String> {
    this.forEach { println(it) }
    return this
}

fun main() {
    println(D13("d13sample.txt").s1() == 17)
    println(D13("d13.txt").s1() == 704)
    println(D13("d13.txt").s2()) //HGAJBEHC
}