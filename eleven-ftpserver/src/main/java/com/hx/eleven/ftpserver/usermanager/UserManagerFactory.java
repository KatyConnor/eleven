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

/**
 *
 * 创建 UserManager 工厂接口
 * @author wml
 */
public abstract class UserManagerFactory {

    /**
     * 密码默认是 MD5 加密算法
     */
    private PasswordEncryptor passwordEncryptor = new Md5PasswordEncryptor();
    /**
     * 创建一个 UserManager
     * @return
     */
    public abstract UserManager createUserManager();

    public PasswordEncryptor getPasswordEncryptor() {
        return passwordEncryptor;
    }

    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }
}