import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class D24Test {

    @Test
    fun getSolution1() {
        assertThat(D24().getSolution1()).isEqualTo(512)
    }

    @Test
    fun getSolution2() {
        assertThat(D24().getSolution2()).isEqualTo(4120)
    }
}
