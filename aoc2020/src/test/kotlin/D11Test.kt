import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D11Test {

    @Test
    fun getSolution1() {
        assertThat(D11().getSolution1()).isEqualTo(2368)
    }

    @Test
    fun getSolution2() {
        assertThat(D11().getSolution2()).isEqualTo(2124)
    }
}
