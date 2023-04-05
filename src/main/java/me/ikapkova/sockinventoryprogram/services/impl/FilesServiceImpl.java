package me.ikapkova.sockinventoryprogram.services.impl;

import me.ikapkova.sockinventoryprogram.exception.FileProcessingException;
import me.ikapkova.sockinventoryprogram.services.FilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${path.to.data.file}")
    private String pathToSocksFile;
    @Value("${name.to.data.file}")
    private String socksFileName;

    @Override
    public boolean saveToFile(String json) {
        try {
            cleanDataFile();
            Files.writeString(Path.of(pathToSocksFile, socksFileName), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String readerFromFile() throws FileProcessingException {
        try {
            return Files.readString(Path.of(pathToSocksFile, socksFileName));
        } catch (IOException e) {
            throw new FileProcessingException("Ошибка при чтении файла");
        }
    }

    @Override
    public File getDataFile() {
        return new File(pathToSocksFile + "/" + socksFileName);
    }

    @Override
    public boolean cleanDataFile() {
        try {
            Path path = Path.of(pathToSocksFile, socksFileName);

            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Path createTempFile(String suffix) {
        try {
            return Files.createTempFile(Path.of(pathToSocksFile), "tempFile", suffix);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
