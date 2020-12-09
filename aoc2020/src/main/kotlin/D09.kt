import utils.IoHelper

class D09 {
    fun getSolution1(): Long {
        val nums = getInputs()
        val sums = getSums(nums, 25)
        for (i in 25 until nums.size) {
            if (nums[i] !in sums) {
                return nums[i]
            }
            sums.drop(24)
            var insertionIdx = 0
            for (j in 23 downTo 1) {
                insertionIdx += j
                sums.add(insertionIdx, nums[i] + nums[i - j - 1])
            }
            sums.add(nums[i] + nums[i - 1])
        }

        throw IllegalStateException("Not found answer to solution 1")
    }

    fun getSolution2(): Long {
        val invalidNum = getSolution1()
        val nums = getInputs()
        for (len in 2..nums.size) {
            for (i in 0 until (nums.size - len)) {
                val subList = nums.subList(i, i + len)
                if (subList.sum() == invalidNum) {
                    return subList.minOrNull()!! + subList.maxOrNull()!!
                }
            }
        }

        throw IllegalStateException("Not found answer to solution 2")
    }

    private fun getSums(nums: List<Long>, length: Int): MutableList<Long> {
        val sums = mutableListOf<Long>()

        for (i in 0 until (length - 1)) {
            for (j in (i + 1) until length) {
                sums.add(nums[i] + nums[j])
            }
        }

        return sums
    }

    private fun getInputs(): List<Long> {
        return IoHelper().getLongs("d09.in")
    }
}
