package pl.poznan.put.util.converter;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class EnumConverterUtils {
    public <T extends Enum<T>> String toString(Enum<T> object) {
        val words = object.name().split("_");
        return Arrays.stream(words)
                     .map(StringUtils::lowerCase)
                     .map(StringUtils::capitalize)
                     .collect(Collectors.joining(" "));
    }

    public <T extends Enum<T>> T fromString(String string, Class<T> clazz) {
        val words = string.split(" ");
        val name = Arrays.stream(words)
                         .map(StringUtils::upperCase)
                         .collect(Collectors.joining("_"));
        return Enum.valueOf(clazz, name);
    }
}
