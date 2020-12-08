import utils.IoHelper

class D08 {
    private var accumulatedValue = 0

    fun getSolution1(): Int {
        runCommands(getInputs())
        return accumulatedValue
    }

    fun getSolution2(): Int {
        if (trySwitch(Ops.JMP, Ops.NOP)) {
            return accumulatedValue
        }
        trySwitch(Ops.NOP, Ops.JMP)
        return accumulatedValue
    }

    private fun trySwitch(from: Ops, to: Ops): Boolean {
        val commands = getInputs()
        val fromOnes = commands.filter { it.operator == from }
        for (candidateCmd in fromOnes) {
            commands.forEach { it.executed = false }
            accumulatedValue = 0
            candidateCmd.operator = to

            if (runCommands(commands)) {
                return true
            }
            candidateCmd.operator = from
        }
        return false
    }

    private fun runCommands(commands: List<Command>): Boolean {
        var commandPointer = 0
        var currentCommand = commands[commandPointer]
        while (!currentCommand.executed) {
            currentCommand.executed = true
            when (currentCommand.operator) {
                Ops.ACC -> {
                    accumulatedValue += currentCommand.operand
                    commandPointer += 1
                }
                Ops.JMP -> commandPointer += currentCommand.operand
                Ops.NOP -> commandPointer += 1
            }
            if (commandPointer == commands.size) {
                return true
            }
            currentCommand = commands[commandPointer]
        }

        return false
    }

    private fun getInputs(): List<Command> {
        return IoHelper().getLines("d08.in").map {
            val operator = Ops.valueOf(it.slice(0..2).toUpperCase())
            val operand = it.drop(4).toInt()
            Command(operator, operand)
        }
    }
}

enum class Ops { ACC, JMP, NOP }

data class Command(var operator: Ops, val operand: Int, var executed: Boolean = false)

