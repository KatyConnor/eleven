package com.cqrcb.sql.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author wml
 * @Description
 * @data 2022-08-11
 */
public class SqlConvertTest {

    public static void main(String[] args) throws IOException {
//        System.out.print("begin\nend");


        // 联测交易文件
//        SqlFileReadConvert.read_T_DT_CASH("D:\\yss-2022\\T_DT_CASH.sql");
//        SqlFileReadConvert.read_T_DT_CASH_DET("D:\\yss-2022\\T_DT_CASH_DET.sql");
//        SqlFileReadConvert.read_T_DT_COMMAND("D:\\yss-2022\\T_DT_COMMAND.sql");
        SqlFileReadConvert.readT_FA_BALANCE("D:\\yss-2022\\ysstg-20220822\\fenqu\\分区2\\T_FA_BALANCE.sql");
//        SqlFileReadConvert.readT_FA_PRICE("D:\\yss-2022\\T_FA_PRICE.sql");
//        SqlFileReadConvert.readT_FA_VALUATION("D:\\yss-2022\\T_FA_VALUATION.sql");
//        SqlFileReadConvert.readT_FA_VAT_INCOME("D:\\yss-2022\\T_FA_VAT_INCOME.sql");
//        SqlFileReadConvert.read_T_FA_VAT_REDUCE("D:\\yss-2022\\T_FA_VAT_REDUCE.sql");
//        SqlFileReadConvert.read_T_FA_VOUCHER("D:\\yss-2022\\T_FA_VOUCHER.sql");
//        SqlFileReadConvert.read_T_IF_SITUATION("D:\\yss-2022\\T_IF_SITUATION.sql");
//        SqlFileReadConvert.read_T_MT_RESULT("D:\\yss-2022\\T_MT_RESULT.sql");

//        readFile("D:\\yss-2022\\ysstg-20220822\\fenqu\\分区2\\T_FA_BALANCE.sql");
    }

    public static void readFile(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        Set<String> keySet = new HashSet<>();
//        StringBuilder sbline = new StringBuilder();
//        boolean value = false;
        int numNo = 0;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            if (str.startsWith("values")) {
//                sbline.append(str);
//                value = true;
                if (str.endsWith(";")) {
                    String tempStr = str.trim();
                    String res = tempStr.substring(8, tempStr.length() - 2);
                    String[] values = res.split(",");
                    StringBuilder sbstr = new StringBuilder();
                    Map<String, String[]> keyMap = new HashMap<>();
                    keyMap.put("2.20628095916018E19", new String[]{"22062809591601826194", "22062809591601826190"});
                    keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
                    keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
                    String ss = values[4].trim();
//                    if(!"null".equals(ss)){
                    if ("2.2051814314201E19".equals(ss)){
                        sbstr.append(values[1]).append(values[2]).append(values[3]).
                                append(keyMap.get(ss)[0]).append(values[5]).append(values[6]).append(values[7]);
                    }else {
                        String s0 = keyMap.get(ss)[0];
                        String s1 = keyMap.get(ss)[1];
                        if (keySet.contains(s0)){
                            sbstr.append(values[1]).append(values[2]).append(values[3]).
                                    append(s1).append(values[5]).append(values[6]).append(values[7]);
                        }else {
                            sbstr.append(values[1]).append(values[2]).append(values[3]).
                                    append(s0).append(values[5]).append(values[6]).append(values[7]);
//                            sbstr.append(values[10]).append(values[12]).append(s0);
                        }
                    }

//                    }else {


//                        sbstr.append(values[10]).append(values[12]).append(ss).append(numNo);
//                        numNo++;
//                        System.out.println(ss+numNo);
//                    }
//                    sbstr.append(values[10]).append(values[12]).append();//.append(values[3]).append(values[4]);//.append(values[18]);
                    //.append(values[6]).append(values[7]).append(values[8]).append(values[9]).append(values[10]);
                    keySet.add(sbstr.toString());
//                    keySet.add(values[4]);
//                    sbline.delete(0,sbline.length());
//                    value = false;
                }
                continue;
            }
        }
        System.out.println(keySet.size());
//        Iterator iterator = keySet.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }
    }
}
