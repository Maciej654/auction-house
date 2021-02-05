package pl.poznan.put.util.converter;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateConverterUtils {
    private final String LOG_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final String END_DATE_PATTERN = "yyyy-MM-dd HH:mm";

    private final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern(LOG_DATE_PATTERN);
    private final DateTimeFormatter END_DATE_FORMATTER = DateTimeFormatter.ofPattern(END_DATE_PATTERN);

    public String logDateToString(LocalDateTime localDateTime) {
        return LOG_DATE_FORMATTER.format(localDateTime);
    }

    public String endDateToString(LocalDateTime localDateTime) {
        return END_DATE_FORMATTER.format(localDateTime);
    }
}
