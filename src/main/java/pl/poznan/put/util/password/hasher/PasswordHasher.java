package pl.poznan.put.util.password.hasher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class PasswordHasher implements Function<String, String> {
    private final MessageDigest digest;

    public static PasswordHasher of(String algorithm) {
        try {
            var digest = MessageDigest.getInstance(algorithm);
            return new PasswordHasher(digest);
        }
        catch (NoSuchAlgorithmException e) {
            log.error("Cannot construct a PasswordHasher", e);
            return null;
        }
    }

    @Override
    public String apply(String raw) {
        synchronized (digest) {
            digest.reset();
            var bytes = raw.getBytes(StandardCharsets.UTF_8);
            digest.update(bytes);
            var hash = digest.digest();
            return new String(hash);
        }
    }
}
