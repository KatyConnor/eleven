/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.hx.eleven.ftpserver.usermanager.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hx.eleven.ftpserver.auth.ConcurrentLoginPermission;
import com.hx.eleven.ftpserver.auth.TransferRatePermission;
import com.hx.eleven.ftpserver.auth.WritePermission;
import com.hx.eleven.ftpserver.db.DbUserManagerSQL;
import com.hx.eleven.ftpserver.db.FtpJdbcMapperFactory;
import com.hx.eleven.ftpserver.request.ConcurrentLoginRequest;
import com.hx.eleven.ftpserver.request.TransferRateRequest;
import com.hx.eleven.ftpserver.usermanager.AnonymousAuthentication;
import com.hx.eleven.ftpserver.usermanager.UsernamePasswordAuthentication;
import com.hx.eleven.ftpserver.util.StringUtils;
import com.hx.eleven.ftpserver.ftplet.Authentication;
import com.hx.eleven.ftpserver.ftplet.AuthenticationFailedException;
import com.hx.eleven.ftpserver.ftplet.Authority;
import com.hx.eleven.ftpserver.ftplet.FtpException;
import com.hx.eleven.ftpserver.ftplet.User;
import com.hx.eleven.ftpserver.request.WriteRequest;
import com.hx.eleven.ftpserver.usermanager.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过数据库管理FTP USER 用户
 */
public class DbUserManager extends AbstractUserManager {

	private final Logger LOG = LoggerFactory.getLogger(DbUserManager.class);
	private final Map<String, String> userField;

	public DbUserManager(PasswordEncryptor passwordEncryptor) {
		super(passwordEncryptor);
		userField = new HashMap<>();
		userField.put(ATTR_LOGIN, ATTR_LOGIN);
		userField.put(ATTR_PASSWORD, ATTR_PASSWORD);
		userField.put(ATTR_HOME, ATTR_HOME);
		userField.put(ATTR_ENABLE, ATTR_ENABLE);
		userField.put(ATTR_MAX_IDLE_TIME, ATTR_MAX_IDLE_TIME);
		userField.put(ATTR_WRITE_PERM, ATTR_WRITE_PERM);
		userField.put(ATTR_MAX_UPLOAD_RATE, ATTR_MAX_UPLOAD_RATE);
		userField.put(ATTR_MAX_DOWNLOAD_RATE, ATTR_MAX_DOWNLOAD_RATE);
		userField.put(ATTR_MAX_LOGIN_NUMBER, ATTR_MAX_LOGIN_NUMBER);
		userField.put(ATTR_MAX_LOGIN_PER_IP, ATTR_MAX_LOGIN_PER_IP);
	}

	@Override
	public void delete(String username) throws FtpException {
		if (!StringUtils.hasText(username)) {
			return;
		}
		boolean res = FtpJdbcMapperFactory.build().execute(DbUserManagerSQL.deleteUserStmt, new Object[]{username});
	}

	@Override
	public void save(User user) throws FtpException {
		if (user.getName() == null || user.getPassword() == null) {
			throw new NullPointerException("User " + user.getName() == null ? "name" : "password" + " is null");
		}

		String home = user.getHomeDirectory();
		if (home == null) {
			home = "/";
		}

		TransferRateRequest transferRateRequest = new TransferRateRequest();
		transferRateRequest = (TransferRateRequest) user.authorize(transferRateRequest);

		ConcurrentLoginRequest concurrentLoginRequest = new ConcurrentLoginRequest(0, 0);
		concurrentLoginRequest = (ConcurrentLoginRequest) user.authorize(concurrentLoginRequest);

		Object[] values = new Object[]{user.getName(), getPasswordEncryptor().encrypt(user.getPassword()), home,
				user.getEnabled(), user.authorize(new WriteRequest()) != null, user.getMaxIdleTime(),
				transferRateRequest.getMaxUploadRate(), transferRateRequest.getMaxDownloadRate(),
				concurrentLoginRequest.getMaxConcurrentLogins(), concurrentLoginRequest.getMaxConcurrentLoginsPerIP()};

		// 用户存在执行 update 更新，用户不存在执行 insert 插入数据
		if (!doesExist(user.getName())) {
			FtpJdbcMapperFactory.build().execute(DbUserManagerSQL.insertUserStmt, values);
		} else {
			FtpJdbcMapperFactory.build().executeUpdate(DbUserManagerSQL.updateUserStmt, values);
		}
	}

	@Override
	public User getUserByName(String name) throws FtpException {
		try {
			BaseUser user = this.selectUserByName(name);
			if (user != null) {
				// 重置密码，密码不能泄露API外查看
				user.setPassword(null);
			}
			return user;
		} catch (SQLException ex) {
			LOG.error("DbUserManager.getUserByName()", ex);
			throw new FtpException("DbUserManager.getUserByName()", ex);
		}
	}

