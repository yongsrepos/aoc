import utils.IoHelper

class D20 {
    fun getSolution1(): Long {
        val idToPossibleBoarders = getInputs().map { it.key to it.value.getPossibleBoardersNumbers() }.toMap()
        val idOfCornerTiles = getTileIds(idToPossibleBoarders, 2)
        var result = 1L
        for (key in idOfCornerTiles) {
            result *= key
            println(key)
        }
        return result
    }

    fun getSolution2(): Int {
        val monsterHashes = 15
        val N = 12
        val matrix = MutableList(N) { MutableList(N) { 0 } }
        val orignalIdToTile = getInputs().toMutableMap()

        val idToTileCopy = getInputs().toMutableMap()
        val resolvedIds = mutableListOf<Int>()
        val idToPossibleBoarders =
            idToTileCopy.map { it.key to it.value.getPossibleBoardersNumbers() }.toMap().toMutableMap()
        var neighbors = toNeigbors(idToTileCopy)
        val cornerTileIds = getTileIds(idToPossibleBoarders, 2)

        val randomCornerTileId = cornerTileIds[0]
        val neighborsOfCorner = neighbors[randomCornerTileId]!!.toList()
        if (neighborsOfCorner.size != 2) {
            throw IllegalStateException("corner $randomCornerTileId doesn't have 2 neighbours")
        }
        val cornerTile = idToTileCopy[randomCornerTileId]!!
        val stateAsTopRightCorner =
            cornerTile.getOneStateAsTopLeftCorner(
                idToTileCopy[neighborsOfCorner[0]]!!,
                idToTileCopy[neighborsOfCorner[1]]!!
            )
        cornerTile.adjustTo(stateAsTopRightCorner)
        orignalIdToTile[cornerTile.id] = cornerTile
        resolvedIds.add(cornerTile.id)

        var startTile = cornerTile
        var idsInRow = listOf<Int>()
        for (row in 0..(N - 2)) {
            println("row $row")
            startTile.printTile()
            idsInRow = resolvRow(startTile, N, neighbors, idToTileCopy, row, matrix, orignalIdToTile)

            if (row == (N - 2)) {
                break
            }

            var neighborsOfCurrentTile = neighbors[startTile.id]!!
            val bottomNum = toLong(startTile.getBottom())
            val bottomNeighborId =
                neighborsOfCurrentTile.find { idToTileCopy[it]!!.getPossibleBoardersNumbers().contains(bottomNum) }!!
            val remainingNeighbors =
                neighbors[bottomNeighborId]!!.filter { it != startTile.id }.map { idToTileCopy[it]!! }
            val bottomNeighbor = idToTileCopy[bottomNeighborId]!!
            bottomNeighbor.adjustMissingLeft(bottomNum, remainingNeighbors[0], remainingNeighbors[1])
            orignalIdToTile[bottomNeighbor.id] = bottomNeighbor
            startTile = bottomNeighbor
            idsInRow.forEach { idToTileCopy.remove(it) }
            neighbors = toNeigbors(idToTileCopy)
        }

        println("row $N")

        var neighborsOfCurrentTile = neighbors[startTile.id]!!
        val bottomNum = toLong(startTile.getBottom())
        val bottomNeighborId =
            neighborsOfCurrentTile.find { idToTileCopy[it]!!.getPossibleBoardersNumbers().contains(bottomNum) }!!
        val remainingNeighbors =
            neighbors[bottomNeighborId]!!.filter { it != startTile.id }.map { idToTileCopy[it]!! }
        val bottomNeighbor = idToTileCopy[bottomNeighborId]!!
        bottomNeighbor.adjustOnBottomLeft(bottomNum, remainingNeighbors[0])
        orignalIdToTile[bottomNeighbor.id] = bottomNeighbor
        startTile = bottomNeighbor
        val lastNeededNeighbor = idToTileCopy[matrix[N - 2][N - 1]]!!
        idsInRow.forEach { idToTileCopy.remove(it) }
        neighbors = toNeigbors(idToTileCopy)
        resolvLastRow(startTile, N, neighbors, idToTileCopy, lastNeededNeighbor, N - 1, matrix, orignalIdToTile)

        matrix.forEach { println(it) }

        // add together
        println("final")
        val jointPixels = matrix.flatMap { rows ->
            concatenateRows(rows.map { orignalIdToTile[it]!! }.map { it.removeBoarders() })
        }

        val image = Tile(666, jointPixels)
        image.printTile()
        for (i in 1..4) {
            image.rotateClockwiseOnce()
            val checkMonster = image.checkMonster()
            if (checkMonster.isNotEmpty()) {
                val result = image.getMarkCount('#').subtract(checkMonster).size
                println("Gocha! Count=$result")
                //image.printTile(checkMonster)
            }
        }
        image.flip()
        val checkMonster = image.checkMonster()
        if (checkMonster.isNotEmpty()) {
            val result = image.getMarkCount('#').subtract(checkMonster).size
            println("Gocha! flip. Count=$result")
            //image.printTile(checkMonster)
        }

        for (i in 1..4) {
            image.rotateClockwiseOnce()
            val checkMonster = image.checkMonster()
            if (checkMonster.isNotEmpty()) {
                val result = image.getMarkCount('#').subtract(checkMonster).size
                println("Gocha! after flip. Count=$result")
                //image.printTile(checkMonster)
                break
            }
        }

        return 0
    }

