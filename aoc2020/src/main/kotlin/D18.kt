import utils.IoHelper

class D18 {
    fun getSolution1(): Long {
        return IoHelper().getLines("d18.in").map { valuate(it) }.sum()
    }

    fun getSolution2(): Long {
        return IoHelper().getLines("d18.in").map { valuate2(it) }.sum()
    }

    private fun valuate(expression: String): Long {
        val operands = ArrayDeque<Long>()
        val operators = ArrayDeque<Char>()
        expression.forEach {
            if (it in listOf('+', '*', '(')) {
                operators.addLast(it)
            } else if (it == ')') {
                operators.removeLast()//remove the latest '('
                if (!operators.isEmpty() && operators.last() in listOf('+', '*')) {
                    val operator = operators.removeLast()
                    val operandA = operands.removeLast()
                    val operandB = operands.removeLast()
                    when (operator) {
                        '+' -> operands.addLast(operandA + operandB)
                        '*' -> operands.addLast(operandA * operandB)
                    }
                }
            } else if (it != ' ') {
                if (operators.isEmpty() || operators.last() == '(') {
                    operands.addLast(it.toString().toLong())
                } else {
                    val operator = operators.removeLast()
                    val operand = operands.removeLast()
                    when (operator) {
                        '+' -> operands.addLast(operand + it.toString().toLong())
                        '*' -> operands.addLast(operand * it.toString().toLong())
                    }
                }
            }
        }
        return operands.last()
    }

    private fun valuate2(expression: String): Long {
        val operands = ArrayDeque<Long>()
        val operators = ArrayDeque<Char>()
        expression.forEach {
            if (it in listOf('+', '*', '(')) {
                operators.addLast(it)
            } else if (it == ')') {
                if (operators.last() == '*') {
                    var tmp = 1L
                    while (operators.last() == '*') {
                        tmp *= operands.removeLast()
                        operators.removeLast()
                    }
                    tmp *= operands.removeLast()
                    operands.addLast(tmp)
                }
                operators.removeLast()//remove the latest '('
                if (!operators.isEmpty() && operators.last() == '+') {
                    operators.removeLast()
                    val operandA = operands.removeLast()
                    val operandB = operands.removeLast()
                    operands.addLast(operandA + operandB)
                }
            } else if (it != ' ') {
                if (operators.isEmpty() || operators.last() == '(') {
                    operands.addLast(it.toString().toLong())
                } else {
                    if (!operators.isEmpty() && operators.last() == '+') {
                        operators.removeLast()
                        operands.addLast(operands.removeLast() + it.toString().toLong())
                    } else {
                        operands.addLast(it.toString().toLong())
                    }
                }
            }
        }

        return operands.reduce { acc, l -> acc * l }
    }
}
