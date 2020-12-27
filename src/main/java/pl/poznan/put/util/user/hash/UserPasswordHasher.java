package pl.poznan.put.util.user.hash;

import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

@RequiredArgsConstructor
public class UserPasswordHasher implements Function<String, String> {
    public static UserPasswordHasher of(String algorithm) {
        try {
            var digest = MessageDigest.getInstance(algorithm);
            return new UserPasswordHasher(digest);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private final MessageDigest digest;

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
