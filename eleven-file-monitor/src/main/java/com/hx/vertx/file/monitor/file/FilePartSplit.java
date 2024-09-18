package com.hx.vertx.file.monitor.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * 文件缓存
 */
public class FilePartSplit {

    private static String fileName = "D://备份//历史待恢复数据//sql//2.sql";
    private static String outFile = "D://备份//历史待恢复数据//sql//sql//3.sql";
    private static String outDir = "D://备份//历史待恢复数据//sql//2sql//";

    /**
     * 2G以上大文件读取
     *
     * @param inputFile, outputFile
     * @return void
     * @author wang****
     * @date: 2021/4/7 16:16
     */
    void largeFileIO(String inputFile, String outputFile) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            // 10M缓存
            BufferedReader in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);
            FileWriter fw = new FileWriter(outputFile);
            while (in.ready()) {
                String line = in.readLine();
                if (line.startsWith("INSERT")) {
                    fw.append(line + "\r");
                }
            }
            in.close();
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 大文件拆分
     *
     * @param inputFile, outputFile
     * @return void
     * @author wang****
     * @date: 2021/4/8 16:19
     */
    void largeFileSplit(String inputFile, String outputDir) {
        int j = 0;
        try {
            File out = new File(outputDir);
            if (!out.exists()) {
                out.mkdirs();
            }
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(inputFile)));
            // 20M缓存
            BufferedReader in = new BufferedReader(new InputStreamReader(bis, "utf-8"), 20 * 1024 * 1024);
            int i = 0;
            while (in.ready()) {
                long startTime = System.currentTimeMillis();
                ++i;
                FileWriter fw = new FileWriter(outputDir + i + ".sql");
                String line = null;
                //50万行一个文件拆分
                for (long lineCounter = 0; lineCounter < 500000 && (line = in.readLine()) != null; ++lineCounter) {
                    if (line.startsWith("INSERT INTO `cur_rec` VALUES")) {
                        fw.append(line + "\r");
                        ++j;
                    }
                }
                fw.flush();
                fw.close();
                long endTime = System.currentTimeMillis();
                System.out.println("第" + i + "个文件拆分成功，耗时：" + (float) (endTime - startTime) / 1000 + "秒");
            }
            in.close();
        } catch (IOException ex) {
            System.out.println("第" + j + "行数据拆分异常\r" + ex);
            ex.printStackTrace();
        }
    }

    /**
     * 快速把本地的文件片合并到一起FileChannel
     *
     * @throws IOException
     */
    private static void fastFileMerge() throws IOException {
        // 需要合并的文件所在的文件夹
        File chunkFolder = new File("d:/testPart/");
        // 合并后的文件
        File mergeFile = new File("d:/testPartMerge.zip");
        // 如果该文件夹
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        // 创建合并后的文件
        mergeFile.createNewFile();
        // 获取分块列表
        File[] fileArray = chunkFolder.listFiles();
        // 把文件转成集合并排序
        ArrayList<File> fileList = new ArrayList<>(Arrays.asList(fileArray));
        // 从小到大进行排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())){
                    return -1;
                }
                return 1;
            }
        });
        // 合并文件
        try(FileChannel fosChannel = new FileOutputStream(mergeFile).getChannel()) {
            for (File chunkFile : fileList) {
                try(FileChannel fisChannel =  new FileInputStream(chunkFile).getChannel()) {
                    fisChannel.transferTo(0, fisChannel.size(), fosChannel);
                }catch (IOException ex){
                    System.out.println("IOException" + ex);
                }
            }

        }catch (IOException ex){
            System.out.println("IOException" + ex);
        }
    }
}
