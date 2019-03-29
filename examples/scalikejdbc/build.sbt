name := "ScalikeJdbcExamples"
version := "1.0"
scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
    "org.scalikejdbc" %% "scalikejdbc"          % "3.1.0",
    "org.scalikejdbc" %% "scalikejdbc-config"   % "3.1.0",
    "ch.qos.logback"  %  "logback-classic"      % "1.2.3",
    "org.hsqldb"      %  "hsqldb" % "[2,)",
    "org.slf4j"       %  "slf4j-simple" % "[1.7,)"
)
