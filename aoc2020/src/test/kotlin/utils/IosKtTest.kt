package utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IosKtTest {

    @Test
    fun getLines() {
        val lines = getLines(IosKtTest(), "inputs_test.txt")

        assertThat(lines).hasSize(2)
        assertThat(lines).contains("1")
        assertThat(lines).contains("2")
    }
}
