import utils.IoHelper

class D17 {
    private var cubesToCheck = mutableSetOf<HasNeighbors>()
    private var activeCubes = mutableSetOf<HasNeighbors>()

    fun getSolution1(): Int {
        return getSolution(3)
    }

    fun getSolution2(): Int {
        return getSolution(4)
    }

    private fun getSolution(qubeDimension: Int): Int {
        init(qubeDimension)
        repeat(6) { update() }
        return activeCubes.size
    }

    private fun update() {
        var newlyActivated = mutableSetOf<HasNeighbors>()
        var newlyDeactivated = mutableSetOf<HasNeighbors>()
        var updatedTotalCubes = mutableSetOf<HasNeighbors>()
        for (cube in cubesToCheck) {
            updatedTotalCubes.addAll(cube.getNeighbors())
            if (cube in activeCubes) {
                if (shouldDeactivate(cube)) {
                    newlyDeactivated.add(cube)
                }
            } else if (shouldActivate(cube)) {
                newlyActivated.add(cube)
            }
        }
        activeCubes = activeCubes.union(newlyActivated).subtract(newlyDeactivated).toMutableSet()
        cubesToCheck = cubesToCheck.union(updatedTotalCubes).toMutableSet()
    }

    private fun shouldActivate(me: HasNeighbors): Boolean {
        return me.getNeighbors().count { it in activeCubes } == 3
    }

    private fun shouldDeactivate(me: HasNeighbors): Boolean {
        return me.getNeighbors().count { it in activeCubes } !in 2..3
    }

    private fun init(qubeDimension: Int) {
        IoHelper().getLines("d17.in").forEachIndexed { rowIdx, line ->
            line.forEachIndexed { colIdx, cubeMark ->
                run {
                    val cube = if (qubeDimension == 3) TripleQube(rowIdx, colIdx, 0)
                    else QuadQube(rowIdx, colIdx, 0, 0)
                    cubesToCheck.add(cube)
                    cubesToCheck.addAll(cube.getNeighbors())
                    if (cubeMark == '#') {
                        activeCubes.add(cube)
                    }
                }
            }
        }
    }
}

interface HasNeighbors {
    fun getNeighbors(): Set<HasNeighbors>
}

data class TripleQube(private val x: Int, private val y: Int, private val z: Int) : HasNeighbors {
    override fun getNeighbors(): Set<HasNeighbors> {
        val result = mutableSetOf<TripleQube>()
        for (i in -1..1) {
            for (j in -1..1) {
                for (k in -1..1) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue
                    }
                    result.add(TripleQube(this.x + i, this.y + j, this.z + k))
                }
            }
        }
        return result
    }
}

data class QuadQube(private val x: Int, private val y: Int, private val z: Int, private val w: Int) : HasNeighbors {
    override fun getNeighbors(): Set<HasNeighbors> {
        val result = mutableSetOf<QuadQube>()
        for (a in -1..1) {
            for (b in -1..1) {
                for (c in -1..1) {
                    for (d in -1..1) {
                        if (a == 0 && b == 0 && c == 0 && d == 0) {
                            continue
                        }
                        result.add(QuadQube(this.x + a, this.y + b, this.z + c, this.w + d))
                    }
                }
            }
        }
        return result.toSet()
    }
}
