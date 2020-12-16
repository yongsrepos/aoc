import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D16Test {

    @Test
    fun getSolution1() {
        assertThat(D16().getSolution1()).isEqualTo(22057)
    }

    @Test
    fun getSolution2() {
        assertThat(D16().getSolution2()).isEqualTo(1093427331937)
    }
}
