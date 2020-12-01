import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class D01Test {

    @Test
    fun getSolution1() {
        assertThat(D01().getSolution1()).isEqualTo(712075)
    }

    @Test
    fun getSolution2() {
        assertThat(D01().getSolution2()).isEqualTo(145245270)
    }
}
