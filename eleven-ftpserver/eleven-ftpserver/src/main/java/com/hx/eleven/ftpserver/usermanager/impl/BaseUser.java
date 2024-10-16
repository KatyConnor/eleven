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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hx.eleven.ftpserver.ftplet.Authority;
import com.hx.eleven.ftpserver.ftplet.AuthorizationRequest;
import com.hx.eleven.ftpserver.ftplet.User;

/**
 * <strong>Internal class, do not use directly.</strong>
 * 
 * Generic user class. The user attributes are:
 * <ul>
 * <li>userid</li>
 * <li>userpassword</li>
 * <li>enableflag</li>
 * <li>homedirectory</li>
 * <li>writepermission</li>
 * <li>idletime</li>
 * <li>uploadrate</li>
 * <li>downloadrate</li>
 * </ul>
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */

public class BaseUser implements User {

    /**
     * 用户名
     */
    private String name = null;
    /**
     * 密码
     */
    private String password = null;
    /**
     * 最大空闲时间,单位：秒
     */
    private int maxIdleTimeSec = 0; // no limit
    /**
     * 用户主目录
     */
    private String homeDir = null;

    private boolean isEnabled = true;
    /**
     * 用户权限
     */
    private List<? extends Authority> authorities = new ArrayList<>();

    public BaseUser() {
    }

    public BaseUser(User user) {
        name = user.getName();
        password = user.getPassword();
        authorities = user.getAuthorities();
        maxIdleTimeSec = user.getMaxIdleTime();
        homeDir = user.getHomeDirectory();
        isEnabled = user.getEnabled();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * 返回不可变的权限集合
     * @return
     */
    public List<Authority> getAuthorities() {
        if (authorities != null) {
            return Collections.unmodifiableList(authorities);
        } else {
            return null;
        }
    }

    public void setAuthorities(List<Authority> authorities) {
        if (authorities != null) {
            this.authorities = Collections.unmodifiableList(authorities);
        } else {
            this.authorities = null;
        }
    }

    public int getMaxIdleTime() {
        return maxIdleTimeSec;
    }

    public void setMaxIdleTime(int idleSec) {
        this.maxIdleTimeSec = idleSec;
        if (this.maxIdleTimeSec < 0) {
            this.maxIdleTimeSec = 0;
        }
    }

    public boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enb) {
        isEnabled = enb;
    }

    public String getHomeDirectory() {
        return homeDir;
    }

    public void setHomeDirectory(String home) {
        homeDir = home;
    }

    @Override
    public String toString() {
        return name;
    }

    public AuthorizationRequest authorize(AuthorizationRequest request) {
        if(this.authorities == null) {
            return null;
        }
        
        boolean someoneCouldAuthorize = false;
        for (Authority authority : authorities) {
            if (authority.canAuthorize(request)) {
                someoneCouldAuthorize = true;
                request = authority.authorize(request);
                if (request == null) {
                    return null;
                }
            }
        }

        if (someoneCouldAuthorize) {
            return request;
        } else {
            return null;
        }
    }

    public List<Authority> getAuthorities(Class<? extends Authority> clazz) {
        List<Authority> selected = new ArrayList<>();
        for (Authority authority : authorities) {
            if (authority.getClass().equals(clazz)) {
                selected.add(authority);
            }
        }
        return selected;
    }
}
