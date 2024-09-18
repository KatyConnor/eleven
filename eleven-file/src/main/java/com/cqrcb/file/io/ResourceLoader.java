package com.cqrcb.file.io;

public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String var1);

    ClassLoader getClassLoader();
}
