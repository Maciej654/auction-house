package pl.poznan.put.util.hex.codecs;

import java.nio.charset.StandardCharsets;

public class HexCodecs {
    private static final byte[] HEX = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

    public String encode(byte[] bytes) {
        byte[] chars = new byte[bytes.length << 1];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            int p = i << 1;
            chars[p] = HEX[v >>> 4];
            chars[p | 1] = HEX[v & 0x0F];
        }
        return new String(chars, StandardCharsets.UTF_8);
    }
}
