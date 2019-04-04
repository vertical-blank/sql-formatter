package com.github.vertical_blank.sqlformatter.core.util

import org.junit.jupiter.api.Test

class UtilTest {

    @Test
    void escapeRegExp() {
        def escaped = Util.escapeRegExp('[lodash](https://lodash.com/)')
        assert escaped == '''\\[lodash\\]\\(https://lodash\\.com/\\)'''
    }

}
