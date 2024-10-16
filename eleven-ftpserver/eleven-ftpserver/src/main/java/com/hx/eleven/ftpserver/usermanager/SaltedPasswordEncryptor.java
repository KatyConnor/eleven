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

import java.security.SecureRandom;

import com.hx.eleven.ftpserver.util.EncryptUtils;
import com.hx.eleven.ftpserver.util.PasswordUtil;

/**
 *
 * 使用加密盐进行密码加密
 * @author wml
 */
public class SaltedPasswordEncryptor implements PasswordEncryptor {

    private SecureRandom rnd = new SecureRandom();
    private static final int MAX_SEED = 99999999;
    private static final int HASH_ITERATIONS = 1000;

    /**
     *  密码加密
     */
    public String encrypt(String password) {
        String seed = Integer.toString(rnd.nextInt(MAX_SEED));
        return encrypt(password, seed);
    }

    /**
     * 密码校验
     * @param passwordToCheck 传入被校验密码
     * @param storedPassword 数据库/应用内部存储密码
     * @return
     */
    public boolean matches(String passwordToCheck, String storedPassword) {
        if (storedPassword == null) {
            throw new NullPointerException("storedPassword can not be null");
        }
        if (passwordToCheck == null) {
            throw new NullPointerException("passwordToCheck can not be null");
        }
        int divider = storedPassword.indexOf(':');
        if (divider < 1) {
            throw new IllegalArgumentException("stored password does not contain salt");
        }
        String storedSalt = storedPassword.substring(0, divider);
        return PasswordUtil.secureCompareFast(encrypt(passwordToCheck, storedSalt).toLowerCase(), storedPassword.toLowerCase());
    }

    private String encrypt(String password, String salt) {
        String hash = salt + password;
        for (int i = 0; i < HASH_ITERATIONS; i++) {
            hash = EncryptUtils.encryptMD5(hash);
        }
        return salt + ":" + hash;
    }

}
