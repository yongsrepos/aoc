import utils.IoHelper

class D24 {
    fun getSolution1(): Int {
        return getInitFloor().count { it.value.color == Color.B }
    }

    fun getSolution2(): Int {
        var floor = getInitFloor()

        var r = 1
        repeat(100) {
            var floorWithTilesToCheck = mutableMapOf<Pair<Int, Int>, TileD24>()
            for (posAndTile in floor) {
                floorWithTilesToCheck[posAndTile.key] = posAndTile.value
                posAndTile.value.getNeighborsPositions().forEach {
                    if (it !in floor) {
                        floorWithTilesToCheck[it] = TileD24(it.first, it.second)
                    }
                }
            }

            var floorSnapshot =
                floorWithTilesToCheck.map { it.key to TileD24(it.value.x, it.value.y, it.value.color) }.toMap()
            for (tileToCheck in floorWithTilesToCheck.values) {
                if (tileToCheck.shouldFlip(floorSnapshot)) {
                    tileToCheck.flip()
                }
            }

            floor = floorWithTilesToCheck
            r = floorWithTilesToCheck.count { it.value.color == Color.B }
        }

        return r
    }

    private fun getInitFloor(): MutableMap<Pair<Int, Int>, TileD24> {
        val posToTile = mutableMapOf<Pair<Int, Int>, TileD24>()

        getInputs().map { parse(it) }.forEach { directions ->
            val tile = TileD24()
            for (direction in directions) {
                tile.move(direction)
            }
            val pos = tile.x to tile.y
            if (pos in posToTile) {
                posToTile[pos]!!.flip()
            } else {
                tile.flip()
                posToTile[pos] = tile
            }
        }
        return posToTile
    }

    private fun parse(moves: String): List<DirectionD24> {
        var copy = moves
        var directions = mutableListOf<DirectionD24>()

        while (copy.isNotEmpty()) {
            val first = copy.first().toString()
            copy = if (first in listOf(DirectionD24.E.name.toLowerCase(), DirectionD24.W.name.toLowerCase())) {
                directions.add(DirectionD24.valueOf(first.toUpperCase()))
                copy.drop(1)
            } else {
                directions.add(DirectionD24.valueOf(copy.slice(0..1).toUpperCase()))
                copy.drop(2)
            }
        }

        return directions
    }

    private fun getInputs(): List<String> {
        return IoHelper().getLines("d24.in")
    }
}

data class TileD24(var x: Int = 0, var y: Int = 0, var color: Color = Color.W) {
    fun move(direction: DirectionD24): TileD24 {
        when (direction) {
            DirectionD24.E -> x += 2
            DirectionD24.NE -> {
                x += 1
                y += 2
            }
            DirectionD24.SE -> {
                x += 1
                y -= 2
            }
            DirectionD24.W -> {
                x -= 2
            }
            DirectionD24.NW -> {
                x -= 1
                y += 2
            }
            DirectionD24.SW -> {
                x -= 1
                y -= 2
            }
        }

        return this
    }

    fun flip() {
        color = if (color == Color.W) {
            Color.B
        } else {
            Color.W
        }
    }

    fun shouldFlip(floor: Map<Pair<Int, Int>, TileD24>): Boolean {
        val blackNeighbors = getNeighborsPositions().count { it in floor && floor[it]!!.color == Color.B }
        return if (color == Color.B) blackNeighbors == 0 || blackNeighbors > 2 else blackNeighbors == 2
    }

    fun getNeighborsPositions(): List<Pair<Int, Int>> {
        return DirectionD24.values().map { TileD24(x, y, color).move(it).getPost() }
    }

    private fun getPost(): Pair<Int, Int> {
        return x to y
    }

}

enum class DirectionD24 {
    E, SE, NE, W, SW, NW
}

enum class Color {
    B, W
}
