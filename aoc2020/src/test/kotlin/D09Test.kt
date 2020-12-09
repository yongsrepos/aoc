import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D09Test {

    @Test
    fun getSolution1() {
        assertThat(D09().getSolution1()).isEqualTo(85848519)
    }

    @Test
    fun getSolution2() {
        assertThat(D09().getSolution2()).isEqualTo(13414198)
    }
}
