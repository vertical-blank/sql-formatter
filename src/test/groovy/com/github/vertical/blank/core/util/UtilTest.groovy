package com.github.vertical.blank.core.util

import org.junit.jupiter.api.Test

class UtilTest {

    @Test
    void escapeRegExp() {
        def escaped = Util.escapeRegExp('[lodash](https://lodash.com/)')
        assert escaped == '''\\[lodash\\]\\(https://lodash\\.com/\\)'''
    }

}
