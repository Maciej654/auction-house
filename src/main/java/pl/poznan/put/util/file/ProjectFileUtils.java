package pl.poznan.put.util.file;

import lombok.experimental.UtilityClass;

import java.io.File;

@UtilityClass
public class ProjectFileUtils {
    public String homePathName  = System.getProperty("user.home");
    public File   homeDirectory = new File(homePathName);
}