    private fun resolvRow(
        startTile: Tile,
        N: Int,
        neighbors: MutableMap<Int, MutableSet<Int>>,
        idToTile: Map<Int, Tile>,
        row: Int,
        maxtrix: MutableList<MutableList<Int>>,
        orignalIdToTile: MutableMap<Int, Tile>
    ): List<Int> {
        val resolvedIdsThisRow = mutableListOf(startTile.id)
        maxtrix[row][0] = startTile.id
        var currentTile = startTile

        for (i in 1..(N - 2)) {
            var neighborsOfCurrentTile = neighbors[currentTile.id]!!
            val rightNum = toLong(currentTile.getRight())
            val rightNeighborId =
                neighborsOfCurrentTile.find { idToTile[it]!!.getPossibleBoardersNumbers().contains(rightNum) }!!
            val remainingNeighbors = neighbors[rightNeighborId]!!.filter { it != currentTile.id }.map { idToTile[it]!! }
            val rightNeighbor = idToTile[rightNeighborId]!!
            rightNeighbor.adjustMissingTop(rightNum, remainingNeighbors[0], remainingNeighbors[1])
            orignalIdToTile[rightNeighborId] = rightNeighbor
            resolvedIdsThisRow.add(rightNeighborId)
            currentTile = rightNeighbor
            rightNeighbor.printTile()
            maxtrix[row][i] = currentTile.id
        }

        var neighborsOfCurrentTile = neighbors[currentTile.id]!!
        val rightNum = toLong(currentTile.getRight())
        val rightNeighborId =
            neighborsOfCurrentTile.find { idToTile[it]!!.getPossibleBoardersNumbers().contains(rightNum) }!!
        val remainingNeighbors = neighbors[rightNeighborId]!!.filter { it != currentTile.id }.map { idToTile[it]!! }
        val rightNeighbor = idToTile[rightNeighborId]!!
        rightNeighbor.adjustTopRight(rightNum, remainingNeighbors[0])
        orignalIdToTile[rightNeighborId] = rightNeighbor
        resolvedIdsThisRow.add(rightNeighborId)
        currentTile = rightNeighbor
        rightNeighbor.printTile()
        maxtrix[row][N - 1] = currentTile.id

        return resolvedIdsThisRow
    }

