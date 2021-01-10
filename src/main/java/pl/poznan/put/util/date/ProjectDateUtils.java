package pl.poznan.put.util.date;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;

@UtilityClass
public class ProjectDateUtils {
    public final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final SimpleDateFormat FORMAT = new SimpleDateFormat(PATTERN);
}
