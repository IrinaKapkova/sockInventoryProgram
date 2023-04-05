package me.ikapkova.sockinventoryprogram.services;

import me.ikapkova.sockinventoryprogram.model.Socks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public interface SocksService {


    Socks addSocks(Socks socks);

    List<Socks> moveSocks(Integer size, String colors, Integer cotton, Integer quantity);

    List<Socks> deleteSocks(Integer size, String colors, Integer cotton, Integer quantity);

    Collection<Socks> getChecklistSocks();

    List<Socks>  getQuantitySocksMinCotton(Integer size, String colors, Integer cotton);

    List<Socks>  getQuantitySocksMaxCotton(Integer size, String colors, Integer cotton);

    Integer getQuantitySocksSize(Integer size, String colors, Integer cotton);

    Path createSocks() throws IOException;
}