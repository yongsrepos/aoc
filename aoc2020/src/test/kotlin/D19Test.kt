import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D19Test {

    @Test
    fun getSolution1() {
        assertThat(D19().getSolution1()).isEqualTo(162)
    }

    @Test
    fun getSolution2() {
        assertThat(D19().getSolution2()).isEqualTo(267)
    }
}
