package pl.poznan.put.util.converter;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateConverterUtils {
    private final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    public String toString(LocalDateTime date) {
        return FORMATTER.format(date);
    }
}
