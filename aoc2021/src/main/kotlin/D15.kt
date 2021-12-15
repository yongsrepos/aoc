import java.time.Instant

class D15(override val inputs: String) : D0 {
    override fun s1() = Graph(100).init(IoHelper.getLines(inputs)).getShortestDistance(0 to 0, 99 to 99)

    override fun s2(): Int {
        TODO("Not yet implemented")
    }
}

class Graph(private val width: Int) {
    private val nodes = mutableMapOf<Pair<Int, Int>, Int>()

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

    fun getShortestDistance(src: Pair<Int, Int>, dst: Pair<Int, Int>): Int{
        val distToSrc = mutableMapOf<Pair<Int, Int>, Int>()
        val solvedDistToSrc = mutableMapOf(src to 0)
        val path = mutableListOf(src)

        var currentNode = src
        while (currentNode != dst) {
            val newNeighbours = getNeighbours(currentNode).filterNot { it in path }
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
            currentNode = distToSrc.filterNot { it.key in path }.minByOrNull { it.value }!!.key
            solvedDistToSrc[currentNode] = distToSrc[currentNode]!!
            distToSrc.remove(currentNode)
            path.add(currentNode)
        }
        println(solvedDistToSrc.size)
        return solvedDistToSrc[dst]!!
    }
}

fun main() {
    //println(D15("d15sample.txt").s1())
    val now = Instant.now()
    println(D15("d15.txt").s1())
    println((Instant.now().toEpochMilli()  - now.toEpochMilli())/(1000))
}