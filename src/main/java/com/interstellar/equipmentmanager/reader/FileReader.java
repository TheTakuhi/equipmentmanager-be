package com.interstellar.equipmentmanager.reader;

import java.io.InputStream;

public interface FileReader {
    InputStream readFileFromResources(String path);
}
