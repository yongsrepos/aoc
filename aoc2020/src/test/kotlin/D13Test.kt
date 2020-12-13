import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D13Test {

    @Test
    fun getSolution1() {
        assertThat(D13().getSolution1()).isEqualTo(3215)
    }

    @Test
    fun getSolution2() {
        assertThat(D13().getSolution2()).isEqualTo(1001569619313439L)
    }
}