    private fun resolvLastRow(
        startTile: Tile,
        N: Int,
        neighbors: MutableMap<Int, MutableSet<Int>>,
        idToTile: Map<Int, Tile>,
        lastNeededNeighbor: Tile,
        row: Int,
        maxtrix: MutableList<MutableList<Int>>,
        orignalIdToTile: MutableMap<Int, Tile>
    ): List<Int> {
        val resolvedIdsThisRow = mutableListOf(startTile.id)
        maxtrix[row][0] = startTile.id
        var currentTile = startTile

        for (i in 1..(N - 2)) {
            var neighborsOfCurrentTile = neighbors[currentTile.id]!!
            val rightNum = toLong(currentTile.getRight())
            val rightNeighborId =
                neighborsOfCurrentTile.find { idToTile[it]!!.getPossibleBoardersNumbers().contains(rightNum) }!!
            val remainingNeighbors = neighbors[rightNeighborId]!!.filter { it != currentTile.id }.map { idToTile[it]!! }
            val rightNeighbor = idToTile[rightNeighborId]!!
            rightNeighbor.adjustLastRow(rightNum, remainingNeighbors[0])
            orignalIdToTile[rightNeighborId] = rightNeighbor
            resolvedIdsThisRow.add(rightNeighborId)
            currentTile = rightNeighbor
            rightNeighbor.printTile()
            maxtrix[row][i] = currentTile.id
        }

        var neighborsOfCurrentTile = neighbors[currentTile.id]!!
        val rightNum = toLong(currentTile.getRight())
        val rightNeighborId =
            neighborsOfCurrentTile.find { idToTile[it]!!.getPossibleBoardersNumbers().contains(rightNum) }!!
        val rightNeighbor = idToTile[rightNeighborId]!!
        rightNeighbor.adjustBottomRight(rightNum, lastNeededNeighbor)
        orignalIdToTile[rightNeighborId] = rightNeighbor
        resolvedIdsThisRow.add(rightNeighborId)
        currentTile = rightNeighbor
        rightNeighbor.printTile()
        maxtrix[N - 1][N - 1] = currentTile.id

        return resolvedIdsThisRow
    }

    private fun toNeigbors(idToTile: Map<Int, Tile>): MutableMap<Int, MutableSet<Int>> {
        val neighbors = mutableMapOf<Int, MutableSet<Int>>()
        val ids = idToTile.keys.toList()
        for (i in 0 until ids.size - 1)
            for (j in 1 until ids.size) {
                if (idToTile[ids[i]]!!.isNeighbor(idToTile[ids[j]]!!)) {
                    if (ids[i] !in neighbors) {
                        neighbors[ids[i]] = mutableSetOf(ids[j])
                    } else {
                        neighbors[ids[i]]!!.add(ids[j])
                    }

                    if (ids[j] !in neighbors) {
                        neighbors[ids[j]] = mutableSetOf(ids[i])
                    } else {
                        neighbors[ids[j]]!!.add(ids[i])
                    }
                }
            }

        return neighbors
    }

    private fun getTileIds(idToPossibleBoarders: Map<Int, List<Long>>, numOfBoardersToExclude: Int) =
        idToPossibleBoarders.filter { tileEntry ->
            tileEntry.value.count { borderNum ->
                idToPossibleBoarders.values.none {
                    it.contains(borderNum) && it != tileEntry.value
                }
            } == numOfBoardersToExclude * 2
        }.map { it.key }


    private fun getInputs(): Map<Int, Tile> {
        return IoHelper().getSections("d20.in", "\n\n").map { parseTile(it) }.map { it.id to it }.toMap()
    }

    private fun parseTile(tileBlock: String): Tile {
        val lines = tileBlock.lines()
        val id = lines.first().trim().drop(5).dropLast(1).toInt()
        val pixels = lines.drop(1).map { it.trim().toCharArray() }
        return Tile(id, pixels)
    }
}

data class Tile(val id: Int, var pixels: List<CharArray>) {
    var flipped = false
    var rotations = 0

    fun flip() {
        flipped = !flipped
        pixels = pixels.reversed()
    }

    fun rotateClockwiseOnce(): Tile {
        rotations += 1
        val squareSize = pixels[0].size
        val rotated = List(squareSize) { CharArray(squareSize) }
        for (col in pixels[0].indices) {
            for (row in pixels.indices.reversed()) {
                rotated[col][squareSize - row - 1] = pixels[row][col]
            }
        }
        pixels = rotated
        return this
    }

