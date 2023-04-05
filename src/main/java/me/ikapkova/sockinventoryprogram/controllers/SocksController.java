package me.ikapkova.sockinventoryprogram.controllers;

import java.util.Collection;
import java.util.List;
import me.ikapkova.sockinventoryprogram.model.Socks;
import me.ikapkova.sockinventoryprogram.services.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/socks")
@Tag(name = "Носки", description = "CRUD - операции по учету носков")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @GetMapping
    @Operation(summary = "Назначение программы")
    public String Socks() {
        return "Складской учет носков";
    }


    @PostMapping("/add")
    @Operation(summary = "Приход товара на склад", description = "необходимо указать следующие показатели партии носков: размер, " +
            "цвет, процент содержания хлопка в материале, количество пар приходуемых на склад")
    public ResponseEntity<Socks> addSocks(@RequestBody Socks socks) {
        Socks socks1 = socksService.addSocks(socks);
        return new ResponseEntity<>(socks1, HttpStatus.CREATED);
    }

    @PutMapping("/shift")
    @Operation(summary = "Перемещение товара на склад", description = "вводим данные: размер, цвет, процент содержания хлопка в материале, количество пар")
    public ResponseEntity<List<Socks>> moveSocks(@RequestParam(name = "Размер:") Integer size1,
                                                 @RequestParam(name = "Цвет:") String colors1,
                                                 @RequestParam(name = "Содержание хлопка в %:") Integer cotton1,
                                                 @RequestParam(name = "Количество пар") Integer quantity1) {
        List<Socks> socksList = socksService.moveSocks(size1, colors1, cotton1, quantity1);
        return new ResponseEntity<>(socksList, HttpStatus.CREATED);
    }


    @GetMapping("/minCotton")
    @Operation(summary = "Поиск нужного товара", description = "вводим необходимые данные: размер, цвет, процент содержания хлопка в материале")
    public ResponseEntity<Object> getQuantitySocksMin(@RequestParam(name = "Размер: ") @Parameter(description = " от 36 до 40") Integer size,
                                                      @RequestParam(name = "Цвет:") @Parameter(description = "белый, черный, розовый, голубой, красный, разноцветный") String colors,
                                                      @RequestParam(name = "Содержание хлопка в % min") @Parameter(description = "от 0 до 100") Integer cotton) {
        List<Socks> list = socksService.getQuantitySocksMinCotton(size, colors, cotton);
        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

    @GetMapping("/maxCotton")
    @Operation(summary = "Поиск нужного товара", description = "вводим необходимые данные: размер, цвет, процент содержания хлопка в материале")
    public ResponseEntity<Object> getQuantitySocksMax(@RequestParam(name = "Размер: ") @Parameter(description = " от 35 до 41") Integer size,
                                                      @RequestParam(name = "Цвет:") @Parameter(description = "GREEN, RED, WHITE, BLACK, YELLOW, BLUE, MULTICOLORED") String colors,
                                                      @RequestParam(name = "max содержание хлопка в материале % ") @Parameter(description = "от 0 до 100") Integer cotton) {
        List<Socks> list1 = socksService.getQuantitySocksMaxCotton(size, colors, cotton);
        return new ResponseEntity<>(list1, HttpStatus.CREATED);
    }

    @GetMapping("/total")
    @Operation(summary = "Общее количество пар носков на складе по указанным параметрам", description = "Укажите размер, цвет, процент содержания хлопка")
    public ResponseEntity<Object> getQuantityTotal(@RequestParam(name = "Размер: ") @Parameter(description = " от 35 до 41") Integer size,
                                                   @RequestParam(name = "Цвет:") @Parameter(description = "GREEN, RED, WHITE, BLACK, YELLOW, BLUE, MULTICOLORED") String colors,
                                                   @RequestParam(name = "cottonPart") @Parameter(description = "от 0 до 100") Integer cottonPart) {
        Integer total = socksService.getQuantitySocksSize(size, colors, cottonPart);
        return new ResponseEntity<>(total, HttpStatus.CREATED);
    }

    @DeleteMapping("/defect")
    @Operation(summary = "Удаление брака", description = "введите: размер, цвет, процент содержания хлопка в материале, количество пар списываемого товара")
    public ResponseEntity<List<Socks>> deleteSocks(@RequestParam(name = "Размер:") Integer size1,
                                                   @RequestParam(name = "Цвет") String colors1,
                                                   @RequestParam(name = "процент содержания хлопка") Integer cotton1,
                                                   @RequestParam(name = "Количество товара") Integer quantity1) {
        List<Socks> socksList = socksService.deleteSocks(size1, colors1, cotton1, quantity1);
        return new ResponseEntity<>(socksList, HttpStatus.CREATED);
    }

    @GetMapping("/checklist")
    @Operation(summary = "Полный список носков на складе",
            description = "Инвентаризационный список всех носков")
    public ResponseEntity<Object> getAllSocks() throws Exception {
        Collection<Socks> socks = socksService.getChecklistSocks();
        return new ResponseEntity<>(socks, HttpStatus.CREATED);
    }
}