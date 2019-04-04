package vertical_blank

import scalikejdbc._
import scalikejdbc.SQLInterpolation._

object Main extends App {

  scalikejdbc.GlobalSettings.sqlFormatter = SQLFormatterSettings("vertical_blank.LogFormatter")

  // (1)
  Class.forName("org.h2.Driver")
  // (2)
  ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

/*
  // (3)
  DB autoCommit { implicit session =>
    sql"""
      create table members (
        id serial not null primary key,
        name nvarchar(64),
        created_at timestamp not null
      )
    """.execute.apply()
  }

  DB localTx { implicit session =>
    Seq("Alice", "Bob", "Chris") foreach { name =>
      // (4)
      sql"insert into members (name, created_at) values ($name, current_timestamp)".update.apply()
    }
  }
*/


  DB.readOnly { implicit session =>
    val id = 1
    val (m, g) = (Member.syntax("m"), Group.syntax("g"))
    // (1)
    val member = withSQL {
      select.from(Member as m).innerJoin(Group as g).on(m.groupId, g.id)
        .where.eq(m.id, id)
    }.map(Member(m.resultName, g.resultName)(_)).single.apply()
    println(member)
  }

}
