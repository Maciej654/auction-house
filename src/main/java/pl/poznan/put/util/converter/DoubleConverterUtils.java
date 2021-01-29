package pl.poznan.put.util.converter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DoubleConverterUtils {
    private final String PRICE_FORMAT = "%.2f";

    public String toString(double d) {
        return String.format(PRICE_FORMAT, d);
    }

    public double fromString(String d) {
        return Double.parseDouble(normalize(d));
    }

    private String normalize(String d) {
        return d.replace(',', '.');
    }
}
