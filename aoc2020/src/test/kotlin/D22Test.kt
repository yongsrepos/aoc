import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class D22Test {

    @Test
    fun getSolution1() {
        assertThat(D22().getSolution1()).isEqualTo(31781)
    }

    @Test
    fun getSolution2() {
        assertThat(D22().getSolution2()).isEqualTo(35154)
    }
}
