import utils.IoHelper
import kotlin.math.abs

class D12 {
    fun getSolution1(): Int {
        var currentPos = Vector(0, 0, Direction.E)
        for (instruction in getInputs()) {
            val action = Action.valueOf(instruction[0].toString())
            currentPos = currentPos.move(action, instruction.drop(1).toInt(), TurnOrRotate.TURN)
        }

        return abs(currentPos.x) + abs(currentPos.y)
    }

    fun getSolution2(): Int {
        var ship = Vector(0, 0, Direction.E)
        var waypointRelative = Vector(10, 1, Direction.E)

        for (instruction in getInputs()) {
            val action = Action.valueOf(instruction[0].toString())
            val unit = instruction.drop(1).toInt()
            if (Action.F == action) {
                ship = ship.moveTowards(unit, waypointRelative)
            } else {
                waypointRelative = waypointRelative.move(action, unit, TurnOrRotate.ROTATE)
            }
        }

        return abs(ship.x) + abs(ship.y)
    }

    private fun getInputs(): List<String> {
        return IoHelper().getLines("d12.in")
    }
}

enum class Action { N, S, E, W, L, R, F }

enum class Direction { N, S, E, W }

enum class TurnOrRotate { TURN, ROTATE }

data class Vector(val x: Int, val y: Int, val direction: Direction) {
    fun move(action: Action, unit: Int, turnOrRotate: TurnOrRotate): Vector {
        return when (action) {
            Action.N -> Vector(x, y + unit, direction)
            Action.S -> Vector(x, y - unit, direction)
            Action.E -> Vector(x + unit, y, direction)
            Action.W -> Vector(x - unit, y, direction)
            Action.L, Action.R -> turnOrRotate(action, unit, turnOrRotate)
            Action.F -> moveForward(unit)
        }
    }

    fun moveTowards(unit: Int, waypointRelative: Vector): Vector {
        return Vector(x + unit * waypointRelative.x, y + unit * waypointRelative.y, direction)
    }

    fun moveForward(unit: Int): Vector {
        return when (direction) {
            Direction.N -> Vector(x, y + unit, direction)
            Direction.S -> Vector(x, y - unit, direction)
            Direction.E -> Vector(x + unit, y, direction)
            Direction.W -> Vector(x - unit, y, direction)
        }
    }

    fun turnOrRotate(leftOrRight: Action, degrees: Int, turnOrRotate: TurnOrRotate): Vector {
        val times = (degrees / 90) % 4
        if (times == 0) {
            return Vector(x, y, direction)
        }

        var result = turnOrRotateOneQuarter(leftOrRight, turnOrRotate)
        if (times != 1) {
            for (i in 2..times) {
                result = result.turnOrRotateOneQuarter(leftOrRight, turnOrRotate)
            }
        }

        return result
    }

    fun turnOrRotateOneQuarter(action: Action, turnOrRotate: TurnOrRotate): Vector {
        return if (turnOrRotate == TurnOrRotate.TURN) turnOneQuarter(action) else rotateOneQuater(action)
    }

    fun turnOneQuarter(action: Action): Vector {
        val newDirection = when (direction) {
            Direction.N -> if (action == Action.L) Direction.W else Direction.E
            Direction.S -> if (action == Action.L) Direction.E else Direction.W
            Direction.E -> if (action == Action.L) Direction.N else Direction.S
            Direction.W -> if (action == Action.L) Direction.S else Direction.N
        }
        return Vector(x, y, newDirection)
    }

    fun rotateOneQuater(action: Action): Vector {
        return if (action == Action.L) Vector(-y, x, direction) else Vector(y, -x, direction)
    }
}
