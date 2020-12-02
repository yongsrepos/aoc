import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D02Test {

    @Test
    fun getSolution1() {
        assertThat(D02().getSolution1()).isEqualTo(666)
    }

    @Test
    fun getSolution2() {
        assertThat(D02().getSolution2()).isEqualTo(670)
    }
}
