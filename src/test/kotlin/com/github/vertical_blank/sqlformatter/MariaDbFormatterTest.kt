package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.languages.Dialect
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object MariaDbFormatterTest :
    Spek({
      val formatter = SqlFormatter.of(Dialect.MariaDb)

      describe("MariaDbFormatter") { with(formatter) { behavesLikeMariaDbFormatter(formatter) } }
    })
