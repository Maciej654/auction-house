package pl.poznan.put.util.task;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProjectTaskUtils {
    public final long IMMEDIATE   = 0;
    public final long MILLISECOND = 1;
    public final long SECOND      = 1000 * MILLISECOND;
    public final long MINUTE      = 60 * SECOND;
}
