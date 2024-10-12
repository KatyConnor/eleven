package com.hx.nine.eleven.web.ftp;

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;

import java.io.IOException;

/**
 *  实现scp命令
 * @auth wml
 * @date 2024/10/9
 */
public class ScpCommandFactory implements CommandFactory {
	@Override
	public Command createCommand(ChannelSession channelSession, String s) throws IOException {
		return null;
	}
}
