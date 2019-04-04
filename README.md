# sql-formatter ![travis](https://api.travis-ci.org/vertical-blank/sql-formatter.svg?branch=master)

Java port of great SQL formatter https://github.com/zeroturnaround/sql-formatter.

Written in only Java Standard Library, with no dependent library.

## Usage

### Maven

First, add following dependency into your `pom.xml` :

```xml
<dependencies>
  <dependency>
...
  </dependency>
</dependencies>
```

### Gradle

`'com.github.vertical.blank:sql-formatter:1.0'`


## Examples

You can easily use `com.github.vertical.blank.sqlformatter.SqlFormatter` :

```
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
