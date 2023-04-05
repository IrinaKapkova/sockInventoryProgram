package me.ikapkova.sockinventoryprogram.model;

public enum SocksColors {
    GREEN("зеленый"), RED("красный"), WHITE("белый"), BLACK("черный"), YELLOW("желтый"), BLUE("синий"), MULTICOLORED("многоцветный");
    public final String color;

    SocksColors(String color) {
        this.color = color;
    }
}
