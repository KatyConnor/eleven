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

import com.hx.eleven.ftpserver.util.EncryptUtils;
import com.hx.eleven.ftpserver.util.PasswordUtil;

/**
 *  采用 MD5 方式处理密码
 */
public class Md5PasswordEncryptor implements PasswordEncryptor {

    /**
     * 密码进行 MD5 加密，返回加密后的密码
     * @param password 明文密码
     * @return         加密后密码
     */
    public String encrypt(String password) {
        return EncryptUtils.encryptMD5(password);
    }

    /**
     * 密码校验
     * @param passwordToCheck 传入被校验密码
     * @param storedPassword  数据库/应用内部存储密码
     * @return
     */
    public boolean matches(String passwordToCheck, String storedPassword) {
        if (storedPassword == null) {
            throw new NullPointerException("storedPassword can not be null");
        }
        if (passwordToCheck == null) {
            throw new NullPointerException("passwordToCheck can not be null");
        }
        return PasswordUtil.secureCompareFast(encrypt(passwordToCheck).toLowerCase(), storedPassword.toLowerCase());
    }
}
