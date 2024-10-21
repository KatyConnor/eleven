package com.hx.nine.eleven.web.ftp.jsch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @auth wml
 * @date 2024/10/21
 */
public class SftpTool {

	public static final Logger logger = LoggerFactory.getLogger(SftpTool.class);

	private String host;

	private String username;

	private int port;

	public SftpTool(String host, String username, int port){
		this.host = host;
		this.username = username;
		this.port = port;
	}

	/**
	 * 登陆sftp
	 *
	 * @param authTypeMode 认证方式
	 * @return 登陆信息
	 */
	public Map<String,Object> loginIn(AuthTypeMode authTypeMode){
		//解决JSch日志打印问题
		JSch.setLogger(new SettleJschLogPrint());
		try {
			ChannelSftp sftp = null;
			Session session = null;

			JSch jsch = new JSch();
			logger.info("获取SFTP服务器连接username:{},host:{},port:{}",username,host,port);
			session = jsch.getSession(username,host,port);
			logger.info("连接成功建立");
			if (AuthTypeEnum.RSA.getCode().equals(authTypeMode.getAuthType())){
				jsch.addIdentity(authTypeMode.getAuthValue(),"");
			}else {
				session.setPassword(authTypeMode.getAuthValue());
			}
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking","no");
			sshConfig.put("PreferredAuthentications","publickey,gssapi-with-mic,keyboard-interactive,password");
			session.setConfig(sshConfig);
			session.connect();
			logger.info("用户" + username + "成功登陆");
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;

			HashMap<String,Object> loginInfo = new HashMap<>();
			loginInfo.put("sftp",sftp);
			loginInfo.put("session",session);
			return loginInfo;
		} catch (JSchException e) {
			throw new RuntimeException("user login SFTP server occur exception:" + e);
		}
	}

	/**
	 * 退出登陆
	 *
	 * @param sftp sftp对象
	 * @param session session对象
	 */
	public void loginOut(ChannelSftp sftp, Session session){
		try {
			if(null != sftp && sftp.isConnected()){
				sftp.disconnect();
			}
			if (null != session && session.isConnected()){
				session.disconnect();
			}
		} catch (Exception e) {
			logger.warn("用户退出SFTP服务器出现异常:" + e);
		}
	}

	/**
	 * 下载文件
	 *
	 * @param downloadFilePath 要下载的文件所在绝对路径
	 * @param downloadFileName 要下载的文件名(sftp服务器上的文件名)
	 * @param saveFile 文件存放位置（文件所在绝对路径）
	 * @param authTypeMode 用户认证方式
	 */
	public void download(String downloadFilePath, String downloadFileName, File saveFile, AuthTypeMode authTypeMode) throws Exception{
//		Assert.notNull(downloadFilePath,"download file absolute path is not null");
//		Assert.notNull(downloadFileName,"download file is not null");
//		Assert.notNull(saveFile,"save file location is not null");
//		Assert.notNull(authTypeMode,"auth type way is not null");

		OutputStream outputStream = null;
		ChannelSftp sftp = null;
		Session session = null;
		try {
			Map<String,Object> loginInfo = loginIn(authTypeMode);
			sftp = (ChannelSftp) loginInfo.get("sftp");
			session = (Session) loginInfo.get("session");

			logger.info("待下载文件地址为:" + downloadFilePath + ",文件名为:" + downloadFileName + ",认证方式:" + authTypeMode.getAuthValue());
			sftp.cd(downloadFilePath);
			sftp.ls(downloadFileName);
			outputStream = new FileOutputStream(saveFile);
			sftp.get(downloadFileName,outputStream);
			logger.info("文件下载完成！");

		} finally {
			if (null != outputStream){
				outputStream.close();
			}
			loginOut(sftp,session);
		}
	}

	/**
	 * 上传文件
	 *
	 * @param uploadPath 上传SFTP完整路径
	 * @param uploadFile 上传文件（完整路径）
	 * @param authTypeMode 认证方式
	 */
	public void upload(String uploadPath, String uploadFile, AuthTypeMode authTypeMode) throws Exception{
//		Assert.notNull(uploadPath,"upload path is not null");
//		Assert.notNull(uploadFile,"upload file is not null");
//		Assert.notNull(authTypeMode,"auth type way is not null");

		InputStream inputStream = null;
		ChannelSftp sftp = null;
		Session session = null;
		try {
			Map<String,Object> loginInfo = loginIn(authTypeMode);
			sftp = (ChannelSftp) loginInfo.get("sftp");
			session = (Session) loginInfo.get("session");

			logger.info("待上传文件为:" + uploadFile + ",上传SFTP服务器路径:" + uploadPath + ",认证方式:" + authTypeMode.getAuthValue());
			File file = new File(uploadFile);
			inputStream = new FileInputStream(file);
			try {
				sftp.cd(uploadPath);
			} catch (SftpException e) {
				logger.error("SFTP器服务存放文件路径不存在");
				throw new RuntimeException("upload path is not exist");
			}
			sftp.put(inputStream,file.getName());
			logger.info("上传文件成功！");
		} finally {
			if (null != inputStream){
				inputStream.close();
			}
			loginOut(sftp,session);
		}

	}

	/**
	 * authType = PASSWORD，authValue = password
	 * authType = rsa，authValue = rsa文件路径
	 */
	class AuthTypeMode{
		private String authType;

		private String authValue;

		AuthTypeMode(String authType, String authValue){
			this.authType = authType;
			this.authValue = authValue;
		}

		String getAuthType(){
			return authType;
		}

		String getAuthValue(){
			return authValue;
		}
	}

	/**
	 * 在slf4j日志框架里打印JSch日志
	 */
	class SettleJschLogPrint implements com.jcraft.jsch.Logger{

		@Override
		public boolean isEnabled(int i) {
			return true;
		}

		@Override
		public void log(int i, String s) {
			logger.info(s);
		}
	}

	enum AuthTypeEnum {

		PASSWORD("PASSWORD","密码认证"),

		RSA("RSA","rsa密钥认证");

		private String code;

		private String desc;

		AuthTypeEnum(String code, String desc){
			this.code = code;
			this.desc = desc;
		}

		public String getCode(){
			return this.code;
		}
	}

	public static void main(String[] args) throws Exception{
		//密码认证方式
		SftpTool sftpTool = new SftpTool("192.168.79.151","albert",22);
		//下载文件
		File saveFile = new File("/data/sftp/verifyfile_01.txt");
		sftpTool.download("/verifyfile","verifyfile_01.csv",saveFile,sftpTool.new AuthTypeMode(AuthTypeEnum.PASSWORD.getCode(),"alan123456"));
		//上传文件
		sftpTool.upload("/verifyfile","/data/sftp/verifyfile_01.txt",sftpTool.new AuthTypeMode(AuthTypeEnum.PASSWORD.getCode(),"alan123456"));

		//密钥认证方式下载文件
		SftpTool sftpToolTwo = new SftpTool("192.168.79.151","qingniu",22);
		File saveFileTwo = new File("/data/sftp/verifyfile_01.txt");
		sftpToolTwo.download("/verifyfile","verifyfile_01.csv",saveFileTwo,sftpToolTwo.new AuthTypeMode(AuthTypeEnum.RSA.getCode(),"/data/rsa/id_rsa_qingniu"));
		//密钥认证方式上传文件( 上传会失败Permission denied，因为qingniu这个用户没有写的权限 )
		sftpToolTwo.upload("/verifyfile","/data/sftp/verifyfile_01.txt",sftpToolTwo.new AuthTypeMode(AuthTypeEnum.RSA.getCode(),"/data/rsa/id_rsa_qingniu"));
	}

}
