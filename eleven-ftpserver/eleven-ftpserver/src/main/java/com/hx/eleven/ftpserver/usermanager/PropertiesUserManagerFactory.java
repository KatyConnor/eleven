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

package com.hx.eleven.ftpserver.usermanager;

import com.hx.eleven.ftpserver.ftplet.UserManager;
import com.hx.eleven.ftpserver.usermanager.impl.PropertiesUserManager;

/**
 * 文件配置用户
 *
 * @author
 */
public class PropertiesUserManagerFactory extends UserManagerFactory {

	/**
	 * 默认管理员用户
	 */
	private String adminName = "admin";
    /**
     * 用户信息文件地址
     */
	private String userDataFile;

	public UserManager createUserManager() {
		return new PropertiesUserManager(this.getPasswordEncryptor(), userDataFile, adminName);
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getUserDataFile() {
		return userDataFile;
	}

	public void setUserDataFile(String userDataFile) {
		this.userDataFile = userDataFile;
	}
}
