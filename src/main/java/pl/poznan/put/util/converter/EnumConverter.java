package pl.poznan.put.util.converter;

import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

public class EnumConverter<T extends Enum<T>> extends StringConverter<T> {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString(T object) {
        return StringUtils.capitalize(object.name());
    }

    @Override
    public T fromString(String string) {
        return Enum.valueOf(clazz, StringUtils.upperCase(string));
    }
}
