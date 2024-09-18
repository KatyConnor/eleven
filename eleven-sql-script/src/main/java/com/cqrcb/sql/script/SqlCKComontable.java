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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author wml
 * @Description
 * @data 2022-08-12
 */
public class SqlCKComontable {

    public static void main(String[] args) throws IOException {
        readFile("D:\\yss-2022\\ysstg-20220822\\fenqu\\分区1\\T_CK_MSG_MESSAGE.sql");
//        read_T_CK_MSG_MESSAGE("D:\\yss-2022\\ysstg-20220822\\T_CK_MSG_MESSAGE.sql");
//        read_T_CK_VALUATION("D:\\yss-2022\\ysstg-20220822\\bx\\已处理\\T_CK_VALUATION.sql");
//        read_T_CK_BALANCE("D:\\yss-2022\\ysstg-20220822\\bx\\已处理\\T_CK_BALANCE.sql");
//        read_T_DI_CH_CHDQUOTE("D:\\yss-2022\\ysstg-20220822\\T_DI_CH_CHDQUOTE.sql");
//        read_T_DI_CH_CHDQUOTE("D:\\yss-2022\\ysstg-20220822\\T_DI_CH_FQUOTE.sql");
//        String col = "FMSGTYPE, FMSGCONTENT, FMSGINTIME, FMSGHANDLETIME, FMSGCLS, FANOMALY_IGNORE, FSTATUS, FLISTSTATUS, FMSGHANDLER, FUNDID, FREPORTTYPE, FBEGINDATE, FENDDATE, FDEPTCODE, FCERTID, FSERIALNO, FID, FMSGCOMMTYPE, FMSGSRCTYPE, FERRINFO, FSSCDESTAPPID, FSSCDESTUSERID, FPROCESSDESC, FSSCID, FHKSUM, FISLOCK, FPKGID, FCREATORID, FCREATETIME, FLASTEDITORID, FLASTEDITTIME, FCHECKERID, FCHECKED, FCHECKTIME, FDELETEUSERID, FMARKDELETETIME, FDELETED, FPRODUCT_ID, FHEAD, FCHARSET, FISSWALLOW, FSETCODE, FCHECKING_NUM, FLABELINFO, FTRUEDATA, FHKDATE, FZGDATA, FSENDTIME, FSFWB";
//        String[] cols = col.split(",");
//        for (int i = 0; i <cols.length; i++){
//            if ("FPRODUCT_ID".equals(cols[i].trim())){
//                System.out.println(i);
//            }
//        }


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
                    keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
                    keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
                    String ss = values[41].trim();
                    if(!"null".equals(ss)){
                        if ("2.2051814314201E19".equals(ss)){
                            if (keySet.contains(keyMap.get(ss)[0])){
                                sbstr.append(values[10]).append(values[12]).append(keyMap.get(ss)[0]);
                                System.out.println("1");
                            }else {
                                sbstr.append(values[10]).append(values[12]).append(keyMap.get(ss)[0]);
                            }
                        }else {
                            String s0 = keyMap.get(ss)[0];
                            String s1 = keyMap.get(ss)[1];
                            if (keySet.contains(s0)){
                                sbstr.append(values[10]).append(values[12]).append(s1);
                            }else {
                                sbstr.append(values[10]).append(values[12]).append(s0);
                            }
                        }

                    }else {
                        sbstr.append(values[10]).append(values[12]).append(ss).append(numNo);
                        numNo++;
                        System.out.println(ss+numNo);
                    }
//                    sbstr.append(values[10]).append(values[12]).append();//.append(values[3]).append(values[4]);//.append(values[18]);
                    //.append(values[6]).append(values[7]).append(values[8]).append(values[9]).append(values[10]);
                    keySet.add(sbstr.toString());
//                    keySet.add(values[19]);
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


    /**
     * T_FA_BALANCE.sql
     *
     * @param path
     * @throws IOException
     */
    public static void read_T_CK_MSG_MESSAGE(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        int commit = 1;
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }

            // 行结尾
            if (str.endsWith(";")) {
                StringBuilder sb = new StringBuilder();
                if (str.trim().startsWith("commit")) {
                    sb.append(str);
                    continue;
                }

                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length() - 2);
                String[] values = res.split(",");
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        sb.append("values (");
                    }

