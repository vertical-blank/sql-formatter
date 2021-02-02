package com.github.vertical_blank.sqlformatter.core.util

import org.junit.jupiter.api.Test

class UtilTest {

    @Test
    void testTrimEnd() {
        assert "" == Util.trimEnd(" ");
        assert "" == Util.trimEnd("");
        assert " \r\nabc" == Util.trimEnd(" \r\nabc");
        assert " \r\nabc" == Util.trimEnd(" \r\nabc ");
        assert " \r\nabc" == Util.trimEnd(" \r\nabc\n");
        assert " \r\nabc" == Util.trimEnd(" \r\nabc\r");
        assert " \r\nabc" == Util.trimEnd(" \r\nabc\r\n");
        assert " \r\nabc" == Util.trimEnd(" \r\nabc\r\n ");
        assert " \r\nabc" == Util.trimEnd(" \r\nabc   \r\n  \n  ");
    }

}