    fun copyOf(): Tile {
        return Tile(id, pixels.map { it.copyOf() })
    }

    fun getBoarders(): List<CharArray> {
        return listOf(getTop(), getRight(), getBottom(), getLeft())
    }

    fun getTop(): CharArray = pixels.first()

    fun getBottom(): CharArray = pixels.last()

    fun getRight(): CharArray = pixels.indices.reversed().map { pixels[it].last() }.toCharArray()

    fun getLeft(): CharArray = pixels.indices.reversed().map { pixels[it].first() }.toCharArray()

    fun getBoardersAsNumbers(): List<Long> {
        return getBoarders().map { toLong(it) }
    }

    fun isNeighbor(other: Tile): Boolean {
        return this.getPossibleBoardersNumbers().intersect(other.getPossibleBoardersNumbers()).size == 2
    }

    fun getPossibleBoardersNumbers(): List<Long> {
        return getBoarders().flatMap { listOf(toLong(it), toLong(it.copyOf().reversedArray())) }
    }

    fun adjustTo(state: List<Long>) {
        for (i in 1..4) {
            if (rotateClockwiseOnce().getBoardersAsNumbers() == state) {
                return
            }
        }
        flip()
        if (getBoardersAsNumbers() == state) {
            return
        }
        for (i in 1..4) {
            if (rotateClockwiseOnce().getBoardersAsNumbers() == state) {
                return
            }
        }

        throw IllegalStateException("Not adjusted to state=$state")
    }

    fun removeBoarders(): List<CharArray> {
        return this.pixels.toMutableList().drop(1).dropLast(1).map { it.drop(1).dropLast(1).toCharArray() }
    }

    fun getValidStateAtTopEdge(myLeftNum: Long, neighborB: Tile, neighborC: Tile): List<List<Long>> {
        val r = mutableListOf<List<Long>>()
        val copy = copyOf()
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnTopEdge(copy, myLeftNum, neighborB, neighborC)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        copy.flip()
        if (isValidOnTopEdge(copy, myLeftNum, neighborB, neighborC)) {
            val element = copy.getBoardersAsNumbers()
            if (element !in r) {
                r.add(element)
            }
        }
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnTopEdge(copy, myLeftNum, neighborB, neighborC)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        return r
    }

    fun isValidStateCorner(me: Tile, neighborA: Tile, neighborB: Tile): Boolean {
        val intersectA = me.getPossibleBoardersNumbers().intersect(neighborA.getPossibleBoardersNumbers())
        val intersectB = me.getPossibleBoardersNumbers().intersect(neighborB.getPossibleBoardersNumbers())

        val isTopRight = (toLong(me.getLeft()) in intersectA && toLong(me.getBottom()) in intersectB)
                || (toLong(me.getLeft()) in intersectB && toLong(me.getBottom()) in intersectA)
        val isTopLeft = (toLong(me.getRight()) in intersectA && toLong(me.getRight()) in intersectB)
                || (toLong(me.getRight()) in intersectB && toLong(me.getRight()) in intersectA)
        val isBottomLeft = (toLong(me.getTop()) in intersectA && toLong(me.getRight()) in intersectB)
                || (toLong(me.getTop()) in intersectB && toLong(me.getRight()) in intersectA)
        val isBottomRight = (toLong(me.getTop()) in intersectA && toLong(me.getLeft()) in intersectB)
                || (toLong(me.getTop()) in intersectB && toLong(me.getLeft()) in intersectA)

        return isTopLeft || isTopRight || isBottomLeft || isBottomRight
    }

    fun getOneStateAsTopLeftCorner(neighborA: Tile, neighborB: Tile): List<Long> {
        return getValidStateAsTopLeftCorner(neighborA, neighborB)[1]
    }

    fun getValidStateAsTopLeftCorner(neighborA: Tile, neighborB: Tile): List<List<Long>> {
        val r = mutableListOf<List<Long>>()
        val copy = copyOf()
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isAtTopLeftCorner(copy, neighborA, neighborB)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        copy.flip()
        if (isAtTopLeftCorner(copy, neighborA, neighborB)) {
            val element = copy.getBoardersAsNumbers()
            if (element !in r) {
                r.add(element)
            }
        }
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isAtTopLeftCorner(copy, neighborA, neighborB)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        return r
    }

