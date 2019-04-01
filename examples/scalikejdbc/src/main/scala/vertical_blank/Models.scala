package vertical_blank

import scalikejdbc._
import org.joda.time.DateTime

case class Group(id: Long, name: String, createdAt: DateTime)

// (1)
object Group extends SQLSyntaxSupport[Group] {
  override def columnNames = Seq("id", "name", "created_at")
  override val schemaName: Option[String] = None
  override val tableName: String = "groups"

  def apply(g: ResultName[Group])(rs: WrappedResultSet): Group = {
    Group(rs.long(g.id), rs.string(g.name), rs.jodaDateTime(g.createdAt))
  }

}

// Cannot remove groupId to use in query...
case class Member(id: Long, name: Option[String], groupId: Long, group: Group, createdAt: DateTime)

// (1)
object Member extends SQLSyntaxSupport[Member] {
  override def columnNames = Seq("id", "name", "group_id", "created_at")
  override val schemaName: Option[String] = None
  override val tableName: String = "members"

  def apply(m: ResultName[Member], g: ResultName[Group])(rs: WrappedResultSet): Member = {
    val group = Group(g)(rs)
    Member(rs.long(m.id), rs.stringOpt(m.name), rs.long(m.groupId), group, rs.jodaDateTime(m.createdAt))
  }

}