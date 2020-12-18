import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D18Test {

    @Test
    fun getSolution1() {
        assertThat(D18().getSolution1()).isEqualTo(4940631886147L)
    }

    @Test
    fun getSolution2() {
        assertThat(D18().getSolution2()).isEqualTo(283582817678281L)
    }
}
