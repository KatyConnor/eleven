package hx.nine.eleven.file.io;

import hx.nine.eleven.file.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public abstract class AbstractResource implements Resource {

    private static Logger logger = LoggerFactory.getLogger(AbstractResource.class);

    public AbstractResource() {
    }

    public boolean exists() {
        if (this.isFile()) {
            try {
                return this.getFile().exists();
            } catch (IOException var4) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not retrieve File for existence check of " + this.getDescription(), var4);
                }
            }
        }

        try {
            this.getInputStream().close();
            return true;
        } catch (Throwable var3) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not retrieve InputStream for existence check of " + this.getDescription(), var3);
            }

            return false;
        }
    }

    public boolean isReadable() {
        return this.exists();
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isFile() {
        return false;
    }

    public URL getURL() throws IOException {
        throw new FileNotFoundException(this.getDescription() + " cannot be resolved to URL");
    }

    public URI getURI() throws IOException {
        URL url = this.getURL();

        try {
            return ResourceUtils.toURI(url);
        } catch (URISyntaxException uriSyntaxException) {
            throw new NestedIOException("Invalid URI [" + url + "]", uriSyntaxException);
        }
    }

    public File getFile() throws IOException {
        throw new FileNotFoundException(this.getDescription() + " cannot be resolved to absolute file path");
    }

    public ReadableByteChannel readableChannel() throws IOException {
        return Channels.newChannel(this.getInputStream());
    }

    public long contentLength() throws IOException {
        InputStream is = this.getInputStream();
        long contentLength;
        try {
            long size = 0L;
            byte[] buf = new byte[256];
            while (true) {
                int read;
                if ((read = is.read(buf)) == -1) {
                    contentLength = size;
                    break;
                }
                size += read;
            }
        } finally {
            try {
                if (is != null){
                    is.close();
                }
            } catch (IOException ioException) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not close content-length InputStream for " + this.getDescription(), ioException);
                }
            }
        }
        return contentLength;
    }

    public long lastModified() throws IOException {
        File fileToCheck = this.getFileForLastModifiedCheck();
        long lastModified = fileToCheck.lastModified();
        if (lastModified == 0L && !fileToCheck.exists()) {
            throw new FileNotFoundException(this.getDescription() + " cannot be resolved in the file system for checking its last-modified timestamp");
        } else {
            return lastModified;
        }
    }

    protected File getFileForLastModifiedCheck() throws IOException {
        return this.getFile();
    }

    public Resource createRelative(String relativePath) throws IOException {
        throw new FileNotFoundException("Cannot create a relative resource for " + this.getDescription());
    }

    public String getFilename() {
        return null;
    }

    public boolean equals(Object other) {
        return this == other || other instanceof Resource && ((Resource) other).getDescription().equals(this.getDescription());
    }

    public int hashCode() {
        return this.getDescription().hashCode();
    }

    public String toString() {
        return this.getDescription();
    }
}
