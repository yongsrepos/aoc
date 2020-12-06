import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D06Test {

    @Test
    fun getSolution1() {
        assertThat(D06().getSolution1()).isEqualTo(6930)
    }

    @Test
    fun getSolution2() {
        assertThat(D06().getSolution2()).isEqualTo(3585)
    }
}
