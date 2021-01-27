package pl.poznan.put.util.callback;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@UtilityClass
@Slf4j
public class Callbacks {
    public void noop() {
        log.warn("noop callback");
    }

    public <T> void noop(T t) {
        log.warn("noop callback");
    }

    public <T, R> void noop(T t, R r) {
        log.warn("noop callback");
    }
}