    fun isAtTopLeftCorner(me: Tile, neighborA: Tile, neighborB: Tile): Boolean {
        val intersectA = me.getPossibleBoardersNumbers().intersect(neighborA.getPossibleBoardersNumbers())
        val intersectB = me.getPossibleBoardersNumbers().intersect(neighborB.getPossibleBoardersNumbers())

        return (toLong(me.getRight()) in intersectA && toLong(me.getBottom()) in intersectB)
                || (toLong(me.getRight()) in intersectB && toLong(me.getBottom()) in intersectA)
    }

    fun isValidOnTopEdge(me: Tile, leftNum: Long, neighborB: Tile, neighborC: Tile): Boolean {
        val intersectB = me.getPossibleBoardersNumbers().intersect(neighborB.getPossibleBoardersNumbers())
        val intersectC = me.getPossibleBoardersNumbers().intersect(neighborC.getPossibleBoardersNumbers())

        val myLeft = toLong(me.getLeft())
        val myBottom = toLong(me.getBottom())
        val myRight = toLong(me.getRight())
        return myLeft == leftNum && ((myBottom in intersectB && myRight in intersectC) || (myBottom in intersectC && myRight in intersectB))
    }

    fun checkMonster(): Set<Pair<Int, Int>> {
        val r = mutableSetOf<Pair<Int, Int>>()
        for (row in 0..(pixels.size - 3)) {
            for (i in 0 until (pixels[row].size - 20)) {
                val checkPoints = listOf(
                    row to i + 18,
                    row + 1 to i,
                    row + 1 to i + 5,
                    row + 1 to i + 6,
                    row + 1 to i + 11,
                    row + 1 to i + 12,
                    row + 1 to i + 17,
                    row + 1 to i + 18,
                    row + 1 to i + 19,
                    row + 2 to i + 1,
                    row + 2 to i + 4,
                    row + 2 to i + 7,
                    row + 2 to i + 10,
                    row + 2 to i + 13,
                    row + 2 to i + 16,
                )
                if (checkPoints.all { pixels[it.first][it.second] == '#' }
                ) {
                    r.addAll(checkPoints)
                }
            }

            for (i in 0 until (pixels[row].size - 20)) {
                val checkPoints = listOf(
                    row + 2 to i + 18,
                    row + 1 to i,
                    row + 1 to i + 5,
                    row + 1 to i + 6,
                    row + 1 to i + 11,
                    row + 1 to i + 12,
                    row + 1 to i + 17,
                    row + 1 to i + 18,
                    row + 1 to i + 19,
                    row to i + 1,
                    row to i + 4,
                    row to i + 7,
                    row to i + 10,
                    row to i + 13,
                    row to i + 16,
                )
                if (checkPoints.all { pixels[it.first][it.second] == '#' }
                ) {
                    r.addAll(checkPoints)
                }
            }

            for (i in 0 until (pixels[row].size - 20)) {
                val checkPoints = listOf(
                    row to i + 1,
                    row + 1 to i,
                    row + 1 to i + 1,
                    row + 1 to i + 2,
                    row + 1 to i + 7,
                    row + 1 to i + 8,
                    row + 1 to i + 13,
                    row + 1 to i + 14,
                    row + 1 to i + 19,
                    row + 2 to i + 3,
                    row + 2 to i + 6,
                    row + 2 to i + 9,
                    row + 2 to i + 12,
                    row + 2 to i + 15,
                    row + 2 to i + 18,
                )
                if (checkPoints.all { pixels[it.first][it.second] == '#' }
                ) {
                    r.addAll(checkPoints)
                }
            }

            for (i in 0 until (pixels[row].size - 20)) {
                val checkPoints = listOf(
                    row + 2 to i + 1,
                    row + 1 to i,
                    row + 1 to i + 1,
                    row + 1 to i + 2,
                    row + 1 to i + 7,
                    row + 1 to i + 8,
                    row + 1 to i + 13,
                    row + 1 to i + 14,
                    row + 1 to i + 19,
                    row to i + 3,
                    row to i + 6,
                    row to i + 9,
                    row to i + 12,
                    row to i + 15,
                    row to i + 18,
                )
                if (checkPoints.all { pixels[it.first][it.second] == '#' }
                ) {
                    r.addAll(checkPoints)
                }
            }
        }

        for (row in 0..(pixels.size - 20)) {
            //..................#.
            //#....##....##....###
            //.#..#..#..#..#..#...

            for (i in 0 until (pixels[row].size - 20)) {
                val checkPoints = listOf(
                    row to i + 1,
                    row + 1 to i,
                    row + 1 to i + 1,
                    row + 2 to i + 1,
                    row + 3 to i + 2,
                    row + 6 to i + 2,
                    row + 7 to i + 1,
                    row + 8 to i + 1,
                    row + 9 to i + 2,
                    row + 12 to i + 2,
                    row + 13 to i + 1,
                    row + 14 to i + 1,
                    row + 15 to i + 2,
                    row + 18 to i + 2,
                    row + 19 to i + 1,
                )
                if (checkPoints.all { pixels[it.first][it.second] == '#' }
                ) {
                    r.addAll(checkPoints)
                }
            }

            for (i in 0 until (pixels[row].size - 20)) {
                val checkPoints = listOf(
                    row + 19 to i + 1,
                    row + 18 to i,
                    row + 18 to i + 1,
                    row + 17 to i + 1,
                    row + 16 to i + 2,
                    row + 13 to i + 2,
                    row + 12 to i + 1,
                    row + 11 to i + 1,
                    row + 10 to i + 2,
                    row + 7 to i + 2,
                    row + 6 to i + 1,
                    row + 5 to i + 1,
                    row + 4 to i + 2,
                    row + 2 to i + 2,
                    row to i + 1,
                )
                if (checkPoints.all { pixels[it.first][it.second] == '#' }
                ) {
                    r.addAll(checkPoints)
                }
            }

            for (i in 0 until (pixels[row].size - 20)) {
                val checkPoints = listOf(
                    row + 19 to i + 1,
                    row + 18 to i + 2,
                    row + 18 to i + 1,
                    row + 17 to i + 1,
                    row + 16 to i,
                    row + 13 to i,
                    row + 12 to i + 1,
                    row + 11 to i + 1,
                    row + 10 to i,
                    row + 7 to i,
                    row + 6 to i + 1,
                    row + 5 to i + 1,
                    row + 4 to i,
                    row + 2 to i,
                    row to i + 1,
                )
                if (checkPoints.all { pixels[it.first][it.second] == '#' }
                ) {
                    r.addAll(checkPoints)
                }
            }

            for (i in 0 until (pixels[row].size - 20)) {
                val checkPoints = listOf(
                    row to i + 1,
                    row + 1 to i + 2,
                    row + 1 to i + 1,
                    row + 2 to i + 1,
                    row + 3 to i,
                    row + 6 to i,
                    row + 7 to i + 1,
                    row + 8 to i + 1,
                    row + 9 to i,
                    row + 12 to i,
                    row + 13 to i + 1,
                    row + 14 to i + 1,
                    row + 15 to i,
                    row + 18 to i,
                    row + 19 to i + 1,
                )
                if (checkPoints.all { pixels[it.first][it.second] == '#' }
                ) {
                    r.addAll(checkPoints)
                }
            }
        }
        return r
    }

