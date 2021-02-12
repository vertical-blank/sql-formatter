package com.github.vertical_blank.sqlformatter

import com.github.vertical_blank.sqlformatter.core.FormatConfig

import groovy.transform.TypeChecked

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse

@TypeChecked
public class SqlFormatterTestTestTest {
  def format = { String query, FormatConfig cfg = [:] -> SqlFormatter.format(query, cfg) }

  @Nested
  @DisplayName("空のカートを作成する")
  class NestedTTTTT {

    @DisplayName("カートは商品を持っていない 1")
    @Test
    void falseTrue() {
      println SqlFormatter.standard().extend(a -> a).format("SELECT * FROM HOGE;");
    }

  }

  // @Nested
  // @DisplayName("空のカートを作成する 2")
  // class NestedTTTTT2 {

  //   @DisplayName("カートは商品を持っていない 2")
  //   @Test
  //   void falseTrue() {
  //     assertFalse(false)
  //   }

  // }

  // @Test
  // void hogehoge() {
  //   assert 1 == 1
  // }

}
