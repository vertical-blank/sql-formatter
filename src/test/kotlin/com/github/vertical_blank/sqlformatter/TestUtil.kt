package com.github.vertical_blank.sqlformatter

import org.amshove.kluent.shouldBeEqualTo

interface Expect<T> {
  fun toBe(v2: T)
}

fun <T> expect(v: T) =
    object : Expect<T> {
      override fun toBe(v2: T) {
        v shouldBeEqualTo v2
      }
    }
