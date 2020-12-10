import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D10Test {

    @Test
    fun getSolution1() {
        assertThat(D10().getSolution1()).isEqualTo(1)
    }

    @Test
    fun getSolution2() {
        assertThat(D10().getSolution2()).isEqualTo(442136281481216)
    }
}
