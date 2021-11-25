# sql-formatter

[![Maven Central](https://img.shields.io/maven-central/v/com.github.vertical-blank/sql-formatter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.vertical-blank%22%20AND%20a:%22sql-formatter%22)
![Java CI with Maven](https://github.com/vertical-blank/sql-formatter/workflows/Java%20CI%20with%20Maven/badge.svg)
[![codecov](https://codecov.io/gh/vertical-blank/sql-formatter/branch/master/graph/badge.svg)](https://codecov.io/gh/vertical-blank/sql-formatter)

Java port of great SQL formatter <https://github.com/zeroturnaround/sql-formatter>.

Written with only Java Standard Library, without dependencies.

[Demo](http://www.vertical-blank.com/sql-formatter/)

Demo is running on Google Cloud Function, with native-compiled shared library by GraalVM.

This does not support:

- Stored procedures.
- Changing of the delimiter type to something else than ;.

## Usage

### Maven

```xml
<dependency>
  <groupId>com.github.vertical-blank</groupId>
  <artifactId>sql-formatter</artifactId>
  <version>2.0.3</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.github.vertical-blank:sql-formatter:2.0.3'
```

## Examples

You can easily use `com.github.vertical_blank.sqlformatter.SqlFormatter` :

```java
SqlFormatter.format("SELECT * FROM table1")
```

This will output:

```sql
SELECT
  *
FROM
  table1
```

You can also pass `FormatConfig` object built by builder:

```js
SqlFormatter.format('SELECT * FROM tbl',
  FormatConfig.builder()
    .indent("    ") // Defaults to two spaces
    .uppercase(true) // Defaults to false (not safe to use when SQL dialect has case-sensitive identifiers)
    .linesBetweenQueries(2) // Defaults to 1
    .maxColumnLength(100) // Defaults to 50
    .params(Arrays.asList("a", "b", "c")) // Map or List. See Placeholders replacement.
    .build()
);
```

### Dialect

You can pass dialect `com.github.vertical_blank.sqlformatter.languages.Dialect` or `String` to `SqlFormatter.of` :

```java
SqlFormatter
    .of(Dialect.N1ql)  // Recommended
     //.of("n1ql")      // String can be passed
    .format("SELECT *");
```

SQL formatter supports the following dialects:

- **sql** - [Standard SQL][]
- **mariadb** - [MariaDB][]
- **mysql** - [MySQL][]
- **postgresql** - [PostgreSQL][]
- **db2** - [IBM DB2][]
- **plsql** - [Oracle PL/SQL][]
- **n1ql** - [Couchbase N1QL][]
- **redshift** - [Amazon Redshift][]
- **spark** - [Spark][]
- **tsql** - [SQL Server Transact-SQL][tsql]

### Extend formatters

Formatters can be extended as below :

```java
SqlFormatter
    .of(Dialect.MySql)
    .extend(cfg -> cfg.plusOperators("=>"))
    .format("SELECT * FROM table WHERE A => 4")
```

Then it results in:

```sql
SELECT
  *
FROM
  table
WHERE
  A => 4
```

### Placeholders replacement

You can pass `List` or `Map` to `format` :

```java
// Named placeholders
Map<String, String> namedParams = new HashMap<>();
namedParams.put("foo", "'bar'");
SqlFormatter.of(Dialect.TSql).format("SELECT * FROM tbl WHERE foo = @foo", namedParams);

// Indexed placeholders
SqlFormatter.format("SELECT * FROM tbl WHERE foo = ?", Arrays.asList("'bar'"));
```

Both result in:

```sql
SELECT
  *
FROM
  tbl
WHERE
  foo = 'bar'
```

## Build

Building this library requires JDK 11 because of [ktfmt](https://github.com/facebookincubator/ktfmt).


[standard sql]: https://en.wikipedia.org/wiki/SQL:2011
[couchbase n1ql]: http://www.couchbase.com/n1ql
[ibm db2]: https://www.ibm.com/analytics/us/en/technology/db2/
[oracle pl/sql]: http://www.oracle.com/technetwork/database/features/plsql/index.html
[amazon redshift]: https://docs.aws.amazon.com/redshift/latest/dg/cm_chap_SQLCommandRef.html
[spark]: https://spark.apache.org/docs/latest/api/sql/index.html
[postgresql]: https://www.postgresql.org/
[mariadb]: https://mariadb.com/
[mysql]: https://www.mysql.com/
[tsql]: https://docs.microsoft.com/en-us/sql/sql-server/