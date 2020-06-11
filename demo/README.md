
# Demo

## How to build

### Download jar

```bash
wget https://repo1.maven.org/maven2/com/github/vertical-blank/sql-formatter/1.0.1/sql-formatter-1.0.1.jar
```

### compile java

```bash
path/to/graalvm/bin/javac src/main/java/com/github/vertical_blank/sqlformatter/SqlFormatterDemo.java -cp ./sql-formatter-1.0.1.jar
```

### generate shared object

```bash
path/to/graalvm/bin/native-image --shared -H:Name=sqlformatterdemo -cp src/main/java/:sql-formatter-1.0.1.jar
```

### setup javascript

Note: Python 2.7 is requied by node-gyp.

```bash
cp sqlformatterdemo.so js/
cd js/
npm i
```

## deploy to google cloud function

```bash
gcloud functions deploy format_sql --runtime nodejs8 --entry-point handler --trigger-http
```
