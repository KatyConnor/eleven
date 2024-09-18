package com.cqrcb.file.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

public abstract class VfsUtils {

    private static final String VFS3_PKG = "org.jboss.vfs.";
    private static final String VFS_NAME = "VFS";
    private static final Method VFS_METHOD_GET_ROOT_URL;
    private static final Method VFS_METHOD_GET_ROOT_URI;
    private static final Method VIRTUAL_FILE_METHOD_EXISTS;
    private static final Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;
    private static final Method VIRTUAL_FILE_METHOD_GET_SIZE;
    private static final Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
    private static final Method VIRTUAL_FILE_METHOD_TO_URL;
    private static final Method VIRTUAL_FILE_METHOD_TO_URI;
    private static final Method VIRTUAL_FILE_METHOD_GET_NAME;
    private static final Method VIRTUAL_FILE_METHOD_GET_PATH_NAME;
    private static final Method VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE;
    private static final Method VIRTUAL_FILE_METHOD_GET_CHILD;
    protected static final Class<?> VIRTUAL_FILE_VISITOR_INTERFACE;
    protected static final Method VIRTUAL_FILE_METHOD_VISIT;
    private static final Field VISITOR_ATTRIBUTES_FIELD_RECURSE;

    public VfsUtils() {
    }

    public static Object invokeVfsMethod(Method method,Object target, Object... args) throws IOException {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException ex) {
            Throwable targetEx = ex.getTargetException();
            if (targetEx instanceof IOException) {
                throw (IOException)targetEx;
            }

            ReflectionUtils.handleInvocationTargetException(ex);
        } catch (Exception ex) {
            ReflectionUtils.handleReflectionException(ex);
        }

        throw new IllegalStateException("Invalid code path reached");
    }

    public static boolean exists(Object vfsResource) {
        try {
            return (Boolean)invokeVfsMethod(VIRTUAL_FILE_METHOD_EXISTS, vfsResource);
        } catch (IOException ioException) {
            return false;
        }
    }

    public static boolean isReadable(Object vfsResource) {
        try {
            return (Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource) > 0L;
        } catch (IOException ioException) {
            return false;
        }
    }

    public static long getSize(Object vfsResource) throws IOException {
        return (Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource);
    }

    public static long getLastModified(Object vfsResource) throws IOException {
        return (Long)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED, vfsResource);
    }

    public static InputStream getInputStream(Object vfsResource) throws IOException {
        return (InputStream)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_INPUT_STREAM, vfsResource);
    }

    public static URL getURL(Object vfsResource) throws IOException {
        return (URL)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URL, vfsResource);
    }

    public static URI getURI(Object vfsResource) throws IOException {
        return (URI)invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URI, vfsResource);
    }

    public static String getName(Object vfsResource) {
        try {
            return (String)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_NAME, vfsResource);
        } catch (IOException ioException) {
            throw new IllegalStateException("Cannot get resource name", ioException);
        }
    }

    public static Object getRelative(URL url) throws IOException {
        return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, (Object)null, url);
    }

    public static Object getChild(Object vfsResource, String path) throws IOException {
        return invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_CHILD, vfsResource, path);
    }

    public static File getFile(Object vfsResource) throws IOException {
        return (File)invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE, vfsResource);
    }

    public static Object getRoot(URI url) throws IOException {
        return invokeVfsMethod(VFS_METHOD_GET_ROOT_URI, (Object)null, url);
    }

    public static Object getRoot(URL url) throws IOException {
        return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, (Object)null, url);
    }

    public static Object doGetVisitorAttributes() {
        return ReflectionUtils.getField(VISITOR_ATTRIBUTES_FIELD_RECURSE, (Object)null);
    }

    public static String doGetPath(Object resource) {
        return (String) ReflectionUtils.invokeMethod(VIRTUAL_FILE_METHOD_GET_PATH_NAME, resource);
    }

    static {
        ClassLoader loader = VfsUtils.class.getClassLoader();

        try {
            Class<?> vfsClass = loader.loadClass("org.jboss.vfs.VFS");
            VFS_METHOD_GET_ROOT_URL = vfsClass.getMethod("getChild", URL.class);
            VFS_METHOD_GET_ROOT_URI = vfsClass.getMethod("getChild", URI.class);
            Class<?> virtualFile = loader.loadClass("org.jboss.vfs.VirtualFile");
            VIRTUAL_FILE_METHOD_EXISTS = virtualFile.getMethod("exists");
            VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = virtualFile.getMethod("openStream");
            VIRTUAL_FILE_METHOD_GET_SIZE = virtualFile.getMethod("getSize");
            VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = virtualFile.getMethod("getLastModified");
            VIRTUAL_FILE_METHOD_TO_URI = virtualFile.getMethod("toURI");
            VIRTUAL_FILE_METHOD_TO_URL = virtualFile.getMethod("toURL");
            VIRTUAL_FILE_METHOD_GET_NAME = virtualFile.getMethod("getName");
            VIRTUAL_FILE_METHOD_GET_PATH_NAME = virtualFile.getMethod("getPathName");
            VIRTUAL_FILE_METHOD_GET_PHYSICAL_FILE = virtualFile.getMethod("getPhysicalFile");
            VIRTUAL_FILE_METHOD_GET_CHILD = virtualFile.getMethod("getChild", String.class);
            VIRTUAL_FILE_VISITOR_INTERFACE = loader.loadClass("org.jboss.vfs.VirtualFileVisitor");
            VIRTUAL_FILE_METHOD_VISIT = virtualFile.getMethod("visit", VIRTUAL_FILE_VISITOR_INTERFACE);
            Class<?> visitorAttributesClass = loader.loadClass("org.jboss.vfs.VisitorAttributes");
            VISITOR_ATTRIBUTES_FIELD_RECURSE = visitorAttributesClass.getField("RECURSE");
        } catch (Throwable throwable) {
            throw new IllegalStateException("Could not detect JBoss VFS infrastructure", throwable);
        }
    }
}
