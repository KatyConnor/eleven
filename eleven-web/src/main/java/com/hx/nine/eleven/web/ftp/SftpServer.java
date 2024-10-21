package com.hx.nine.eleven.web.ftp;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.InteractiveProcessShellFactory;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.subsystem.SubsystemFactory;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @auth wml
 * @date 2024/10/9
 */
public class SftpServer {

	public static void main(String[] args) {
		System.out.println("启动sftp服务");
	}

	private static SftpServer nfmsSftpServer = new SftpServer();

	public static SftpServer getInstance() {
		return nfmsSftpServer;
	}

	private SftpServer() {
		init();
	}

	private void init() {
		SshServer sshd = SshServer.setUpDefaultServer();
		// 设置sftp绑定端口
		sshd.setPort(2222);
		//设置默认的签名文件,如果文件不存在会创建
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("D:\\code")));//,"DSA", 1024
		//设置sftp子系统
		sshd.setSubsystemFactories(Arrays.<SubsystemFactory>asList(new SftpSubsystemFactory()));
//		sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(new SftpSubsystem.Factory()));
		// 用户名密码校验
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
			@Override
			public boolean authenticate(String username, String password, ServerSession session) {
				//假定用户名:usera, 密码:passa
				return "nfms".equals(username) && "nfms".equals(password);
			}
		});

		// 设置sftp默认的访问目录
		sshd.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get("D:\\")));
		sshd.setCommandFactory(new ScpCommandFactory());
		//设置ssh的shell环境
		sshd.setShellFactory(new ProcessShellFactory());
		sshd.setShellFactory(InteractiveProcessShellFactory.INSTANCE);
		//启动ssh服务
		try {
			sshd.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
