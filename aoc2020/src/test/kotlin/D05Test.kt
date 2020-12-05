import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class D05Test {

    @Test
    fun getSolution1() {
        assertThat(D05().getSolution1()).isEqualTo(850)
    }

    @Test
    fun getSolution2() {
        assertThat(D05().getSolution2()).isEqualTo(599)
    }
}
