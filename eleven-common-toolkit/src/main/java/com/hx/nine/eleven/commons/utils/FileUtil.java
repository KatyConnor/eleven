package com.hx.nine.eleven.commons.utils;

import com.hx.nine.eleven.commons.CopyOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wml
 * @Description
 * @data 2022-05-20
 */
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    public static final CopyOptions DEFAULT_OPTIONS = new CopyOptions();

    public static boolean move(String from, String to){
       return move(from,to,DEFAULT_OPTIONS);
    }

    /**
     * 线程同步处理文件
     * @param from     源文件地址
     * @param to	   目标文件文件地址
     * @param options  参数
     * @return
     */
    public static boolean move(String from, String to,CopyOptions options){
        isCreateTargetPath(to);
        Set<CopyOption> copyOptionSet = toCopyOptionSet(options);
        CopyOption[] copyOptions = copyOptionSet.toArray(new CopyOption[0]);
        try {
            Files.move(Paths.get(from), Paths.get(to), copyOptions);
            return true;
        } catch (IOException e) {
            LOGGER.info("文件从[{}]移动到[{}]失败",from,to);
        }
        return false;
    }

    /**
     * 异步处理删掉文件
     * 需保证后续没有线程获取当前文件
     * @param path
     * @return
     */
    public static boolean delete(Path path){
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            LOGGER.error(StringUtils.format("文件[{}]删除失败， ",path.toUri().getPath()),e);
        }
        return false;
    }

    /**
     * 文件内容 Unicode 转 中文
     * @param path
     */
    public static void unicodeCH(String path) throws IOException {
        // 读取文件转换，写入临时文件
        File file = new File(path);
        String tempPath = path.substring(0,path.lastIndexOf("/")+1)+"temp"+path.substring(path.lastIndexOf("/")+1);
        File fileTemp = new File(tempPath);
        FileInputStream inStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        String str = null;
        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp)));
        while ((str = br.readLine()) != null) {
            String result = StringUtils.unicodeToCH(str);
            bwr.write(result);
            bwr.newLine();
        }
        bwr.flush();
        bwr.close();
        br.close();
        // 临时文件写入源文件
        File fileread = new File(tempPath);
        File filewrite = new File(path);
        BufferedReader bread = new BufferedReader(new InputStreamReader(new FileInputStream(fileread), "UTF-8"));
        BufferedWriter bwrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filewrite)));
        String str1 = null;
        while ((str1 = bread.readLine()) != null) {
            bwrite.write(str1);
            bwrite.newLine();
        }
        bwrite.flush();
        bwrite.close();
        bread.close();
        fileTemp.delete();
        fileread.delete();
    }

    /**
     * 分析文件路径是否存在，不存在直接创建目录，如果文件存在则先删除文件
     * @param targetFile
     */
    private static void isCreateTargetPath(String targetFile){
        Path tFilePath = Paths.get(targetFile);
        if (Files.exists(tFilePath)) {
            try {
                Files.delete(tFilePath);
            } catch (IOException e) {
                LOGGER.error(StringUtils.format("删除已存在的目标文件[{}]失败",targetFile),e);
            }
        }

        int lastSpile = targetFile.lastIndexOf(SystemUtils.osName() == 1 ? "\\" : "/");
        String targetFilePath = targetFile.substring(0, lastSpile) + (SystemUtils.osName() == 1 ? "\\" : "/");
        Path targetPath = Paths.get(targetFilePath);
        if (!Files.exists(targetPath)) {
            try {
                Files.createDirectory(targetPath);
            } catch (IOException e) {
                LOGGER.error(StringUtils.format("文件目录[{}]创建失败",targetFilePath),e);
            }
        }
    }

    private static Set<CopyOption> toCopyOptionSet(CopyOptions copyOptions) {
        Set<CopyOption> copyOptionSet = new HashSet<>();
        if (copyOptions.isReplaceExisting()) copyOptionSet.add(StandardCopyOption.REPLACE_EXISTING);
        if (copyOptions.isCopyAttributes()) copyOptionSet.add(StandardCopyOption.COPY_ATTRIBUTES);
        if (copyOptions.isAtomicMove()) copyOptionSet.add(StandardCopyOption.ATOMIC_MOVE);
        if (copyOptions.isNofollowLinks()) copyOptionSet.add(LinkOption.NOFOLLOW_LINKS);
        return copyOptionSet;
    }
}
