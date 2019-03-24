package vblank

import org.junit.Test
import vblank.SqlFormatter
import vblank.core.Tokenizer

class UtilTest {

    @Test
    void escapeRegExp1() {
        def escaped = Util.escapeRegExp('[lodash](https://lodash.com/)')

        assert escaped == '''\\[lodash\\]\\(https://lodash\\.com/\\)'''
    }

}
