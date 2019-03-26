package vblank.core

import org.junit.Test
import vblank.SqlFormatter

class N1qlFormatterTest {

    static expect(String result) {
        [toBe: { assert result == it }]
    }

    static lang = Config.builder().language("n1ql").indent("  ").build()

    @Test
    void "formats SELECT query with element selection expression"() {
        def result = SqlFormatter.format("SELECT orderlines[0].productId FROM orders;", lang)
        expect(result).toBe(
            "SELECT\n" +
                "  orderlines[0].productId\n" +
                "FROM\n" +
                "  orders;"
        )
    };

}
