package utils

// on condition the same file name appears once in classpath, including prod and test code
class IoHelper {

    fun getInts(filename: String): List<Int> {
        return getLines(filename).map { it.toInt() }
    }

    fun getLongs(filename: String): List<Long> {
        return getLines(filename).map { it.toLong() }
    }

    fun getLines(filename: String): List<String> {
        return getRawContent(filename)?.lines().orEmpty().filter { it.isNotBlank() }
    }

    fun getSections(filename: String, delimiter: String): List<String> {
        return getRawContent(filename)?.split(delimiter).orEmpty()
    }

    fun getRawContent(filename: String): String? {
        return this.javaClass.classLoader.getResource(filename)?.readText()?.trim()
    }
}