    fun getMarkCount(mark: Char): Set<Pair<Int, Int>> {
        return pixels.flatMapIndexed { row: Int, chars: CharArray ->
            chars.mapIndexed { col, c -> if (c == mark) row to col else null }.filterNotNull().toSet()
        }.toSet()
    }

    fun printTile() {
        println()
        println("Tile: $id")
        pixels.forEach { println(it) }
    }


    fun printTile(monsterdots: Set<Pair<Int, Int>>) {
        println("Tile: $id")
        pixels.forEachIndexed { row, chars ->
            chars.forEachIndexed { col, c -> if (row to col in monsterdots) print('0') else print(c) }
            println()
        }
        println()
    }

    fun printBoards() {
        println("Boarders: $id")
        getBoarders().forEach { println(it) }
    }

    fun adjustMissingTop(myLeftNum: Long, neighborB: Tile, neighborC: Tile) {
        val validStateAtTopEdge = getValidStateAtTopEdge(myLeftNum, neighborB, neighborC)
        if (validStateAtTopEdge.size != 1) {
            throw IllegalStateException("more than one state as at edge")
        }
        adjustTo(validStateAtTopEdge[0]!!)
    }

    fun adjustMissingLeft(myTopNum: Long, neighborA: Tile, neighborB: Tile) {
        val validStateAtTopEdge = getValidStateAtLeftEdge(myTopNum, neighborA, neighborB)
        if (validStateAtTopEdge.size != 1) {
            throw IllegalStateException("more than one state as at edge")
        }
        adjustTo(validStateAtTopEdge[0]!!)
    }

