package com.github.vertical_blank.sqlformatter.core.util

import org.junit.jupiter.api.Test

class RegexUtilTest {

    @Test
    void escapeRegExp() {
        def escaped = RegexUtil.escapeRegExp('[lodash](https://lodash.com/)')
        assert escaped == '''\\[lodash\\]\\(https://lodash\\.com/\\)'''
    }

}
