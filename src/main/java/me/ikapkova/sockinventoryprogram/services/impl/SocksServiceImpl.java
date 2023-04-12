package me.ikapkova.sockinventoryprogram.services.impl;

import me.ikapkova.sockinventoryprogram.exception.FileProcessingException;
import me.ikapkova.sockinventoryprogram.exception.InvalidSockRequestException;
import me.ikapkova.sockinventoryprogram.exception.ProductNotFoundException;
import me.ikapkova.sockinventoryprogram.model.Socks;
import me.ikapkova.sockinventoryprogram.services.FilesService;
import me.ikapkova.sockinventoryprogram.services.SocksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SocksServiceImpl implements SocksService {

    final private FilesService filesService;

    private static List<Socks> socksList = new LinkedList<>();


    public SocksServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }
private void validateRequest (Socks socks){
        if (socks.getSize() == null || socks.getColor() == null || socks.getCottonPart() == null){
            throw new InvalidSockRequestException ("Все поля должны быть заполнены");
        }
    if (socks.getCottonPart() < 0 || socks.getCottonPart() >100) {
        throw new InvalidSockRequestException("Показатель содержания хлопка должен быть в виде числа от 0 до 100");
    }
    if (socks.getQuantity() <= 0){
        throw new InvalidSockRequestException("Количество должно быть больше 0");
    }
}
    @Override
    public Socks addSocks(Socks socks) {
        validateRequest(socks);
        {
//проверку аргументов  вынесла из метода
            if (!socksList.isEmpty())
            for (Socks test : socksList) {
                if (test.getSize().equals(socks.getSize()) && test.getColor().equals(socks.getColor())
                        && test.getCottonPart() == socks.getCottonPart()) {
                    test.setQuantity(socks.getQuantity() + test.getQuantity());
                    socksList.add(socks);
                    saveToFile();
                } else {
                    socksList.add(socks);
                    saveToFile();
                }
            }
            return socks;
        }
    }

    @Override
    public List<Socks> moveSocks(Integer size, String colors, Integer cotton, Integer quantity) {

        List<Socks> collect = socksList.stream()
                .filter(socks -> socks.getSize().size.equals(size) && socks.getCottonPart().equals(cotton))
                .filter(socks -> socks.getColor().color.equals(colors))
                .filter(socks -> socks.getQuantity() >= (quantity)).toList();

        if (!collect.isEmpty()) {
            collect.stream()
                    .peek(socks5 -> socks5.setQuantity(socks5.getQuantity() - quantity))
                    .toList();
            saveToFile();
        }
        return collect;
    }
    @Override
    public List<Socks> deleteSocks(Integer size, String colors, Integer cotton, Integer quantity) {

        List<Socks> collect = socksList.stream()
                .filter(socks -> socks.getSize().size.equals(size) && socks.getCottonPart().equals(cotton))
                .filter(socks -> socks.getColor().color.equals(colors))
                .filter(socks -> socks.getQuantity() >= (quantity)).toList();

        if (!collect.isEmpty()) {
            collect.stream()
                    .peek(socks5 -> socks5.setQuantity(socks5.getQuantity() - quantity))
                    .collect(Collectors.toUnmodifiableList());
            saveToFile();
        }
        return collect;
    }

    @Override
    public Collection<Socks> getChecklistSocks() {
        Collections.sort(socksList, Comparator.comparing(Socks::getSize));
        return socksList;

    }


    @Override
    public List<Socks> getQuantitySocksMinCotton(Integer size, String colors, Integer cotton) {

        return socksList.stream()
                .filter(socks -> socks.getSize().size.equals(size) && socks.getCottonPart() >= cotton)
                .filter(socks -> socks.getColor().color.equals(colors))
                .collect(Collectors.toList());

    }

    @Override
    public List<Socks> getQuantitySocksMaxCotton(Integer size, String colors, Integer cotton) {

        return socksList.stream()
                .filter(socks -> socks.getSize().size.equals(size) && socks.getCottonPart() <= cotton)
                .filter(socks -> socks.getColor().color.equals(colors))
                .collect(Collectors.toList());

    }

    @Override
    public Integer getQuantitySocksSize(Integer size, String colors, Integer cotton) {
        for (Socks socks : socksList) {
            if (socks.getColor().equals(colors) &&
                    socks.getSize().equals(size) &&
                    socks.getCottonPart() > cotton) {
                return socks.getQuantity();
            } else if (!socksList.iterator().hasNext()) {
                throw new ProductNotFoundException("Товар с данными параметрами не найден");
            }
        }
        return null;
    }


    @Override
    public Path createSocks() throws IOException {

        Path path = filesService.createTempFile("allSocks");
        for (Socks socks : socksList) {
            try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.append("\nРазмер " + "'")
                        .append(String.valueOf(socks.getSize()))
                        .append("'").append(",\tцвет - ")
                        .append(socks.getColor().color)
                        .append(", \tсостав: хлопок - ")
                        .append(String.valueOf(socks.getCottonPart()))
                        .append("%").append(", \tкол-во: ")
                        .append(String.valueOf(socks.getQuantity()))
                        .append(" шт.");
            }
        }
        return path;
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksList);
            filesService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при записи файла");
        }
    }

    private void readFromFile() throws FileProcessingException {
        String json = filesService.readerFromFile();
        try {
            socksList = new ObjectMapper().readValue(json, new TypeReference<LinkedList<Socks>>() {
            });
        } catch (JsonProcessingException e) {
            throw new FileProcessingException("Ошибка при чтении файла");
        }
    }

    private void allColors() throws FileProcessingException {
        String json = filesService.readerFromFile();
        try {
            socksList = new ObjectMapper().readValue(json, new TypeReference<LinkedList<Socks>>() {
            });
        } catch (JsonProcessingException e) {
            throw new FileProcessingException("Ошибка при чтении файла");
        }
    }
}
