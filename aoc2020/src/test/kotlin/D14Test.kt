import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D14Test {

    @Test
    fun getSolution1() {
        assertThat(D14().getSolution1()).isEqualTo(9615006043476)
    }

    @Test
    fun getSolution2() {
        assertThat(D14().getSolution2()).isEqualTo(4275496544925)
    }
}
