package com.interstellar.equipmentmanager.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TestPrinter {
    private static final ObjectWriter writer = new ObjectMapper()
            .findAndRegisterModules()
            .writerWithDefaultPrettyPrinter();

    public static String prettifyObjectPrint(Object object) {
        String res;
        try {
            res = writer.writeValueAsString(object);
        } catch (Exception e) {
            res = String.format("\n{%s}\n", e.getMessage());
        }
        return res;
    }
}
