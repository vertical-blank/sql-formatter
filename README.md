# sql-formatter

[![Maven Central](https://img.shields.io/maven-central/v/com.github.vertical-blank/sql-formatter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.vertical-blank%22%20AND%20a:%22sql-formatter%22)
![travis](https://api.travis-ci.org/vertical-blank/sql-formatter.svg?branch=master)
[![codecov](https://codecov.io/gh/vertical-blank/sql-formatter/branch/master/graph/badge.svg)](https://codecov.io/gh/vertical-blank/sql-formatter)

Java port of great SQL formatter <https://github.com/zeroturnaround/sql-formatter.>

Written with only Java Standard Library, without dependencies.

[Demo](http://www.vertical-blank.com/sql-formatter/)

Demo is running on Google Cloud Function, with native-compiled shared library by GraalVM.

## Usage

### Maven

```xml
<dependency>
  <groupId>com.github.vertical-blank</groupId>
  <artifactId>sql-formatter</artifactId>
  <version>1.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.github.vertical-blank:sql-formatter:1.0'
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

### Dialect

You can pass dialect name to `SqlFormatter.of` :

```java
SqlFormatter
    .of("n1ql")  // Defaults to "sql"
    .format("SELECT *");
```

Currently just four SQL dialects are supported:

- **sql** - [Standard SQL](https://en.wikipedia.org/wiki/SQL:2011)
- **n1ql** - [Couchbase N1QL](http://www.couchbase.com/n1ql)
- **db2** - [IBM DB2](https://www.ibm.com/analytics/us/en/technology/db2/)
- **pl/sql** - [Oracle PL/SQL](http://www.oracle.com/technetwork/database/features/plsql/index.html)

### Format

Defaults to two spaces.
You can pass indent string to `format` :

```java
SqlFormatter.format("SELECT * FROM table1", "    ");
```

This will output:

```sql
SELECT
    *
FROM
    table1
```

### Placeholders replacement

You can pass List or Map to `format` :

```java
// Named placeholders
Map<String, String> namedParams = new HashMap<>();
namedParams.put("foo", "'bar'");
SqlFormatter.format("SELECT * FROM tbl WHERE foo = @foo", namedParams);

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
