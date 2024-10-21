package com.hx.nine.eleven.web.ftp.jsch;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import javax.swing.*;

/**
 * @auth wml
 * @date 2024/10/21
 */
public class AES {

	public static void main(String[] arg){

		try{
			JSch jsch=new JSch();

			//jsch.setKnownHosts("/home/foo/.ssh/known_hosts");

			String host=null;
			if(arg.length>0){
				host=arg[0];
			}
			else{
				host= JOptionPane.showInputDialog("Enter username@hostname",
						System.getProperty("user.name")+
								"@localhost");
			}
			String user=host.substring(0, host.indexOf('@'));
			host=host.substring(host.indexOf('@')+1);

			Session session=jsch.getSession(user, host, 22);
			//session.setPassword("your password");

			// username and password will be given via UserInfo interface.
			UserInfo ui=new MyUserInfo("yunshouhu");
			session.setUserInfo(ui);

			session.setConfig("cipher.s2c", "aes128-cbc,3des-cbc,blowfish-cbc");
			session.setConfig("cipher.c2s", "aes128-cbc,3des-cbc,blowfish-cbc");
			session.setConfig("CheckCiphers", "aes128-cbc");

			session.connect();

			Channel channel=session.openChannel("shell");

			channel.setInputStream(System.in);
			channel.setOutputStream(System.out);

			channel.connect();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

		public MyUserInfo(String passwd) {
			super();
			this.passwd = passwd;
		}
		public String getPassword(){ return passwd; }
		public boolean promptYesNo(String str){

			return true;
		}

		String passwd;

		public String getPassphrase(){ return null; }
		public boolean promptPassphrase(String message){ return true; }
		public boolean promptPassword(String message){

			return true;
		}
		public void showMessage(String message){
			JOptionPane.showMessageDialog(null, message);
		}

		public String[] promptKeyboardInteractive(String destination,
												  String name,
												  String instruction,
												  String[] prompt,
												  boolean[] echo){


			return prompt;

		}
	}
}
