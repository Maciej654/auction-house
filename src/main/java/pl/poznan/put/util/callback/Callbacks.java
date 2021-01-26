package pl.poznan.put.util.callback;

import lombok.experimental.UtilityClass;

@SuppressWarnings("unused")
@UtilityClass
public class Callbacks {
    public void noop()                {}

    public <T> void noop(T t)         {}

    public <T, R> void noop(T t, R r) {}
}

