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

import com.hx.eleven.ftpserver.ftplet.FtpException;
import com.hx.eleven.ftpserver.ftplet.UserManager;
import com.hx.eleven.ftpserver.usermanager.Md5PasswordEncryptor;
import com.hx.eleven.ftpserver.usermanager.PasswordEncryptor;

/**
 *
 *
 * @author
 */
public abstract class AbstractUserManager implements UserManager {

    /**
     * 用户名/用户唯一ID
     */
    public static final String ATTR_LOGIN = "userid";
    /**
     *  用户密码
     */
    public static final String ATTR_PASSWORD = "user_password";
    /**
     * 用户根目录/用户主目录
     */
    public static final String ATTR_HOME = "home_directory";
    /**
     * 写入权限
     */
    public static final String ATTR_WRITE_PERM = "write_permission";
    /**
     *
     */
    public static final String ATTR_ENABLE = "enable_flag";
    /**
     * 空闲时间
     */
    public static final String ATTR_MAX_IDLE_TIME = "idle_time";
    /**
     * 上传最大字节数/上传速度
     */
    public static final String ATTR_MAX_UPLOAD_RATE = "upload_rate";
    /**
     * 下载最大字节数/下载速度
     */
    public static final String ATTR_MAX_DOWNLOAD_RATE = "download_rate";
    /**
     * 最大登录数
     */
    public static final String ATTR_MAX_LOGIN_NUMBER = "max_current_login_number";
    /**
     * 每个IP的最大登录数
     */
    public static final String ATTR_MAX_LOGIN_PER_IP = "max_current_login_per_ip";
    /**
     * 管理员用户
     */
    private final String adminName = "admin";
    /**
     * 密码加密处理工具
     */
    private final PasswordEncryptor passwordEncryptor;

    public AbstractUserManager() {
        this(new Md5PasswordEncryptor());
    }

    public AbstractUserManager(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    /**
     * 获取管理员用户名
     * @return
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * 验证用户是否是管理员用户
     * @param username 用户名
     * @return
     * @throws FtpException
     */
    public boolean isAdmin(String username) throws FtpException {
        if (username == null) {
            return false;
        }
        return adminName.equals(username);
    }


    /**
     * 获取密码加密工具
     * @return
     */
    public PasswordEncryptor getPasswordEncryptor() {
        return passwordEncryptor;
    }
}
