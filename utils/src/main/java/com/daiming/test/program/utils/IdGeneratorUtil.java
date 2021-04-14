package com.daiming.test.program.utils;

import java.util.UUID;

public class IdGeneratorUtil {

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replace("-", "");
    }
}
