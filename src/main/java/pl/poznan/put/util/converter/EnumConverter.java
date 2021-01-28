package pl.poznan.put.util.converter;

import javafx.util.StringConverter;

public class EnumConverter<T extends Enum<T>> extends StringConverter<T> {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString(T object) {
        return EnumConverterUtils.toString(object);
    }

    @Override
    public T fromString(String string) {
        return EnumConverterUtils.fromString(string, clazz);
    }
}
