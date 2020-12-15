import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D15Test {

    @Test
    fun getSolution1() {
        assertThat(D15().getSolution1()).isEqualTo(706)
    }

    @Test
    fun getSolution2() {
        assertThat(D15().getSolution2()).isEqualTo(19331)
    }
}
