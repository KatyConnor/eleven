package hx.nine.eleven.file;

import hx.nine.eleven.file.io.CqrcbPathMatchingResourcePatternResolver;
import hx.nine.eleven.file.io.Resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static FileUtils build(){
        return new FileUtils();
    }

    public void readFile() throws IOException {
        CqrcbPathMatchingResourcePatternResolver patternResolver = new CqrcbPathMatchingResourcePatternResolver();
        Resource[] resource = patternResolver.getResources("classpath*:config/**/*.json");
        for (int i = 0; i < resource.length; i++) {
            InputStream inputStream = new BufferedInputStream(resource[i].getInputStream());
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String str1 = new String(bytes);
            System.out.println(str1);
        }
    }

}
