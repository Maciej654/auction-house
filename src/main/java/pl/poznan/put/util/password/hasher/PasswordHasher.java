package pl.poznan.put.util.password.hasher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import pl.poznan.put.util.hex.codecs.HexCodecs;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class PasswordHasher implements Function<String, String> {
    private static final String DEFAULT_ALGORITHM = "SHA-256";

    private static final Map<String, PasswordHasher> CACHE = new HashMap<>();

    public static PasswordHasher defaultInstance() {
        return of(DEFAULT_ALGORITHM);
    }

    public static PasswordHasher of(String algorithm) {
        var hasher = CACHE.get(algorithm);
        if (hasher != null) return hasher;
        synchronized (CACHE) {
            hasher = CACHE.get(algorithm);
            if (hasher != null) return hasher;
            try {
                val digest = MessageDigest.getInstance(algorithm);
                hasher = new PasswordHasher(digest);
                CACHE.put(algorithm, hasher);
                return hasher;
            }
            catch (NoSuchAlgorithmException e) {
                log.error("Cannot construct a PasswordHasher", e);
                return null;
            }
        }
    }

    private final MessageDigest digest;

    @Override
    public String apply(String raw) {
        synchronized (digest) {
            digest.reset();
            val bytes = raw.getBytes(StandardCharsets.UTF_8);
            digest.update(bytes);
            val hash = digest.digest();
            return HexCodecs.encode(hash);
        }
    }
}
