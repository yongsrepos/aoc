import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D03Test {

    @Test
    fun getSolution1() {
        assertThat(D03().getSolution1()).isEqualTo(159)
    }

    @Test
    fun getSolution2() {
        assertThat(D03().getSolution2()).isEqualTo(6419669520)
    }
}
