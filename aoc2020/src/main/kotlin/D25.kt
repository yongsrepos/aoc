class D25 {
    fun getSolution1(): Long {
        val yong = 1614360L to 7734663L
        val cardPublicKey = yong.first
        val doorPublicKey = yong.second
        val doorLoopSize = getLoopSize(doorPublicKey)
        return transform(cardPublicKey, doorLoopSize)
    }

    private fun getLoopSize(publicKey: Long): Int {
        var value = 1L
        var loopSize = 0
        while (value != publicKey) {
            value = value * 7
            value = value % 20201227
            loopSize++
        }
        return loopSize
    }

    private fun transform(subjectNum: Long, loopSize: Int): Long {
        var value = 1L
        repeat(loopSize) {
            value = (value * subjectNum) % 20201227
        }
        return value
    }
}
