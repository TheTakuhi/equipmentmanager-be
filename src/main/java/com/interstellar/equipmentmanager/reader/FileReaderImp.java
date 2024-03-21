package com.interstellar.equipmentmanager.reader;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class FileReaderImp implements FileReader {
    @Override
    public InputStream readFileFromResources(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) throw new FileNotFoundException(
                    String.format("Requested file at path '%s' could not be found", path)
            );
            return is;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