    private fun getValidStateAtLeftEdge(myTopNum: Long, neighborB: Tile, neighborC: Tile): List<List<Long>> {
        val r = mutableListOf<List<Long>>()
        val copy = copyOf()
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnLeftEdge(copy, myTopNum, neighborB, neighborC)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        copy.flip()
        if (isValidOnLeftEdge(copy, myTopNum, neighborB, neighborC)) {
            val element = copy.getBoardersAsNumbers()
            if (element !in r) {
                r.add(element)
            }
        }
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnLeftEdge(copy, myTopNum, neighborB, neighborC)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        return r
    }

    fun isValidOnLeftEdge(me: Tile, topNum: Long, neighborB: Tile, neighborC: Tile): Boolean {
        val intersectB = me.getPossibleBoardersNumbers().intersect(neighborB.getPossibleBoardersNumbers())
        val intersectC = me.getPossibleBoardersNumbers().intersect(neighborC.getPossibleBoardersNumbers())

        val myTop = toLong(me.getTop())
        val myBottom = toLong(me.getBottom())
        val myRight = toLong(me.getRight())
        return myTop == topNum && ((myBottom in intersectB && myRight in intersectC) || (myBottom in intersectC && myRight in intersectB))
    }

    fun adjustOnBottomLeft(myTopNum: Long, neighbor: Tile) {
        val validStateAtBottomLeft = getValidStateAtBottomLeft(myTopNum, neighbor)
        if (validStateAtBottomLeft.size != 1) {
            throw IllegalStateException("more than one state as at BottomLeft")
        }
        adjustTo(validStateAtBottomLeft[0]!!)
    }

    private fun getValidStateAtBottomLeft(myTopNum: Long, neighbor: Tile): List<List<Long>> {
        val r = mutableListOf<List<Long>>()
        val copy = copyOf()
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnBottomLeft(copy, myTopNum, neighbor)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        copy.flip()
        if (isValidOnBottomLeft(copy, myTopNum, neighbor)) {
            val element = copy.getBoardersAsNumbers()
            if (element !in r) {
                r.add(element)
            }
        }
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnBottomLeft(copy, myTopNum, neighbor)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        return r
    }

    fun isValidOnBottomLeft(me: Tile, topNum: Long, neighbor: Tile): Boolean {
        val intersect = me.getPossibleBoardersNumbers().intersect(neighbor.getPossibleBoardersNumbers())

        val myTop = toLong(me.getTop())
        val myRight = toLong(me.getRight())
        return myTop == topNum && myRight in intersect
    }

    fun adjustTopRight(myLeftNum: Long, neighbor: Tile) {
        val validStateAtTopRight = getValidStateAtTopRight(myLeftNum, neighbor)
        if (validStateAtTopRight.size != 1) {
            throw IllegalStateException("more than one state as at TopRight")
        }
        adjustTo(validStateAtTopRight[0]!!)
    }

