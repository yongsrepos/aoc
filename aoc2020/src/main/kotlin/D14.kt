import utils.IoHelper
import kotlin.math.pow

class D14 {
    fun getSolution1(): Long {
        val memory = mutableMapOf<Int, Long>()
        var currentMask = ""
        for (cmd in getInputs()) {
            val split = cmd.split(" = ")
            if (cmd.contains("mask")) {
                currentMask = split[1]
                continue
            }

            var originalValue = split[1].toInt()
            val address = split[0].drop(4).dropLast(1).toInt()
            memory[address] = originalValue.applyMask(currentMask)
        }
        return memory.values.sum()
    }

    fun getSolution2(): Long {
        val memory = mutableMapOf<Long, Long>()
        var currentMask = ""
        for (cmd in getInputs()) {
            val split = cmd.split(" = ")
            if (cmd.contains("mask")) {
                currentMask = split[1]
                continue
            }

            var value = split[1].toLong()
            val originalAddress = split[0].drop(4).dropLast(1).toInt()
            originalAddress.applyMaskMode2(currentMask).forEach { memory[it] = value }
        }

        return memory.values.sum()
    }

    private fun getInputs(): List<String> {
        return IoHelper().getLines("d14.in")
    }
}

fun Int.applyMask(mask: String): Long {
    val binaryWithPadding = this.toString(2).padStart(mask.length, '0').toCharArray()
    mask.forEachIndexed { idx, cha -> if (cha != 'X') binaryWithPadding[idx] = mask[idx] }
    return binaryWithPadding.joinToString("").toLong(2)
}

fun Int.applyMaskMode2(mask: String): Set<Long> {
    val binaryWithPadding = this.toString(2).padStart(mask.length, '0').toCharArray()
    val floatingPositions = mutableListOf<Int>()
    mask.forEachIndexed { idx, cha ->
        when (cha) {
            '1' -> binaryWithPadding[idx] = mask[idx]
            'X' -> {
                binaryWithPadding[idx] = mask[idx]
                floatingPositions.add(idx)
            }
        }
    }
    val result = mutableSetOf<Long>()
    genSubsets(floatingPositions).forEach { floatingPositionChoiceSet ->
        run {
            val copyOf = binaryWithPadding.copyOf()
            floatingPositionChoiceSet.forEach { copyOf[it] = '1' }
            result.add(copyOf.joinToString("").replace('X', '0').toLong(2))
        }
    }

    return result.toSet()
}

fun genSubsets(sorted: List<Int>): MutableSet<MutableSet<Int>> {
    var subsets = mutableSetOf<MutableSet<Int>>()
    for (i in 0 until 2.0.pow(sorted.size).toInt()) {
        val binaryI = i.toString(2).padStart(sorted.size, '0')
        var subset = mutableSetOf<Int>()
        binaryI.forEachIndexed { idx, cha ->
            if (cha == '1') subset.add(sorted[sorted.size - (idx + 1)])
        }
        subsets.add(subset)
    }
    return subsets
}
