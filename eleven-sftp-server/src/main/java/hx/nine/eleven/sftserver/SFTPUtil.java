package hx.nine.eleven.sftserver;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

public class SFTPUtil {
    private static final Logger logger = LoggerFactory.getLogger(SFTPUtil.class);

    private Session session;

    private ChannelSftp channelSftp;

    /**
     * 登录
     */
    public boolean login(String hostname, int port, String username, String password) {
        try {
            JSch jSch = new JSch();
            // 根据用户名，IP地址获取一个session对象。
            session = jSch.getSession(username, hostname, port);
            session.setPassword(password);
            // 避免第一次连接时需要输入yes确认
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            logger.info("成功连接服务器");
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            logger.info("成功连接服务器");
            return true;
        } catch (JSchException e) {
            logger.error("SFTPUtil login file error:", e);
        }
        return false;
    }

    /**
     * 退出
     */
    public void logout() {
        if (channelSftp != null && channelSftp.isConnected()) {
            try {
                channelSftp.disconnect();
                logger.info("成功退出 SFTP 服务器");
            } catch (Exception e) {
                logger.error("退出SFTP服务器异常: ", e);
            } finally {
                try {
                    channelSftp.disconnect(); // 关闭FTP服务器的连接
                } catch (Exception e) {
                    logger.error("关闭SFTP服务器的连接异常: ", e);
                }
            }
        }
        if (session != null && session.isConnected()) {
            try {
                session.disconnect();
                logger.info("成功退出 session 会话");
            } catch (Exception e) {
                logger.error("退出 session 会话异常: ", e);
            } finally {
                try {
                    session.disconnect(); // 关闭FTP服务器的连接
                } catch (Exception e) {
                    logger.error("关闭 session 会话的连接异常: ", e);
                }
            }
        }
    }

    /**
     * 判断是否连接
     */
    public boolean isConnected() {
        return channelSftp != null && channelSftp.isConnected();
    }

    /**
     * 上传文件
     * 采用默认的传输模式：OVERWRITE
     *
     * @param src 输入流（文件）
     * @param dst 上传路径
     * @param fileName 上传文件名
     * @throws SftpException
     */
    public boolean upLoadFile(InputStream src, String dst, String fileName) throws SftpException {
        boolean isSuccess = false;
        try {
            if (createDir(dst)) {
                channelSftp.put(src, fileName);
                isSuccess = true;
            }
        } catch (SftpException e) {
            logger.error(fileName + "文件上传异常", e);
        }
        return isSuccess;
    }

    /**
     * 创建一个文件目录
     *
     * @param createPath 路径
     * @return
     */
    public boolean createDir(String createPath) {
        boolean isSuccess = false;
        try {
            if (isDirExist(createPath)) {
                channelSftp.cd(createPath);
                return true;
            }
            String[] pathArray = createPath.split("/");
            StringBuilder filePath = new StringBuilder("/");
            for (String path : pathArray) {
                if (path.isEmpty()) {
                    continue;
                }
                filePath.append(path).append("/");
                if (isDirExist(filePath.toString())) {
                    channelSftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    channelSftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    channelSftp.cd(filePath.toString());
                }
            }
            channelSftp.cd(createPath);
            isSuccess = true;
        } catch (SftpException e) {
            logger.error("目录创建异常！", e);
        }
        return isSuccess;
    }

    /**
     * 判断目录是否存在
     *
     * @param directory 路径
     * @return
     */
    public boolean isDirExist(String directory) {
        boolean isSuccess = false;
        try {
            SftpATTRS sftpATTRS = channelSftp.lstat(directory);
            isSuccess = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            logger.info("SFTPUtil path has error:{}", directory);
            logger.error("SFTPUtil path has error: ", e);
            if (e.getMessage().equalsIgnoreCase("no such file")) {
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    /**
     * 重命名指定文件或目录
     */
    public boolean rename(String oldPath, String newPath) {
        boolean isSuccess = false;
        try {
            channelSftp.rename(oldPath, newPath);
            isSuccess = true;
        } catch (SftpException e) {
            logger.error("重命名指定文件或目录异常", e);
        }
        return isSuccess;
    }

    /**
     * 列出指定目录下的所有文件和子目录。
     */
    public Vector ls(String path) {
        try {
            Vector vector = channelSftp.ls(path);
            return vector;
        } catch (SftpException e) {
            logger.error("列出指定目录下的所有文件和子目录。", e);
        }
        return null;
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(String directory, String deleteFile) {
        boolean isSuccess = false;
        try {
            channelSftp.cd(directory);
            channelSftp.rm(deleteFile);
            isSuccess = true;
        } catch (SftpException e) {
            logger.error("删除文件失败", e);
        }
        return isSuccess;
    }

    /**
     * 下载文件
     *
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 下载到本地路径
     */
    public boolean download(String directory, String downloadFile, String saveFile) {
        boolean isSuccess = false;
        try {
            channelSftp.cd(directory);
            File file = new File(saveFile);
            channelSftp.get(downloadFile, new FileOutputStream(file));
            isSuccess = true;
        } catch (SftpException e) {
            logger.error("下载文件失败", e);
        } catch (FileNotFoundException e) {
            logger.error("下载文件失败", e);
        }
        return isSuccess;
    }

    /**
     * 输出指定文件流
     */
    public InputStream getFile(String path) {
        try {
            InputStream inputStream = channelSftp.get(path);
            return inputStream;
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载文件，新
     */
    public InputStream downloadFile(String remoteFileName, String remoteFilePath) {
        InputStream input = null;
        try {
            if (!isDirExist(remoteFilePath)) {
                logger.info("SFTPUtil not changeWorkingDirectory.filename:{},Path:{}", remoteFileName, remoteFilePath);
                logout();
                return input;
            }
            logger.info("SFTPUtil Info filename:{},Path:{}", remoteFileName, remoteFilePath);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            channelSftp.get(remoteFileName, outputStream);
            input = new ByteArrayInputStream(outputStream.toByteArray());
            outputStream.close();
            logger.info("file download. filename:{},Path:{}", remoteFileName, remoteFilePath);
        } catch (Exception e) {
            logger.error("SFTPUtil download file error:", e);
            logout();
        }
        return input;
    }

    /**
     * 验证sftp文件是否有效(判断文件属性的日期)
     */
    public String validateSourceData(String fileName, String remoteFilePath, Date nowTime) throws IOException, SftpException {
        Date temp = null;
        if (!isDirExist(remoteFilePath)) {
            logout();
            return "尝试切换SFTP路径失败, 文件路径: " + remoteFilePath + fileName;
        }
        Vector vector = channelSftp.ls(remoteFilePath);
        for (int i = 0; i < vector.capacity(); i++) {
            ChannelSftp.LsEntry data = (ChannelSftp.LsEntry) vector.get(i);
            if (data.getFilename().equalsIgnoreCase(fileName)) {
                temp = new Date(data.getAttrs().getMTime() * 1000L);
                //logger.info("时间: timeStamp:{},nowTime:{}",temp,nowTime);
                if (temp.getTime() == nowTime.getTime()) {
                    return "SFTP文件没有更新";
                }
                nowTime.setTime(temp.getTime());
                break;
            }
        }
        //logout();
        return null;
    }


    /**
     * IP
     */
    public String getSFTPHost() {
        return session.getHost();
    }

    /**
     * 端口
     */
    public int getSFTPPort() {
        return session.getPort();
    }
}
