package vblank.core.util

import org.junit.Test

class JSLikeListTest {

    @Test
    void escapeRegExp() {
        def jslist = new JSLikeList([1, 2])
        assert '12' == jslist.join()
    }

}
