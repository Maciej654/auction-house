package pl.poznan.put.util.converter;

import javafx.util.StringConverter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumConverter<T extends Enum<T>> extends StringConverter<T> {
    private final Class<T> clazz;

    public EnumConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString(T object) {
        val words = object.name().split("_");
        return Arrays.stream(words)
                     .map(StringUtils::lowerCase)
                     .map(StringUtils::capitalize)
                     .collect(Collectors.joining(" "));
    }

    @Override
    public T fromString(String string) {
        val words = string.split(" ");
        val name = Arrays.stream(words)
                         .map(StringUtils::upperCase)
                         .collect(Collectors.joining("_"));
        return Enum.valueOf(clazz, name);
    }
}
