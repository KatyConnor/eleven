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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.hx.eleven.ftpserver.auth.ConcurrentLoginPermission;
import com.hx.eleven.ftpserver.auth.TransferRatePermission;
import com.hx.eleven.ftpserver.request.ConcurrentLoginRequest;
import com.hx.eleven.ftpserver.usermanager.UsernamePasswordAuthentication;
import com.hx.eleven.ftpserver.util.BaseProperties;
import com.hx.eleven.ftpserver.util.IoUtils;
import com.hx.eleven.ftpserver.FtpServerConfigurationException;
import com.hx.eleven.ftpserver.auth.WritePermission;
import com.hx.eleven.ftpserver.ftplet.Authentication;
import com.hx.eleven.ftpserver.ftplet.AuthenticationFailedException;
import com.hx.eleven.ftpserver.ftplet.Authority;
import com.hx.eleven.ftpserver.ftplet.FtpException;
import com.hx.eleven.ftpserver.ftplet.User;
import com.hx.eleven.ftpserver.request.TransferRateRequest;
import com.hx.eleven.ftpserver.request.WriteRequest;
import com.hx.eleven.ftpserver.usermanager.AnonymousAuthentication;
import com.hx.eleven.ftpserver.usermanager.PasswordEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Internal class, do not use directly.</strong>
 * 
 * <p>Properties file based <code>UserManager</code> implementation. We use
 * <code>user.properties</code> file to store user data.</p>
 *
 * </p>The file will use the following properties for storing users:</p>
 * <table>
 * <tr>
 *      <th>Property</th>
 *      <th>Documentation</th>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.homedirectory</td>
 *      <td>Path to the home directory for the user, based on the file system implementation used</td>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.userpassword</td>
 *      <td>The password for the user. Can be in clear text, MD5 hash or salted SHA hash based on the 
 *              configuration on the user manager
 *      </td>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.enableflag</td>
 *      <td>true if the user is enabled, false otherwise</td>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.writepermission</td>
 *      <td>true if the user is allowed to upload files and create directories, false otherwise</td>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.idletime</td>
 *      <td>The number of seconds the user is allowed to be idle before disconnected. 
 *              0 disables the idle timeout
 *      </td>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.maxloginnumber</td>
 *      <td>The maximum number of concurrent logins by the user. 0 disables the check.</td>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.maxloginperip</td>
 *      <td>The maximum number of concurrent logins from the same IP address by the user. 0 disables the check.</td>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.uploadrate</td>
 *      <td>The maximum number of bytes per second the user is allowed to upload files. 0 disables the check.</td>
 * </tr>
 * <tr>
 *      <td>ftpserver.user.{username}.downloadrate</td>
 *      <td>The maximum number of bytes per second the user is allowed to download files. 0 disables the check.</td>
 * </tr>
 * </table>
 * 
 * <p>Example:</p>
 * <pre>
 * ftpserver.user.admin.homedirectory=/ftproot
 * ftpserver.user.admin.userpassword=admin
 * ftpserver.user.admin.enableflag=true
 * ftpserver.user.admin.writepermission=true
 * ftpserver.user.admin.idletime=0
 * ftpserver.user.admin.maxloginnumber=0
 * ftpserver.user.admin.maxloginperip=0
 * ftpserver.user.admin.uploadrate=0
 * ftpserver.user.admin.downloadrate=0
 * </pre>
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class PropertiesUserManager extends AbstractUserManager {

    private final Logger LOG = LoggerFactory.getLogger(PropertiesUserManager.class);

    // 属性key前缀
    private final static String PREFIX = "ftpServer.user.";
    // 缓存用户信息
    private BaseProperties userDataProp;
    // 属性文件地址
    private String userDataFile;

    public PropertiesUserManager(PasswordEncryptor passwordEncryptor, String userDataFile, String adminName) {
        super(adminName, passwordEncryptor);
        this.userDataProp = new BaseProperties();
        this.userDataFile = userDataFile;
        loadFromFile();
    }

    /**
     *  刷新配置文件内容
     */
    public void refresh() {
        synchronized (userDataProp) {
            if (this.userDataFile != null) {
                LOG.debug("Refreshing user manager using file: " + this.userDataFile);
                loadFromFile();
            }
        }
    }

    /**
     * 新增一个用户
     * @param usr
     * @throws FtpException
     */
    public synchronized void save(User usr) throws FtpException {
        // 用户非空
        if (usr.getName() == null) {
            throw new NullPointerException("User name is null.");
        }
        String thisPrefix = PREFIX + usr.getName() + '.';
        // set other properties
        userDataProp.setProperty(thisPrefix + ATTR_PASSWORD, getPassword(usr));
        String home = usr.getHomeDirectory();
        if (home == null) {
            home = "/";
        }
        userDataProp.setProperty(thisPrefix + ATTR_HOME, home);
        userDataProp.setProperty(thisPrefix + ATTR_ENABLE, usr.getEnabled());
        userDataProp.setProperty(thisPrefix + ATTR_WRITE_PERM, usr.authorize(new WriteRequest()) != null);
        userDataProp.setProperty(thisPrefix + ATTR_MAX_IDLE_TIME, usr.getMaxIdleTime());

        TransferRateRequest transferRateRequest = new TransferRateRequest();
        transferRateRequest = (TransferRateRequest) usr.authorize(transferRateRequest);

        if (transferRateRequest != null) {
            userDataProp.setProperty(thisPrefix + ATTR_MAX_UPLOAD_RATE, transferRateRequest.getMaxUploadRate());
            userDataProp.setProperty(thisPrefix + ATTR_MAX_DOWNLOAD_RATE, transferRateRequest.getMaxDownloadRate());
        } else {
            userDataProp.remove(thisPrefix + ATTR_MAX_UPLOAD_RATE);
            userDataProp.remove(thisPrefix + ATTR_MAX_DOWNLOAD_RATE);
        }

        // request that always will succeed
        ConcurrentLoginRequest concurrentLoginRequest = new ConcurrentLoginRequest(0, 0);
        concurrentLoginRequest = (ConcurrentLoginRequest) usr.authorize(concurrentLoginRequest);

        if (concurrentLoginRequest != null) {
            userDataProp.setProperty(thisPrefix + ATTR_MAX_LOGIN_NUMBER, concurrentLoginRequest.getMaxConcurrentLogins());
            userDataProp.setProperty(thisPrefix + ATTR_MAX_LOGIN_PER_IP, concurrentLoginRequest.getMaxConcurrentLoginsPerIP());
        } else {
            userDataProp.remove(thisPrefix + ATTR_MAX_LOGIN_NUMBER);
            userDataProp.remove(thisPrefix + ATTR_MAX_LOGIN_PER_IP);
        }
        saveUserData();
    }

    private void saveUserData() throws FtpException {
        if (userDataFile == null) {
            return;
        }

        File dir = new File(userDataFile);
        if (dir != null && !dir.exists() && !dir.mkdirs()) {
            String dirName = dir.getAbsolutePath();
            throw new FtpServerConfigurationException("Cannot create directory for user data file : " + dirName);
        }
        // save user data
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(userDataFile);
            userDataProp.store(fos, "Generated file - don't edit (please)");
        } catch (IOException ex) {
            LOG.error("Failed saving user data", ex);
            throw new FtpException("Failed saving user data", ex);
        } finally {
            IoUtils.close(fos);
        }
    }

    /**
     * 删除用户
     * @param usrName
     * @throws FtpException
     */
    public void delete(String usrName) throws FtpException {
        // remove entries from properties
        String thisPrefix = PREFIX + usrName + '.';
        Enumeration<?> propNames = userDataProp.propertyNames();
        ArrayList<String> remKeys = new ArrayList<>();
        while (propNames.hasMoreElements()) {
            String thisKey = propNames.nextElement().toString();
            if (thisKey.startsWith(thisPrefix)) {
                remKeys.add(thisKey);
            }
        }
        Iterator<String> remKeysIt = remKeys.iterator();
        while (remKeysIt.hasNext()) {
            userDataProp.remove(remKeysIt.next());
        }
        saveUserData();
    }

    /**
     * 获取密码
     * @param usr
     * @return
     */
    private String getPassword(User usr) {
        String name = usr.getName();
        String password = usr.getPassword();

        if (password != null) {
            password = getPasswordEncryptor().encrypt(password);
        } else {
            String blankPassword = getPasswordEncryptor().encrypt("");

            if (doesExist(name)) {
                String key = PREFIX + name + '.' + ATTR_PASSWORD;
                password = userDataProp.getProperty(key, blankPassword);
            } else {
                password = blankPassword;
            }
        }
        return password;
    }

    /**
     * 获取所有用户
     * @return
     */
    public String[] getAllUserNames() {
        // get all user names
        String suffix = '.' + ATTR_HOME;
        ArrayList<String> ulst = new ArrayList<>();
        Enumeration<?> allKeys = userDataProp.propertyNames();
        int prefixlen = PREFIX.length();
        int suffixlen = suffix.length();
        while (allKeys.hasMoreElements()) {
            String key = (String) allKeys.nextElement();
            if (key.endsWith(suffix)) {
                String name = key.substring(prefixlen);
                int endIndex = name.length() - suffixlen;
                name = name.substring(0, endIndex);
                ulst.add(name);
            }
        }

        Collections.sort(ulst);
        return ulst.toArray(new String[0]);
    }

    /**
     * 根据用户名获取用户基本信息
     * @param userName
     * @return
     */
    public User getUserByName(String userName) {
        if (!doesExist(userName)) {
            return null;
        }
        String baseKey = PREFIX + userName + '.';
        BaseUser user = new BaseUser();
        user.setName(userName);
        user.setEnabled(userDataProp.getBoolean(baseKey + ATTR_ENABLE, true));
        user.setHomeDirectory(userDataProp.getProperty(baseKey + ATTR_HOME, "/"));
        List<Authority> authorities = new ArrayList<>();
        if (userDataProp.getBoolean(baseKey + ATTR_WRITE_PERM, false)) {
            authorities.add(new WritePermission());
        }
        int maxLogin = userDataProp.getInteger(baseKey + ATTR_MAX_LOGIN_NUMBER, 0);
        int maxLoginPerIP = userDataProp.getInteger(baseKey + ATTR_MAX_LOGIN_PER_IP, 0);
        authorities.add(new ConcurrentLoginPermission(maxLogin, maxLoginPerIP));
        int uploadRate = userDataProp.getInteger(baseKey + ATTR_MAX_UPLOAD_RATE, 0);
        int downloadRate = userDataProp.getInteger(baseKey + ATTR_MAX_DOWNLOAD_RATE, 0);

        authorities.add(new TransferRatePermission(downloadRate, uploadRate));

        user.setAuthorities(authorities);

        user.setMaxIdleTime(userDataProp.getInteger(baseKey + ATTR_MAX_IDLE_TIME, 0));

        return user;
    }

    /**
     * 检查用户是否存在
     * @param name
     * @return
     */
    public boolean doesExist(String name) {
        String key = PREFIX + name + '.' + ATTR_HOME;
        return userDataProp.containsKey(key);
    }

    /**
     *  用户权限验证
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

            String storedPassword = userDataProp.getProperty(PREFIX + user + '.' + ATTR_PASSWORD);

            if (storedPassword == null) {
                throw new AuthenticationFailedException("Authentication failed，["+user+"]user does not exist");
            }

            if (getPasswordEncryptor().matches(password, storedPassword)) {
                return getUserByName(user);
            } else {
                throw new AuthenticationFailedException("Authentication failed");
            }

        } else if (authentication instanceof AnonymousAuthentication) {
            if (doesExist("anonymous")) {
                return getUserByName("anonymous");
            } else {
                throw new AuthenticationFailedException("Authentication failed");
            }
        } else {
            throw new IllegalArgumentException("Authentication not supported by this user manager");
        }
    }

    /**
     * 销毁缓存用户数据
     */
    public synchronized void dispose() {
        if (userDataProp != null) {
            userDataProp.clear();
            userDataProp = null;
        }
    }

    /**
     * 从文件中加载用户配置
     */
    private void loadFromFile() {
        try {
            if (this.userDataFile != null && this.userDataFile.length() > 0) {
                LOG.debug("File configured, will try loading");
                LOG.debug("File found on file system");
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(this.userDataFile);
                    this.userDataProp.load(fis);
                } finally {
                    IoUtils.close(fis);
                }
            }
        } catch (IOException e) {
            throw new FtpServerConfigurationException("Error loading user data file : " + userDataFile, e);
        }
    }

}
