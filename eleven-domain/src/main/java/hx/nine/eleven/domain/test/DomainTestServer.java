package hx.nine.eleven.domain.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wml
 * @Description
 * @data 2022-06-14
 */
public class DomainTestServer {

//    @Autowired
//    private DomainExecuteService domainExecuteService;
//
//    public ResponseEntity init(){
//        BaseDTO dto = new BaseDTO();
//        return domainExecuteService.excute(ResponseEntity.class,dto,(res,o,factory) ->{
//            DomainDO domainDO = factory.create(o, DomainDO.class);
//            return null;
//        });
//    }

    public static void main(String[] args) throws ParseException, IOException {

        // 0 0 0 * * ? *  | 0 */5 * * * ? * | 0 0 */1 * * ? *
        // 22071311384001200128
        // 22071217110401699840
//        Date nextValidTime = new CronExpression("0 0 7 * * ? *").getNextValidTimeAfter(new Date());
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nextValidTime));
//        BigDecimal bd = new BigDecimal("2.20630151101017E19");
//        DecimalFormat sdf = new DecimalFormat("0");
//        BigDecimal bd = new BigDecimal("2.2051814572701E19");
//        System.out.println(bd.toPlainString());

//        unicodeCH("D:\\yss-2022\\T_FA_BALANCE.sql");
//        readFile("D:\\yss-2022\\T_DT_COMMAND.sql");











//        String s = "D:\\yss-2022\\T_MT_RESULT.sql);";
//        String s1 = s.substring(1,s.length()-2);
//        System.out.println(s1);
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
        while ((str = br.readLine()) != null) {
            if ("".equals(str.trim())) {
                continue;
            }
            if (str.startsWith("values")){
//                sbline.append(str);
//                value = true;
                if (str.endsWith(";")) {
                    String tempStr = str.trim();
                    String res = tempStr.substring(8, tempStr.length() -2);
                    String[] values = res.split(",");
                StringBuilder sbstr = new StringBuilder();
//                sbstr.append(values[1]).append(values[10]);//.append(values[18]);
                    //.append(values[6]).append(values[7]).append(values[8]).append(values[9]).append(values[10]);
//                keySet.add(sbstr.toString());
                    keySet.add(values[1]);
//                    sbline.delete(0,sbline.length());
//                    value = false;
                }
                continue;
            }
//            try{
//                if (str.endsWith(";") && !"commit;".equals(str)) {
//                    String tempStr = sbline.append(str).toString().trim();
//                    String res = tempStr.substring(8, tempStr.indexOf(");"));
//                    String[] values = res.split(",");
////                StringBuilder sbstr = new StringBuilder();
////                sbstr.append(values[1]).append(values[5]).append(values[8]).append(values[9]);//.append(values[18]);
//                    //.append(values[6]).append(values[7]).append(values[8]).append(values[9]).append(values[10]);
////                keySet.add(sbstr.toString());
//                    keySet.add(values[1]);
//                    sbline.delete(0,sbline.length());
//                    value = false;
//                }
//            }catch (StringIndexOutOfBoundsException ex){
//                ex.printStackTrace();
//                System.out.println(sbline.toString());
//                break;
//            }
//
//            if (value){
//                sbline.append(str);
//            }

        }
        System.out.println(keySet.size());
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     *
     * @param path
     * @throws IOException
     */
//    public static void unicodeCH(String path) throws IOException {
//        // 读取文件转换，写入临时文件
//        File file = new File(path);
//        String tempPath = path.substring(0, path.lastIndexOf("\\") + 1) + "temp-" + path.substring(path.lastIndexOf("\\") + 1);
//        File fileTemp = new File(tempPath);
//        FileInputStream inStream = new FileInputStream(file);
//        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
//        String str = null;
//        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
////        Map<String, Integer> keyMap = new HashMap<>();
//        Map<String, String[]> keyMap = new HashMap<>();
//        keyMap.put("2.20628095916018E19", new String[]{"22062809591601826194", "22062809591601826190"});
//        keyMap.put("2.2051814572701E19", new String[]{"22051814572701002011", "22051814572701002004"});
//        keyMap.put("2.2051814314201E19", new String[]{"22051814314201002000"});
//        Set<String> keySet = new HashSet<>();
//        int count = 1;
//        int commit = 1;
//        while ((str = br.readLine()) != null) {
//            if ("".equals(str.trim())) {
//                continue;
//            }
//            if (str.endsWith(";")) {
//                if (str.contains("2.")) {
//                    String tempStr = str.trim();
//                    String res = tempStr.substring(8, tempStr.length() -2);
//                    String[] values = res.split(",");
//                    StringBuilder sb = new StringBuilder();
//                    String fid = null;
//                    for (int i = 0; i < values.length; i++) {
//                        if (i == 0) {
//                            BigDecimal bd = new BigDecimal(values[0]);
//                            if (keyMap.containsKey(bd.toPlainString())) {
//                                String bdFid = bd.toPlainString();
//                                bd = bd.add(new BigDecimal(String.valueOf(keyMap.get(bdFid))));
//                                keyMap.put(bdFid, keyMap.get(bdFid) + 1);
//                            } else {
//                                keyMap.put(bd.toPlainString(), count);
//                            }
////                            fid = "'"+bd.toPlainString()+"'";
//                            sb.append("values ('").append(bd.toPlainString()).append("',");
//                            continue;
//                        }
//
////                        if ((i == values.length - 1 || i == values.length - 2) && !values[i].trim().equals("null")){
////                            sb.append(fid);
////                            continue;
////                        }
//                        sb.append(values[i]).append(",");
//                    }
//                    bwr.write(sb.toString());
//                    bwr.newLine();
//                }
//                if (commit % 1000 == 0) {
//                    bwr.write("commit;");
//                    bwr.newLine();
//                    bwr.write("prompt " + commit);
//                    bwr.newLine();
//                }
//                commit++;
//            } else {
//                bwr.write(str);
//                bwr.newLine();
//            }
//        }
//        bwr.write("commit;");
//        bwr.newLine();
//        bwr.write("prompt " + (commit - 1));
//        bwr.newLine();
//        bwr.flush();
//        bwr.close();
//        br.close();
//        // 临时文件写入源文件
////        File fileread = new File(tempPath);
////        File filewrite = new File(path);
////        BufferedReader bread = new BufferedReader(new InputStreamReader(new FileInputStream(fileread), "UTF-8"));
////        BufferedWriter bwrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filewrite)));
////        String str1 = null;
////        while ((str1 = bread.readLine()) != null) {
////            bwrite.write(str1);
////            bwrite.newLine();
////        }
////        bwrite.flush();
////        bwrite.close();
////        bread.close();
////        fileTemp.delete();
////        fileread.delete();
//    }
}