    private fun getValidStateAtTopRight(myTopNum: Long, neighbor: Tile): List<List<Long>> {
        val r = mutableListOf<List<Long>>()
        val copy = copyOf()
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnTopRight(copy, myTopNum, neighbor)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        copy.flip()
        if (isValidOnTopRight(copy, myTopNum, neighbor)) {
            val element = copy.getBoardersAsNumbers()
            if (element !in r) {
                r.add(element)
            }
        }
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnTopRight(copy, myTopNum, neighbor)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        return r
    }

    fun isValidOnTopRight(me: Tile, leftNum: Long, neighbor: Tile): Boolean {
        val intersect = me.getPossibleBoardersNumbers().intersect(neighbor.getPossibleBoardersNumbers())

        val myLeft = toLong(me.getLeft())
        val myBottum = toLong(me.getBottom())
        return myLeft == leftNum && myBottum in intersect
    }

    fun adjustLastRow(myLeftNum: Long, neighbor: Tile) {
        val validStateAtLastRow = getValidStateAtLastRow(myLeftNum, neighbor)
        if (validStateAtLastRow.size != 1) {
            throw IllegalStateException("more than one state as at LastRow")
        }
        adjustTo(validStateAtLastRow[0]!!)
    }

    private fun getValidStateAtLastRow(myLeftNum: Long, neighbor: Tile): List<List<Long>> {
        val r = mutableListOf<List<Long>>()
        val copy = copyOf()
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnLastRow(copy, myLeftNum, neighbor)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        copy.flip()
        if (isValidOnLastRow(copy, myLeftNum, neighbor)) {
            val element = copy.getBoardersAsNumbers()
            if (element !in r) {
                r.add(element)
            }
        }
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnLastRow(copy, myLeftNum, neighbor)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        return r
    }

    fun isValidOnLastRow(me: Tile, leftNum: Long, neighbor: Tile): Boolean {
        val intersect = me.getPossibleBoardersNumbers().intersect(neighbor.getPossibleBoardersNumbers())

        val myLeft = toLong(me.getLeft())
        val myRight = toLong(me.getRight())
        return myLeft == leftNum && myRight in intersect
    }

    fun adjustBottomRight(myLeftNum: Long, neighbor: Tile) {
        val validStateAtBottomRight = getValidStateAtBottomRight(myLeftNum, neighbor)
        if (validStateAtBottomRight.size != 1) {
            throw IllegalStateException("more than one state as at BottomRight")
        }
        adjustTo(validStateAtBottomRight[0]!!)
    }

    private fun getValidStateAtBottomRight(myLeftNum: Long, neighbor: Tile): List<List<Long>> {
        val r = mutableListOf<List<Long>>()
        val copy = copyOf()
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnBottomRight(copy, myLeftNum, neighbor)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        copy.flip()
        if (isValidOnBottomRight(copy, myLeftNum, neighbor)) {
            val element = copy.getBoardersAsNumbers()
            if (element !in r) {
                r.add(element)
            }
        }
        repeat(4) {
            copy.rotateClockwiseOnce()
            if (isValidOnBottomRight(copy, myLeftNum, neighbor)) {
                val element = copy.getBoardersAsNumbers()
                if (element !in r) {
                    r.add(element)
                }
            }
        }
        return r
    }

    fun isValidOnBottomRight(me: Tile, leftNum: Long, neighbor: Tile): Boolean {
        val intersect = me.getPossibleBoardersNumbers().intersect(neighbor.getPossibleBoardersNumbers())

        val myLeft = toLong(me.getLeft())
        val myTop = toLong(me.getTop())
        return myLeft == leftNum && myTop in intersect
    }
}

fun concatenateRows(tilesPixels: List<List<CharArray>>): List<CharArray> {
    return tilesPixels.reduce { acc, list -> acc.mapIndexed { index, chars -> chars.plus(list[index]) } }
}

fun toLong(charArray: CharArray): Long {
    return charArray.copyOf().joinToString("").replace('.', '0').replace('#', '1').toLong(2)
}


fun main() {
    D20().getSolution2()
}
