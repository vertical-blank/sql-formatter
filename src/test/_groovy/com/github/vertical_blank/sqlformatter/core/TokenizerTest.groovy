package com.github.vertical_blank.sqlformatter.core

import com.github.vertical_blank.sqlformatter.languages.StandardSqlFormatter

import org.junit.jupiter.api.Test

class TokenizerTest {

    @Test
    void hoge() {
        String a = "SELECT * FROM psprcsrqst WHERE prcsname LIKE 'A\\_%' ESCAPE '\\' AND prcstype = :1"
        def tokens = new Tokenizer(new StandardSqlFormatter().dialectConfig()).tokenize(a)

        tokens.each { println it }
    }

}
