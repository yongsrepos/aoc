enum class Direction {
    FORWARD, DOWN, UP
}

object D2 {
    fun getInputs() = IoHelper.getLines("d02.txt")

    fun s1(): Int {
        var horizontal = 0
        var depth = 0
        getInputs().forEach {
            val splits = it.split(" ")
            when (splits[0]) {
                Direction.FORWARD.toString().toLowerCase() -> horizontal += splits[1].toInt()
                Direction.DOWN.toString().toLowerCase() -> depth += splits[1].toInt()
                Direction.UP.toString().toLowerCase() -> depth -= splits[1].toInt()
            }
        }
        return horizontal * depth
    }

    fun s2():Int {
        var horizontal = 0
        var depth = 0
        var aim = 0

        getInputs().forEach {
            val splits = it.split(" ")
            when (splits[0]) {
                Direction.FORWARD.toString().toLowerCase() -> {
                    horizontal += splits[1].toInt()
                    depth += aim * splits[1].toInt()
                }
                Direction.DOWN.toString().toLowerCase() -> aim += splits[1].toInt()
                Direction.UP.toString().toLowerCase() -> aim -= splits[1].toInt()
            }
        }
        return horizontal * depth

    }
}

fun main() {
    println(D2.s1())
    println(D2.s2())
}