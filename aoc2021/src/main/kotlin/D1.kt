object D1 {
    fun getInputs() = IoHelper.getInts("d01.txt")
    fun solveOne(): Int = getInputs().zipWithNext().count { it.second > it.first }
    fun solveTwo(): Int = getInputs().windowed(size = 3).map { it.sum() }.zipWithNext().count { it.second > it.first }
}

fun main() {
    val solutionOne = D1.solveOne()
    val solutionTwo = D1.solveTwo()
    println("One=$solutionOne")
    println("Tw0=$solutionTwo")
}