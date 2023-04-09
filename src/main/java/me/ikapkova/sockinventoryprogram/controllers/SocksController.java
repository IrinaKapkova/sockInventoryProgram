package me.ikapkova.sockinventoryprogram.controllers;

import java.util.Collection;
import java.util.List;
import me.ikapkova.sockinventoryprogram.model.Socks;
import me.ikapkova.sockinventoryprogram.model.SocksSize;
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
    @Operation(summary = "Приход товара на склад", description = "необходимо указать следующие показатели партии носков: " +
            "размер один из возможных вариантов: 35,36,37,38, 39, 40 or 41 " +
            "цвет один из списка: GREEN, RED, WHITE, BLACK, YELLOW, BLUE, MULTICOLORED," +
            " процент содержания хлопка в материале: целое число от 0 до 100, " +
            " количество пар приходуемых на склад")
    public ResponseEntity<Socks> addSocks(@RequestBody Socks socks) {
        Socks socks1 = socksService.addSocks(socks);
        return new ResponseEntity<>(socks1, HttpStatus.CREATED);
    }

    @PutMapping("/shift")
    @Operation(summary = "Перемещение товара на склад", description = "Вводим данные: размер, цвет, процент содержания хлопка в материале, количество пар")
    public ResponseEntity<List<Socks>> moveSocks(@RequestParam(name = "size") @Parameter(description = "Размер", example = "35,36,37,38, 39, 40 or 41") Integer size,
                                                 @RequestParam(name = "color") @Parameter(description = "Цвет", example = "GREEN, RED, WHITE, BLACK, YELLOW, BLUE or MULTICOLORED") String colors,
                                                 @RequestParam(name = "cottonPart") @Parameter(description = "процент содержания хлопка", example = "от 0 до 100")Integer cotton,
                                                 @RequestParam(name = "quantity") @Parameter(description = "Количество пар")Integer quantity)

    {
        List<Socks> socksList = socksService.moveSocks(size, colors, cotton, quantity);
        return new ResponseEntity<>(socksList, HttpStatus.CREATED);
    }


    @GetMapping("/minCotton")
    @Operation(summary = "Поиск нужного товара", description = "Вводим необходимые данные: размер, цвет, процент содержания хлопка в материале")
    public ResponseEntity<Object> getQuantitySocksMin(@RequestParam(name = "size") @Parameter(description = "Размер", example = "35,36,37,38, 39, 40 or 41") Integer size,
                                                      @RequestParam(name = "color") @Parameter(description = "Цвет один из зеленый, красный, белый, черный, желтый, синий или разноцветный", example = "GREEN, RED, WHITE, BLACK, YELLOW, BLUE or MULTICOLORED")  String colors,
                                                      @RequestParam(name = "cottonPartMin%") @Parameter(description = "Содержание хлопка в % min: от 0 до 100") Integer cotton) {
        List<Socks> list = socksService.getQuantitySocksMinCotton(size, colors, cotton);
        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }

    @GetMapping("/maxCotton")
    @Operation(summary = "Поиск нужного товара", description = "вводим необходимые данные: размер, цвет, процент содержания хлопка в материале")
    public ResponseEntity<Object> getQuantitySocksMax(@RequestParam(name = "size") @Parameter(description = "Размер", example = "35,36,37,38, 39, 40 or 41") Integer size,
                                                      @RequestParam(name = "color") @Parameter(description = "Цвет один из зеленый, красный, белый, черный, желтый, синий или разноцветный", example = "GREEN, RED, WHITE, BLACK, YELLOW, BLUE or MULTICOLORED")  String colors,
                                                      @RequestParam(name = "cottonPartMax%") @Parameter(description = "Содержание хлопка в % max: от 0 до 100") Integer cotton) {
        List<Socks> list1 = socksService.getQuantitySocksMaxCotton(size, colors, cotton);
        return new ResponseEntity<>(list1, HttpStatus.CREATED);
    }

    @GetMapping("/total")
    @Operation(summary = "Общее количество пар носков на складе по указанным параметрам", description = "Укажите размер, цвет, процент содержания хлопка")
    public ResponseEntity<Object> getQuantityTotal(@RequestParam(name = "size") @Parameter(description = "Размер", example = "35,36,37,38, 39, 40 or 41") Integer size,
                                                   @RequestParam(name = "color") @Parameter(description = "Цвет", example = "GREEN, RED, WHITE, BLACK, YELLOW, BLUE or MULTICOLORED") String colors,
                                                   @RequestParam(name = "cottonPart") @Parameter(description = "процент содержания хлопка", example = "от 0 до 100")Integer cottonPart) {
        Integer total = socksService.getQuantitySocksSize(size, colors, cottonPart);
        return new ResponseEntity<>(total, HttpStatus.CREATED);
    }

    @DeleteMapping("/defect")
    @Operation(summary = "Списание бракованных носков со склада", description = "введите: размер, цвет, процент содержания хлопка в материале, количество пар списываемого товара")
    public ResponseEntity<List<Socks>> deleteSocks(@RequestParam(name = "size") @Parameter(description = "Размер", example = "35,36,37,38, 39, 40 or 41") Integer size,
                                                   @RequestParam(name = "color") @Parameter(description = "Цвет", example = "GREEN, RED, WHITE, BLACK, YELLOW, BLUE or MULTICOLORED") String colors,
                                                   @RequestParam(name = "cottonPart") @Parameter(description = "процент содержания хлопка", example = "от 0 до 100")Integer cotton,
                                                   @RequestParam(name = "quantity") @Parameter(description = "Количество пар")Integer quantity) {
        List<Socks> socksList = socksService.deleteSocks(size, colors, cotton, quantity);
        return new ResponseEntity<>(socksList, HttpStatus.OK);
    }


    @GetMapping("/checklist")
    @Operation(summary = "Полный список носков на складе",
            description = "Инвентаризационный список всех носков")
    public ResponseEntity<Object> getAllSocks() throws Exception {
        Collection<Socks> socks = socksService.getChecklistSocks();
        return new ResponseEntity<>(socks, HttpStatus.CREATED);
    }
}