package pl.poznan.put.util.converter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PriceConverterUtils {
    public String toString(double price) {
        return DoubleConverterUtils.toString(price) + " PLN";
    }
}
