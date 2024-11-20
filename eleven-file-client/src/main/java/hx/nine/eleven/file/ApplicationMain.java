package hx.nine.eleven.file;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ApplicationMain {

    public static void main(String[] args) {
        try {
            FileUtils.build().readFile();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
