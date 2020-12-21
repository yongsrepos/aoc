import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class D21Test {

    @Test
    fun getSolution1() {
        assertThat(D21().getSolution1()).isEqualTo(2324)
    }

    @Test
    fun getSolution2() {
        assertThat(D21().getSolution2()).isEqualTo("bxjvzk,hqgqj,sp,spl,hsksz,qzzzf,fmpgn,tpnnkc")
    }
}
