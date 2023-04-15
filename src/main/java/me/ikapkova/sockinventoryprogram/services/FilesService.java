package me.ikapkova.sockinventoryprogram.services;

import java.io.File;
import java.nio.file.Path;

public interface FilesService {

    boolean saveToFile(String json);

    String readerFromFile();

    File getDataFile();

    boolean cleanDataFile();

    Path createTempFile(String suffix);
}