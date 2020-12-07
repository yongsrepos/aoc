import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class D07Test {

    @Test
    fun getSolution1() {
        assertThat(D07().getSolution1()).isEqualTo(235)
    }

    @Test
    internal fun getSolution2() {
        assertThat(D07().getSolution2()).isEqualTo(158493)
    }
}
