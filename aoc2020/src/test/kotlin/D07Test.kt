import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class D07Test {

    @Test
    fun getSolution1() {
        assertThat(D07().getSolution1()).isEqualTo(235)
    }

    @Test
    fun getSolution1Alt() {
        assertThat(D07().getSolution1Alt()).isEqualTo(235)
    }

    @Test
    fun getSolution2() {
        assertThat(D07().getSolution2()).isEqualTo(158493)
    }
}
