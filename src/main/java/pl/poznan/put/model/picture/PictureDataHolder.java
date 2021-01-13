package pl.poznan.put.model.picture;

import lombok.Data;

@Data
public class PictureDataHolder {
    private final byte[] data;
    private final String name;
}
