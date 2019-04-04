package com.github.vertical.blank.core.util

import org.junit.jupiter.api.Test

class JSLikeListTest {

    @Test
    void mapAndThenJoin() {
        assert '2,4' == new JSLikeList([1, 2]).map { it * 2 }.join()
    }

}
