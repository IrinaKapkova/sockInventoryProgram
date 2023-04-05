package me.ikapkova.sockinventoryprogram.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Socks {

    private SocksColors color;
    private SocksSize size;
    private Integer cottonPart;
    private Integer quantity;

}