name := "ScalikeJdbcExamples"
version := "1.0"
scalaVersion := "2.12.4"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
    "org.scalikejdbc" %% "scalikejdbc"          % "3.1.0",
    "org.scalikejdbc" %% "scalikejdbc-config"   % "3.1.0",
    "ch.qos.logback"  %  "logback-classic"      % "1.2.3",
    "com.h2database"  %  "h2"                   % "1.4.195",
    "org.slf4j"       %  "slf4j-simple"         % "[1.7,)",
    "vertical_blank"  %  "sql-formatter"        % "1.0",
)
