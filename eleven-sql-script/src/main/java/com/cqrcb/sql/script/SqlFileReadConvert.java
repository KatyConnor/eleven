package com.cqrcb.sql.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wml
 * @Description
 * @data 2022-08-11
 */
public class SqlFileReadConvert {

    /**
     * T_DT_CASH.sql
     * @param path
     * @throws IOException
     */
    public static void read_T_DT_CASH(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, Integer> countMap = new HashMap<>();
        int commit = 1;
        int count = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }

            // 行结尾
            if (str.endsWith(");")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[10]);
                String id = null;
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        BigDecimal bd = new BigDecimal(values[0]);
                        if (countMap.containsKey(bd.toPlainString())) {
                            String bdFid = bd.toPlainString();
                            bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                            countMap.put(bdFid, countMap.get(bdFid) + 1);
                        } else {
                            countMap.put(bd.toPlainString(), count);
                        }
                        id = bd.toPlainString();
                        sb.append("values ('").append(bd.toPlainString()).append("',");
                        continue;
                    }

                    if (i == values.length - 1) {
                        if (!"null".equals(values[i].trim())){
                            sb.append("'").append(id).append("'").append(");");
                        }else {
                            sb.append(values[i]).append(");");
                        }
                    } else if (i == values.length - 2){
                        if (!"null".equals(values[i].trim())){
                            sb.append("'").append(id).append("'").append(",");
                        }else {
                            sb.append(values[i]).append(",");
                        }
                    } else {
                        sb.append(values[i]).append(",");
                    }
                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_DT_CASH_DET.sql
     * @param path
     * @throws IOException
     */
    public static void read_T_DT_CASH_DET(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, Integer> countMap = new HashMap<>();
        int commit = 1;
        int count = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }

            // 行结尾
            if (str.endsWith(";")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[10]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        BigDecimal bd = new BigDecimal(values[0]);
                        if (countMap.containsKey(bd.toPlainString())) {
                            String bdFid = bd.toPlainString();
                            bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                            countMap.put(bdFid, countMap.get(bdFid) + 1);
                        } else {
                            countMap.put(bd.toPlainString(), count);
                        }
                        sb.append("values ('").append(bd.toPlainString()).append("',");
                        continue;
                    }

                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }
                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_DT_COMMAND.sql
     * @param path
     * @throws IOException
     */
    public static void read_T_DT_COMMAND(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("1.183269496104E19", new String[]{"11832694961040033602"});
        keyMap.put("2.20630145911016E19", new String[]{"22063014591101635001"});
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.20630151101017E19", new String[]{"22063015110101700000"});
        keyMap.put("2.20630152719017E19", new String[]{"22063015271901700000"});
        Set<String> keySet = new HashSet<>();
        Map<String, Integer> countMap = new HashMap<>();
        int commit = 1;
        int count = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }

            // 行结尾
            if (str.endsWith(";")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[10]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        BigDecimal bd = new BigDecimal(values[0]);
                        if (countMap.containsKey(bd.toPlainString())) {
                            String bdFid = bd.toPlainString();
                            bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                            countMap.put(bdFid, countMap.get(bdFid) + 1);
                        } else {
                            countMap.put(bd.toPlainString(), count);
                        }
                        sb.append("values ('").append(bd.toPlainString()).append("',");
                        continue;
                    }

                    if (i == 1) {
                        if (keySet.contains(sbKey.toString())) {
                            sb.append("'").append(keyMap.get(values[i].trim())[1]).append("',");
                        } else {
                            keySet.add(sbKey.toString());
                            sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                        }
                        continue;
                    }
                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }
                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_FA_BALANCE.sql
     * @param path
     * @throws IOException
     */
    public static void readT_FA_BALANCE(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.20628095916018E19", new String[]{"22062809591601826194", "22062809591601826190"});
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        Set<String> keySet = new HashSet<>();
        int commit = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(";")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[2]).append(values[3]).append(values[4]).append(values[5]).append(values[6]).append(values[7]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        sb.append("values (");
                    }

                    if (i == 4) {
                        if (keySet.contains(sbKey.toString())) {
                            sb.append("'").append(keyMap.get(values[4].trim())[1]).append("',");
                        } else {
                            keySet.add(sbKey.toString());
                            sb.append("'").append(keyMap.get(values[4].trim())[0]).append("',");
                        }
                        continue;
                    }
                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }

                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_FA_PRICE.sql
     * @param path
     * @throws IOException
     */
    public static void readT_FA_PRICE(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        Map<String, Integer> countMap = new HashMap<>();
        int count = 1;
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.20628095916018E19", new String[]{"22062809591601826194", "22062809591601826190"});
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        Set<String> keySet = new HashSet<>();
        int commit = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(";")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[2]).append(values[3]).append(values[4]).append(values[5]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        BigDecimal bd = new BigDecimal(values[0]);
                        if (countMap.containsKey(bd.toPlainString())) {
                            String bdFid = bd.toPlainString();
                            bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                            countMap.put(bdFid, countMap.get(bdFid) + 1);
                        } else {
                            countMap.put(bd.toPlainString(), count);
                        }
                        sb.append("values ('").append(bd.toPlainString()).append("',");
                        continue;
                    }

                    if (i == 1) {
                        try{
                            if (keySet.contains(sbKey.toString())) {
                                if ("2.2051814314201E19".equals(values[i].trim())){
                                    sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                                }else {
                                    sb.append("'").append(keyMap.get(values[i].trim())[1]).append("',");
                                }
                            } else {
                                keySet.add(sbKey.toString());
                                sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                            }
                            continue;
                        }catch (ArrayIndexOutOfBoundsException exception){
                            System.out.println(keyMap.get(values[i].trim()));
                            System.out.println(sbKey.toString());
                        }

                    }
                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }

                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_FA_VALUATION.sql
     * @param path
     * @throws IOException
     */
    public static void readT_FA_VALUATION(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.20628095916018E19", new String[]{"22062809591601826194", "22062809591601826190"});
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        Set<String> keySet = new HashSet<>();
        int commit = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(";")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[2]).append(values[3]).append(values[4]).append(values[18]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        sb.append("values (");
                    }

                    if (i == 1) {
                        if (keySet.contains(sbKey.toString())) {
                            sb.append("'").append(keyMap.get(values[i].trim())[1]).append("',");
                        } else {
                            keySet.add(sbKey.toString());
                            sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                        }
                        continue;
                    }
                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }

                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_FA_VAT_INCOME.sql
     * @param path
     * @throws IOException
     */
    public static void readT_FA_VAT_INCOME(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        Map<String, Integer> countMap = new HashMap<>();
        int count = 1;
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.20628095916018E19", new String[]{"22062809591601826194", "22062809591601826190"});
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        Set<String> keySet = new HashSet<>();
        int commit = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(";")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[2]).append(values[3]).append(values[4]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        BigDecimal bd = new BigDecimal(values[0]);
                        if (countMap.containsKey(bd.toPlainString())) {
                            String bdFid = bd.toPlainString();
                            bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                            countMap.put(bdFid, countMap.get(bdFid) + 1);
                        } else {
                            countMap.put(bd.toPlainString(), count);
                        }
                        sb.append("values ('").append(bd.toPlainString()).append("',");
                        continue;
                    }

                    if (i == 1) {
                        try{
                            if (keySet.contains(sbKey.toString())) {
                                if ("2.2051814314201E19".equals(values[i].trim())){
                                    sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                                }else {
                                    sb.append("'").append(keyMap.get(values[i].trim())[1]).append("',");
                                }
                            } else {
                                keySet.add(sbKey.toString());
                                sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                            }
                            continue;
                        }catch (ArrayIndexOutOfBoundsException exception){
                            System.out.println(keyMap.get(values[i].trim()));
                            System.out.println(sbKey.toString());
                        }

                    }
                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }

                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_FA_VAT_REDUCE.sql
     * @param path
     * @throws IOException
     */
    public static void read_T_FA_VAT_REDUCE(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        Map<String, Integer> countMap = new HashMap<>();
        int count = 1;
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.20628095916018E19", new String[]{"22062809591601826194", "22062809591601826190"});
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        Set<String> keySet = new HashSet<>();
        int commit = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(";")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[2]).append(values[3]).append(values[4]).append(values[5]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        BigDecimal bd = new BigDecimal(values[0]);
                        if (countMap.containsKey(bd.toPlainString())) {
                            String bdFid = bd.toPlainString();
                            bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                            countMap.put(bdFid, countMap.get(bdFid) + 1);
                        } else {
                            countMap.put(bd.toPlainString(), count);
                        }
                        sb.append("values ('").append(bd.toPlainString()).append("',");
                        continue;
                    }

                    if (i == 1) {
                        try{
                            if (keySet.contains(sbKey.toString())) {
                                if ("2.2051814314201E19".equals(values[i].trim())){
                                    sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                                }else {
                                    sb.append("'").append(keyMap.get(values[i].trim())[1]).append("',");
                                }
                            } else {
                                keySet.add(sbKey.toString());
                                sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                            }
                            continue;
                        }catch (ArrayIndexOutOfBoundsException exception){
                            System.out.println(keyMap.get(values[i].trim()));
                            System.out.println(sbKey.toString());
                        }

                    }
                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }

                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_FA_VOUCHER.sql
     * @param path
     * @throws IOException
     */
    public static void read_T_FA_VOUCHER(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        Map<String, Integer> countMap = new HashMap<>();
        int count = 1;
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.20628095916018E19", new String[]{"22062809591601826194", "22062809591601826190"});
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        Set<String> keySet = new HashSet<>();
        int commit = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(";")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[1]).append(values[5]).append(values[8]).append(values[9]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        BigDecimal bd = new BigDecimal(values[0]);
                        if (countMap.containsKey(bd.toPlainString())) {
                            String bdFid = bd.toPlainString();
                            bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                            countMap.put(bdFid, countMap.get(bdFid) + 1);
                        } else {
                            countMap.put(bd.toPlainString(), count);
                        }
                        sb.append("values ('").append(bd.toPlainString()).append("',");
                        continue;
                    }

                    if (i == 1) {
                        try{
                            if (keySet.contains(sbKey.toString())) {
                                if ("2.2051814314201E19".equals(values[i].trim())){
                                    sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                                }else {
                                    sb.append("'").append(keyMap.get(values[i].trim())[1]).append("',");
                                }
                            } else {
                                keySet.add(sbKey.toString());
                                sb.append("'").append(keyMap.get(values[i].trim())[0]).append("',");
                            }
                            continue;
                        }catch (ArrayIndexOutOfBoundsException exception){
                            System.out.println(keyMap.get(values[i].trim()));
                            System.out.println(sbKey.toString());
                        }

                    }
                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }

                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_IF_SITUATION.sql
     * @param path
     * @throws IOException
     */
    public static void read_T_IF_SITUATION(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        Map<String, Integer> countMap = new HashMap<>();
        int count = 1;
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        int commit = 1;
        StringBuilder sbline = new StringBuilder();
        boolean value = false;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            if (str.startsWith("values")){
                sbline.append(str);
                value = true;
                if (str.endsWith(";")) {
                    String tempStr = sbline.toString().trim();
                    String res = tempStr.substring(8, tempStr.length()-2);
                    String[] values = res.split(",");
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sbKey = new StringBuilder();
                    sbKey.append(values[1]).append(values[5]).append(values[8]).append(values[9]);
                    for (int i = 0; i < values.length; i++) {
                        if (i == 0) {
                            BigDecimal bd = new BigDecimal(values[0]);
                            if (countMap.containsKey(bd.toPlainString())) {
                                String bdFid = bd.toPlainString();
                                bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                                countMap.put(bdFid, countMap.get(bdFid) + 1);
                            } else {
                                countMap.put(bd.toPlainString(), count);
                            }
                            sb.append("values ('").append(bd.toPlainString()).append("',");
                            continue;
                        }

                        if (i == values.length - 1) {
                            sb.append(values[i]).append(");");
                        } else {
                            sb.append(values[i]).append(",");
                        }

                    }
                    bwr.write(sb.toString());
                    bwr.newLine();
                    if (commit % 1000 == 0) {
                        bwr.write("commit;");
                        bwr.newLine();
                        bwr.write("prompt " + commit);
                        bwr.newLine();
                    }
                    commit++;
                    sbline.delete(0,sbline.length());
                    value = false;
                }
                continue;
            }
            try{
                if (str.endsWith(";") && !"commit;".equals(str)) {
                    String tempStr = sbline.append(str).toString().trim();
                    String res = tempStr.substring(8, tempStr.length()-2);
                    String[] values = res.split(",");
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sbKey = new StringBuilder();
                    sbKey.append(values[1]).append(values[5]).append(values[8]).append(values[9]);
                    for (int i = 0; i < values.length; i++) {
                        if (i == 0) {
                            BigDecimal bd = new BigDecimal(values[0]);
                            if (countMap.containsKey(bd.toPlainString())) {
                                String bdFid = bd.toPlainString();
                                bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                                countMap.put(bdFid, countMap.get(bdFid) + 1);
                            } else {
                                countMap.put(bd.toPlainString(), count);
                            }
                            sb.append("values ('").append(bd.toPlainString()).append("',");
                            continue;
                        }

                        if (i == values.length - 1) {
                            sb.append(values[i]).append(");");
                        } else {
                            sb.append(values[i]).append(",");
                        }

                    }
                    bwr.write(sb.toString());
                    bwr.newLine();
                    if (commit % 1000 == 0) {
                        bwr.write("commit;");
                        bwr.newLine();
                        bwr.write("prompt " + commit);
                        bwr.newLine();
                    }
                    commit++;
                    sbline.delete(0,sbline.length());
                    value = false;
                    continue;
                }

                if (!value){
                    bwr.write(str);
                    bwr.newLine();
                }
            }catch (StringIndexOutOfBoundsException ex){
                ex.printStackTrace();
                System.out.println(sbline.toString());
                break;
            }

            if (value){
                sbline.append(str);
            }

        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_MT_RESULT.sql
     *
     * @param path
     * @throws IOException
     */
    public static void read_T_MT_RESULT(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        Map<String, Integer> countMap = new HashMap<>();
        int count = 1;
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        int commit = 1;

        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(");")) {
                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length()-2);
                String[] values = res.split(",");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        BigDecimal bd = new BigDecimal(values[0]);
                        if (countMap.containsKey(bd.toPlainString())) {
                            String bdFid = bd.toPlainString();
                            bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                            countMap.put(bdFid, countMap.get(bdFid) + 1);
                        } else {
                            countMap.put(bd.toPlainString(), count);
                        }
                        sb.append("values ('").append(bd.toPlainString()).append("',");
                        continue;
                    }

                    if (i == values.length - 1) {
                        sb.append(values[i]).append(");");
                    } else {
                        sb.append(values[i]).append(",");
                    }

                }
                bwr.write(sb.toString());
                bwr.newLine();
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                    bwr.write("prompt " + commit);
                    bwr.newLine();
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("prompt " + (commit - 1));
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

}
