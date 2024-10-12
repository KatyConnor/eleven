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

package org.apache.ftpserver.ftplet;

/**
 * 用户管理接口
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface UserManager {

    /**
     * 通过用户名获取User
     *
     * @param username 用户名
     * @throws FtpException 当UserManager不能满足请求时。
     * @return 返回满足条件的 User
     *
     */
    User getUserByName(String username) throws FtpException;

    /**
     * 获取系统中的所有用户名
     *
     * @throws FtpException when the UserManager can't fulfill the request.
     * @return
     */
    String[] getAllUserNames() throws FtpException;

    /**
     * 根据用户名从系统中删除用户。
     * @param username 用户名  {@link User}
     *
     * @throws FtpException 当UserManager不能满足请求时。
     * @throws UnsupportedOperationException
     */
    void delete(String username) throws FtpException;

    /**
     * 保存用户。如果是新用户，则创建它，否则更新现有用户。
     *
     * @param user 要保存的用户
     * @throws FtpException
     * @throws UnsupportedOperationException
     */
    void save(User user) throws FtpException;

    /**
     *  检查用户是否存在
     * @param username 要检查的用户名
     * @return 存在：true，否则返回 false
     * @throws FtpException 
     */
    boolean doesExist(String username) throws FtpException;

    /**
     * 验证用户
     * @param authentication 验证用户身份信息 {@link Authentication}
     * @return 已验证的帐户
     * @throws AuthenticationFailedException 
     * @throws FtpException
     */
    User authenticate(Authentication authentication)
            throws AuthenticationFailedException;

    /**
     * 获取admin用户名
     * @return 返回admin用户名
     * @throws FtpException
     */
    String getAdminName() throws FtpException;

    /**
     * Check if the user is admin.
     * @param username 要检查的用户名 {@link User}
     * @return 是admin：true 否则返回false
     * @throws FtpException
     */
    boolean isAdmin(String username) throws FtpException;
}
