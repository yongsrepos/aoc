import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D17Test {

    @Test
    fun getSolution1() {
        assertThat(D17().getSolution1()).isEqualTo(271)
    }

    @Test
    fun getSolution2() {
        assertThat(D17().getSolution2()).isEqualTo(2064)
    }
}
