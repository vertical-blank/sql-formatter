package com.github.vertical_blank.sqlformatter.core.util

import org.junit.jupiter.api.Test

class UtilTest {

    @Test
    void testTrimEnd() {
        assert "" == Util.trimSpacesEnd(" ");
        assert "" == Util.trimSpacesEnd("");
        assert " \r\nabc" == Util.trimSpacesEnd(" \r\nabc");
        assert " \r\nabc" == Util.trimSpacesEnd(" \r\nabc ");
    }

    @Test
    void testRepeat() {
        assert "a" * 4 == Util.repeat("a", 4)
    }

}
