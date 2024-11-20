package hx.nine.eleven.file.io;

import hx.nine.eleven.file.utils.Assert;
import hx.nine.eleven.file.utils.ClassUtils;
import hx.nine.eleven.file.utils.ResourceUtils;
import hx.nine.eleven.file.utils.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultResourceLoader implements ResourceLoader {

    private ClassLoader classLoader;
    private final Set<ProtocolResolver> protocolResolvers = new LinkedHashSet(4);
    private final Map<Class<?>, Map<Resource, ?>> resourceCaches = new ConcurrentHashMap(4);

    public DefaultResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public DefaultResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
    }

    public void addProtocolResolver(ProtocolResolver resolver) {
        Assert.notNull(resolver, "ProtocolResolver must not be null");
        this.protocolResolvers.add(resolver);
    }

    public Collection<ProtocolResolver> getProtocolResolvers() {
        return this.protocolResolvers;
    }

    public <T> Map<Resource, T> getResourceCache(Class<T> valueType) {
        return (Map)this.resourceCaches.computeIfAbsent(valueType, (key) -> {
            return new ConcurrentHashMap();
        });
    }

    public void clearResourceCaches() {
        this.resourceCaches.clear();
    }

    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        Iterator iterator = this.getProtocolResolvers().iterator();

        Resource resource;
        do {
            if (!iterator.hasNext()) {
                if (location.startsWith("/")) {
                    return this.getResourceByPath(location);
                }

                if (location.startsWith("classpath:")) {
                    return new ClassPathResource(location.substring("classpath:".length()), this.getClassLoader());
                }

                try {
                    URL url = new URL(location);
                    return (Resource)(ResourceUtils.isFileURL(url) ? new FileUrlResource(url) : new UrlResource(url));
                } catch (MalformedURLException var5) {
                    return this.getResourceByPath(location);
                }
            }

            ProtocolResolver protocolResolver = (ProtocolResolver)iterator.next();
            resource = protocolResolver.resolve(location, this);
        } while(resource == null);

        return resource;
    }

    protected Resource getResourceByPath(String path) {
        return new ClassPathContextResource(path, this.getClassLoader());
    }

    protected static class ClassPathContextResource extends ClassPathResource implements ContextResource {
        public ClassPathContextResource(String path, ClassLoader classLoader) {
            super(path, classLoader);
        }

        public String getPathWithinContext() {
            return this.getPath();
        }

        public Resource createRelative(String relativePath) {
            String pathToUse = StringUtils.applyRelativePath(this.getPath(), relativePath);
            return new ClassPathContextResource(pathToUse, this.getClassLoader());
        }
    }
}
