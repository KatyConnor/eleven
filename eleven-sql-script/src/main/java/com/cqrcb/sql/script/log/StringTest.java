package com.cqrcb.sql.script.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @author wml
 * @Description
 * @data 2022-08-19
 */
public class StringTest {

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\yss-2022\\ysstg-20220822\\20220119.txt");
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        StringBuilder res = new StringBuilder();
        res.append("'");
        while ((str = br.readLine()) != null) {
            if (str.indexOf(".") != -1){
                String[] strs = str.split("\\.");
                for (int i = 0; i <strs.length; i++){
                    if (strs[i].matches("[0-9]+")){
                        res.append(strs[i]);
                    }else {
                        res.append("{").append(strs[i]).append("}");
                    }
                }
                res.append("','");
            }else {
                res.append(str).append("','");
            }
        }
        System.out.println(res.toString());
    }
}
