package hx.nine.eleven.file.io;

import java.io.IOException;

public interface ResourcePatternResolver extends ResourceLoader {

    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    Resource[] getResources(String var1) throws IOException;
}
