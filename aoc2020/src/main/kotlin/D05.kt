import utils.IoHelper

class D05 {
    fun getSolution1(): Int? {
        return getInputs().map { getSeatId(decodeSeat(it)) }.maxOrNull()
    }

    fun getSolution2(): Int {
        return getSeatId(getMySeat())
    }

    private fun getInputs(): List<String> {
        return IoHelper().getLines("d05.in")
    }

    private fun getMySeat(): Pair<Int, Int> {
        val seatToIdMap = getInputs().map { decodeSeat(it) to getSeatId(decodeSeat(it)) }.toMap()

        for (row in 1..126) {
            for (col in 0..7) {
                if (row to col !in seatToIdMap) {
                    val id = getSeatId(row to col)
                    if (id + 1 in seatToIdMap.values && id - 1 in seatToIdMap.values) {
                        return row to col
                    }
                }
            }
        }

        throw IllegalStateException("Not able to find my seat")
    }

    private fun decodeSeat(seat: String): Pair<Int, Int> {
        var rowRange = 0 to 127
        for (i in 0..6) {
            rowRange = halve(rowRange, seat[i] == 'F')
        }

        var colRange = 0 to 7
        for (j in 7..9) {
            colRange = halve(colRange, seat[j] == 'L')
        }

        return rowRange.first to colRange.first
    }

    private fun halve(original: Pair<Int, Int>, isFirstHalf: Boolean): Pair<Int, Int> {
        val step = (original.second - original.first) / 2
        return if (isFirstHalf) original.first to original.first + step else original.first + step + 1 to original.second
    }

    private fun getSeatId(position: Pair<Int, Int>): Int {
        return position.first * 8 + position.second
    }
}
