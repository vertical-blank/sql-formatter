# java-sql-formatter

Java port of grate SQL formatter https://github.com/zeroturnaround/sql-formatter.

## Usage

First, add following dependency into your `pom.xml`:

```xml
<dependencies>
  <dependency>
...
  </dependency>
</dependencies>
```

You can easily use via `vertical_blank.sql_formatter.SqlFormatter`:

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


You can pass dialect name to `SqlFormatter.of`:

```
SqlFormatter
    .of("n1ql")                  // Defaults to "sql"
    .format("SELECT *");
```


You can also pass indent string as arg:

```
SqlFormatter
    .format("SELECT *", "    "); // Defaults to two spaces
```

Currently just four SQL dialects are supported:

- **sql** - [Standard SQL][]
- **n1ql** - [Couchbase N1QL][]
- **db2** - [IBM DB2][]
- **pl/sql** - [Oracle PL/SQL][]

### Placeholders replacement

```
// Named placeholders
Map<String, ?> namedParams = new HashMap<>();
namedParams.put("foo", "'bar'");
SqlFormatter.format("SELECT * FROM tbl WHERE foo = @foo", namedParams);

// Indexed placeholders
SqlFormatter.format("SELECT * FROM tbl WHERE foo = ?", Arrays.asList("'bar'"));
```

Both result in:

```
SELECT
  *
FROM
  tbl
WHERE
  foo = 'bar'
```