                    if (i == 41 && !"".equals(values[41].trim()) && !"null".equals(values[41].trim())) {
                        sb.append("'").append(keyMap.get(values[41].trim())[0]).append("',");
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
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_FA_BALANCE.sql
     *
     * @param path
     * @throws IOException
     */
    public static void read_T_CK_VALUATION(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        Map<String, Integer> countMap = new HashMap<>();
        Set<String> keySet = new HashSet<>();
        int commit = 1;
        int line = 1;
        int count = 1;
        while ((str = br.readLine()) != null) {
            if (line == 1) {
                bwr.write("begin");
                bwr.newLine();
                line++;
            }

            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(";")) {
                StringBuilder sb = new StringBuilder();
                if (str.trim().startsWith("commit")) {
                    sb.append(str);
                    continue;
                }

                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length() - 2);
                String[] values = res.split(",");

                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[0]).append(values[1]).append(values[2]).append(values[3]).append(values[4]).append(values[5]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        sb.append("values (");
                        BigDecimal bd = new BigDecimal(String.valueOf(values[i]));
                        if (keySet.contains(sbKey.toString())) {
                            if (countMap.containsKey(bd.toPlainString())) {
                                String bdFid = bd.toPlainString();
                                bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                                countMap.put(bdFid, countMap.get(bdFid) + 1);
                            } else {
                                countMap.put(bd.toPlainString(), count + 1);
                            }
                            sb.append("'").append(bd.toPlainString()).append("',");
                            continue;
                        } else {
                            countMap.put(bd.toPlainString(), count + 1);
                            sb.append("'").append(bd.toPlainString()).append("',");
                            continue;
                        }
                    }

                    if (i == 1) {
                        if (keySet.contains(sbKey.toString())) {
                            sb.append("'").append(keyMap.get(values[1].trim())[0]).append("',");
                        } else {
                            keySet.add(sbKey.toString());
                            if (values[1].trim().length() > 1) {
                                sb.append("'").append(keyMap.get(values[1].trim())[0]).append("',");
                            }
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
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("end;");
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_FA_BALANCE.sql
     *
     * @param path
     * @throws IOException
     */
    public static void read_T_CK_BALANCE(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        Map<String, String[]> keyMap = new HashMap<>();
        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
        Set<String> keySet = new HashSet<>();
        Map<String, Integer> countMap = new HashMap<>();
        int commit = 1;
        int line = 1;
        int count = 1;
        while ((str = br.readLine()) != null) {
            if (line == 1) {
                bwr.write("begin");
                bwr.newLine();
                line++;
            }

            if ("".equals(str.trim())) {
                continue;
            }
            // 行结尾
            if (str.endsWith(";")) {

                StringBuilder sb = new StringBuilder();
                if (str.trim().startsWith("commit")) {
                    sb.append(str);
                    continue;
                }

                String tempStr = str.trim();
                String res = tempStr.substring(8, tempStr.length() - 2);
                String[] values = res.split(",");

                StringBuilder sbKey = new StringBuilder();
                sbKey.append(values[0]).append(values[1]).append(values[2]).append(values[3]).append(values[4]);
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        sb.append("values (");
                        BigDecimal bd = new BigDecimal(String.valueOf(values[i]));
                        if (keySet.contains(sbKey.toString())) {
                            if (countMap.containsKey(bd.toPlainString())) {
                                String bdFid = bd.toPlainString();
                                bd = bd.add(new BigDecimal(String.valueOf(countMap.get(bdFid))));
                                countMap.put(bdFid, countMap.get(bdFid) + 1);
                            } else {
                                countMap.put(bd.toPlainString(), count + 1);
                            }
                            sb.append("'").append(bd.toPlainString()).append("',");
                            continue;
                        } else {
                            Integer keyint = countMap.get(bd.toPlainString());
                            if (null != keyint && keyint > 0) {
                                countMap.put(bd.toPlainString(), keyint + 1);
                            } else {
                                countMap.put(bd.toPlainString(), count + 1);
                            }
                            sb.append("'").append(bd.toPlainString()).append("',");
                        }
                        continue;
                    }

                    if (i == 1) {
                        if (keySet.contains(sbKey.toString())) {
                            if ("2.2051814314201E19".equals(values[1].trim())) {
                                sb.append("'").append(keyMap.get(values[1].trim())[0]).append("',");
                            } else {
                                sb.append("'").append(keyMap.get(values[1].trim())[1]).append("',");
                            }

                        } else {
                            keySet.add(sbKey.toString());
                            if (values[1].trim().length() > 1) {
                                sb.append("'").append(keyMap.get(values[1].trim())[0]).append("',");
                            }
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
                }
                commit++;
            } else {
                bwr.write(str);
                bwr.newLine();
            }
        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("end;");
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }

    /**
     * T_DI_CH_CHDQUOTE.sql
     *
     * @param path
     * @throws IOException
     */
    public static void read_T_DI_CH_CHDQUOTE(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        int line = 1;
        int commit = 1;
        while ((str = br.readLine()) != null) {
            if (line == 1) {
                bwr.write("begin");
                bwr.newLine();
                line++;
            }

            if ("".equals(str.trim())) {
                continue;
            }

            bwr.write(str);
            bwr.newLine();

            if (str.endsWith(";")) {
                if (commit % 1000 == 0) {
                    bwr.write("commit;");
                    bwr.newLine();
                }
                commit++;
            }

        }
        bwr.write("commit;");
        bwr.newLine();
        bwr.write("end;");
        bwr.newLine();
        bwr.flush();
        bwr.close();
        br.close();
    }
}
