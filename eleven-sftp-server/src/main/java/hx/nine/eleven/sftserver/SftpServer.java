package hx.nine.eleven.sftserver;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.scp.server.ScpCommandFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellCommandFactory;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.sftp.server.SftpSubsystemFactory;

public class SftpServer {

    public static void main(String[] args) {
        SshServer sshd = SshServer.setUpDefaultServer();
        // 设置sftp绑定端口
        sshd.setPort(2990);
        // 设置密钥文件，不存在会自动创建
        SimpleGeneratorHostKeyProvider provider = new SimpleGeneratorHostKeyProvider(Paths.get("/persistent/home/wml/devcode/jihuoma"));
        provider.setAlgorithm("DSA");
        provider.setKeySize(1024);
        sshd.setKeyPairProvider(provider);
        SftpSubsystemFactory sftpSubsystemFactory = new SftpSubsystemFactory.Builder().build();
        sshd.setSubsystemFactories(Arrays.asList(sftpSubsystemFactory));
        // 用户名密码校验
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(String username, String password, ServerSession session) {
                return "nfms".equals(username) && "nfms".equals(password);
            }
        });
        // 设置sftp默认的访问目录
        sshd.setFileSystemFactory(new VirtualFileSystemFactory(Paths.get("/persistent/home/wml/devcode/devcode")));
        sshd.setCommandFactory(new ProcessShellCommandFactory());
        sshd.setShellFactory(new ProcessShellFactory());
        //启动ssh服务
        try {
            sshd.start();
            System.out.println("sftp启动完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
