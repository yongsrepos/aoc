import java.time.Instant
class D15(override val inputs: String) : D0 {
    override fun s1() = Graph(100).init(IoHelper.getLines(inputs)).getShortestDistance(0 to 0, 99 to 99)

    override fun s2(): Int{
        //Graph(100).init(IoHelper.getLines(inputs)).renewMap()
        return Graph(500).init(IoHelper.getLines(inputs)).getShortestDistance(0 to 0, 499 to 499)
    }
}


class Graph(private val width: Int) {
    private var nodes = mutableMapOf<Pair<Int, Int>, Int>()

    private fun getNeighbours(me: Pair<Int, Int>): List<Pair<Int, Int>> = listOf(
        me.first to me.second + 1,
        me.first to me.second - 1,
        me.first + 1 to me.second,
        me.first - 1 to me.second
    ).filter { it.first in 0 until width && it.second in 0 until width }

    fun init(lines: List<String>): Graph {
        lines.withIndex().forEach { (y, line) ->
            line.withIndex().forEach { (x, c) ->
                nodes[x to y] = c.toString().toInt()
            }
        }
        return this
    }

    fun renewMap(): Graph {
        val newMap = mutableMapOf<Pair<Int, Int>, Int>()
        nodes.forEach {
            newMap[it.key] = it.value
        }

        val maxX = 99

        for (x in (maxX + 1) until (maxX + 1) * 5) {
            for (y in 0..maxX) {
                val startValue = newMap[(x - (maxX + 1)) to (y)]!!
                val endValue = if (startValue == 9) {
                    1
                } else {
                    startValue + 1
                }
                newMap[x to y] = endValue
            }
        }

        for (x in 0 until (maxX + 1) * 5) {
            for (y in (maxX + 1) until (maxX + 1) * 5) {
                val startValue = newMap[x to (y - (maxX + 1))]!!
                val endValue = if (startValue == 9) {
                    1
                } else {
                    startValue + 1
                }
                newMap[x to y] = endValue
            }
        }
        nodes = newMap
        nodes.printMe()
        println(nodes.size)
        return this
    }

    fun getShortestDistance(src: Pair<Int, Int>, dst: Pair<Int, Int>): Int{
        val distToSrc = mutableMapOf<Pair<Int, Int>, Int>()
        val solvedDistToSrc = mutableMapOf(src to 0)

        var currentNode = src
        while (currentNode != dst) {
            val newNeighbours = getNeighbours(currentNode).filterNot { it in solvedDistToSrc }
            newNeighbours.forEach { neighbour ->
                val srcToCurrentNode = solvedDistToSrc[currentNode]!!
                val edge = nodes[neighbour]!!

                if (neighbour !in distToSrc) {
                    distToSrc[neighbour] = edge + srcToCurrentNode
                } else {
                    val srcToNeighbour = distToSrc[neighbour]!!
                    if (srcToNeighbour > srcToCurrentNode + edge) {
                        distToSrc[neighbour] = srcToCurrentNode + edge
                    }
                }
            }
            currentNode = distToSrc.minBy{ it.value }!!.key
            solvedDistToSrc[currentNode] = distToSrc[currentNode]!!
            distToSrc.remove(currentNode)
        }
        return solvedDistToSrc[dst]!!
    }
}

fun Map<Pair<Int, Int>, Int>.printMe(){
    val maxX = this.map { it.key.first }.max()!!
    val maxY = this.map { it.key.second }.max()!!

    (0..maxY).forEach { y ->
        (0..maxX).forEach { x ->
            if (x to y in this) {
                print(this[x to y])
            } else {
                print(" ")
            }
        }
        print("\n")
    }
}



fun main() {
    //println(D15("d15sample.txt").s1())
    val now = Instant.now()
    println(D15("d15.txt").s1())
    //println((Instant.now().toEpochMilli()  - now.toEpochMilli())/(1000))
    println(D15("d15p2.txt").s2())
}