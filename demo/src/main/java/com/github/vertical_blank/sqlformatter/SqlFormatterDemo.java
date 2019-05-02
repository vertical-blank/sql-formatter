package com.github.vertical_blank.sqlformatter;

import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.nativeimage.c.type.CCharPointer;

public final class SqlFormatterDemo {

    @CEntryPoint(name = "hello")
    static CCharPointer hello(IsolateThread thread) {
        return CTypeConversion.toCString("hello from native lib").get();
    }

    @CEntryPoint(name = "format_sql")
    static CCharPointer formatSql(IsolateThread thread, CCharPointer sql) {
        return CTypeConversion.toCString(SqlFormatter.format(CTypeConversion.toJavaString(sql))).get();
    }

}