	@Override
	public boolean doesExist(String username) throws FtpException {
		List<Map<String, Object>> res = FtpJdbcMapperFactory.build().
				executeQueryByColumn(DbUserManagerSQL.selectUserStmt, new Object[]{username}, userField);
		return res != null && res.size() > 0;
	}

	@Override
	public String[] getAllUserNames() throws FtpException {
			List<String> names = new ArrayList<>();
			List<Map<String, Object>> res = FtpJdbcMapperFactory.build().
					executeQueryByColumn(DbUserManagerSQL.selectAllStmt, null, userField);
			if (res == null || res.size() <= 0){
				return null;
			}
			res.stream().forEach(m->{
				names.add(m.get(ATTR_LOGIN).toString());
			});
			return names.toArray(new String[0]);
	}

	/**
	 *  用户登录密码验证，验证通过返回用户信息
	 */
	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
		if (authentication instanceof UsernamePasswordAuthentication) {
			UsernamePasswordAuthentication upauth = (UsernamePasswordAuthentication) authentication;

			String user = upauth.getUsername();
			String password = upauth.getPassword();

			if (user == null) {
				throw new AuthenticationFailedException("Authentication failed");
			}

			if (password == null) {
				password = "";
			}
			List<Map<String, Object>> res = FtpJdbcMapperFactory.build().
					executeQueryByColumn(DbUserManagerSQL.authenticateStmt, new Object[]{user}, userField);
			if (res != null && res.size() > 0) {
				try {
					Map<String, Object> resultMap = res.get(0);
					String storedPassword = resultMap.get(ATTR_PASSWORD).toString();
					if (getPasswordEncryptor().matches(password, storedPassword)) {
						return getUserByName(user);
					} else {
						throw new AuthenticationFailedException("Authentication failed");
					}
				} catch (FtpException e) {
					throw new AuthenticationFailedException("Authentication failed", e);
				}
			} else {
				throw new AuthenticationFailedException("Authentication failed");
			}
		} else if (authentication instanceof AnonymousAuthentication) {
			try {
				if (doesExist("anonymous")) {
					return getUserByName("anonymous");
				} else {
					throw new AuthenticationFailedException("Authentication failed");
				}
			} catch (AuthenticationFailedException e) {
				throw e;
			} catch (FtpException e) {
				throw new AuthenticationFailedException("Authentication failed", e);
			}
		} else {
			throw new IllegalArgumentException("Authentication not supported by this user manager");
		}
	}

	private BaseUser selectUserByName(String username) throws SQLException {
		List<Map<String, Object>> res = FtpJdbcMapperFactory.build().
				executeQueryByColumn(DbUserManagerSQL.selectUserStmt, new Object[]{username}, userField);

		if (res != null) {
			Map<String, Object> resultMap = res.get(0);
			BaseUser user = new BaseUser();
			user.setName(resultMap.get(ATTR_LOGIN).toString());
			user.setPassword(resultMap.get(ATTR_PASSWORD).toString());
			user.setHomeDirectory(resultMap.get(ATTR_HOME).toString());
			user.setMaxIdleTime(Integer.valueOf(resultMap.get(ATTR_MAX_IDLE_TIME).toString()));
			// 设置权限
			List<Authority> authorities = new ArrayList<>();
			boolean enableFlag = Boolean.valueOf(resultMap.get(ATTR_ENABLE).toString());
			user.setEnabled(enableFlag);
			if (enableFlag) {
				authorities.add(new WritePermission());
			}
			authorities.add(new ConcurrentLoginPermission(
					Integer.valueOf(resultMap.get(ATTR_MAX_LOGIN_NUMBER).toString()),
					Integer.valueOf(resultMap.get(ATTR_MAX_LOGIN_PER_IP).toString())));
			authorities.add(new TransferRatePermission(
					Integer.valueOf(resultMap.get(ATTR_MAX_DOWNLOAD_RATE).toString()),
					Integer.valueOf(resultMap.get(ATTR_MAX_UPLOAD_RATE).toString())));
			user.setAuthorities(authorities);
			return user;
		}
		return null;
	}

	/**
	 * 在SQL语句中嵌入的转义字符串
	 */
	private String escapeString(String input) {
		if (input == null) {
			return input;
		}

		StringBuilder valBuf = new StringBuilder(input);
		for (int i = 0; i < valBuf.length(); i++) {
			char ch = valBuf.charAt(i);
			if (ch == '\'' || ch == '\\' || ch == '$' || ch == '^' || ch == '['
					|| ch == ']' || ch == '{' || ch == '}') {
				valBuf.insert(i, '\\');
				i++;
			}
		}
		return valBuf.toString();
	}
}