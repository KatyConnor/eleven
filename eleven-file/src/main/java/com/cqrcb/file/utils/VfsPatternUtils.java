package com.cqrcb.file.utils;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;

public class VfsPatternUtils extends VfsUtils {

    public static Object getVisitorAttributes() {
        return doGetVisitorAttributes();
    }

    public static String getPath(Object resource) {
        String path = doGetPath(resource);
        return path != null ? path : "";
    }

    public static Object findRoot(URL url) throws IOException {
        return getRoot(url);
    }

    public static void visit(Object resource, InvocationHandler visitor) throws IOException {
        Object visitorProxy = Proxy.newProxyInstance(VIRTUAL_FILE_VISITOR_INTERFACE.getClassLoader(), new Class[]{VIRTUAL_FILE_VISITOR_INTERFACE}, visitor);
        invokeVfsMethod(VIRTUAL_FILE_METHOD_VISIT, resource, new Object[]{visitorProxy});
    }
}
