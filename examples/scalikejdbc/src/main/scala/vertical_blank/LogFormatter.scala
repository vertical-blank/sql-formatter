package vertical_blank

class LogFormatter extends scalikejdbc.SQLFormatter {
  def format(sql: String) = vertical_blank.sql_formatter.SqlFormatter.format(sql)
}