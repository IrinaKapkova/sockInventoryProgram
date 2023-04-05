package me.ikapkova.sockinventoryprogram.model;

public enum SocksSize {
    XXS(35), XS(36), S(37), M(38), L(39), XL(40), XXL(41);

    public final Integer size;

    SocksSize(Integer size) {
        this.size = size;
    }
}