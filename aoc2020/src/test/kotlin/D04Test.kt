import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class D04Test {

    @Test
    fun getSolution1() {
        assertThat(D04().getSolution1()).isEqualTo(233)
    }

    @Test
    fun getSolution2() {
        assertThat(D04().getSolution2()).isEqualTo(111)
    }
}
