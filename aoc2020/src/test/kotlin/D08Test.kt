import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D08Test {

    @Test
    fun getSolution1() {
        assertThat(D08().getSolution1()).isEqualTo(1832)
    }

    @Test
    fun getSolution2() {
        assertThat(D08().getSolution2()).isEqualTo(662)
    }
}
