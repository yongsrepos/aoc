import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D12Test {

    @Test
    fun getSolution1() {
        assertThat(D12().getSolution1()).isEqualTo(1457)
    }

    @Test
    fun getSolution2() {
        assertThat(D12().getSolution2()).isEqualTo(106860)
    }
}
