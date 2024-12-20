package hx.nine.eleven.file.io;

import hx.nine.eleven.file.utils.Assert;
import hx.nine.eleven.file.utils.ClassUtils;
import hx.nine.eleven.file.utils.ObjectUtils;
import hx.nine.eleven.file.utils.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ClassPathResource extends AbstractFileResolvingResource {

    private final String path;

    private ClassLoader classLoader;

    private Class<?> clazz;

    public ClassPathResource(String path) {
        this(path, (ClassLoader)null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
       Assert.notNull(path, "Path must not be null");
        String pathToUse = StringUtils.cleanPath(path);
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }

        this.path = pathToUse;
        this.classLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }

    public ClassPathResource(String path, Class<?> clazz) {
        Assert.notNull(path, "Path must not be null");
        this.path = StringUtils.cleanPath(path);
        this.clazz = clazz;
    }

    /** @deprecated */
    @Deprecated
    protected ClassPathResource(String path, ClassLoader classLoader, Class<?> clazz) {
        this.path = StringUtils.cleanPath(path);
        this.classLoader = classLoader;
        this.clazz = clazz;
    }

    public final String getPath() {
        return this.path;
    }

    public final ClassLoader getClassLoader() {
        return this.clazz != null ? this.clazz.getClassLoader() : this.classLoader;
    }

    public boolean exists() {
        return this.resolveURL() != null;
    }

    protected URL resolveURL() {
        if (this.clazz != null) {
            return this.clazz.getResource(this.path);
        } else {
            return this.classLoader != null ? this.classLoader.getResource(this.path) : ClassLoader.getSystemResource(this.path);
        }
    }

    public InputStream getInputStream() throws IOException {
        InputStream is;
        if (this.clazz != null) {
            is = this.clazz.getResourceAsStream(this.path);
        } else if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(this.path);
        } else {
            is = ClassLoader.getSystemResourceAsStream(this.path);
        }

        if (is == null) {
            throw new FileNotFoundException(this.getDescription() + " cannot be opened because it does not exist");
        } else {
            return is;
        }
    }

    public URL getURL() throws IOException {
        URL url = this.resolveURL();
        if (url == null) {
            throw new FileNotFoundException(this.getDescription() + " cannot be resolved to URL because it does not exist");
        } else {
            return url;
        }
    }

    public Resource createRelative(String relativePath) {
        String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
        return this.clazz != null ? new ClassPathResource(pathToUse, this.clazz) : new ClassPathResource(pathToUse, this.classLoader);
    }

    public String getFilename() {
        return StringUtils.getFilename(this.path);
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder("class path resource [");
        String pathToUse = this.path;
        if (this.clazz != null && !pathToUse.startsWith("/")) {
            builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
            builder.append('/');
        }

        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }

        builder.append(pathToUse);
        builder.append(']');
        return builder.toString();
    }

    public boolean equals( Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof ClassPathResource)) {
            return false;
        } else {
            ClassPathResource otherRes = (ClassPathResource)other;
            return this.path.equals(otherRes.path) && ObjectUtils.nullSafeEquals(this.classLoader, otherRes.classLoader) && ObjectUtils.nullSafeEquals(this.clazz, otherRes.clazz);
        }
    }

    public int hashCode() {
        return this.path.hashCode();
    }
}
