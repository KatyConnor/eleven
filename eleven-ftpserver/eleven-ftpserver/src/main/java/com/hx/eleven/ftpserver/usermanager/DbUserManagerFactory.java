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

import javax.sql.DataSource;

import com.hx.eleven.ftpserver.FtpServerConfigurationException;
import com.hx.eleven.ftpserver.ftplet.UserManager;
import com.hx.eleven.ftpserver.usermanager.impl.DbUserManager;

/**
 *
 *
 */
public class DbUserManagerFactory extends UserManagerFactory {

    /**
     * 默认管理员用户
     */
    private String adminName = "admin";
    private String insertUserStmt;
    private String updateUserStmt;
    private String deleteUserStmt;
    private String selectUserStmt;
    private String selectAllStmt;
    private String isAdminStmt;
    private String authenticateStmt;
    private DataSource dataSource;
    
    public UserManager createUserManager() {
        if (dataSource == null) {
            throw new FtpServerConfigurationException("Required data source not provided");
        }
        if (insertUserStmt == null) {
            throw new FtpServerConfigurationException("Required insert user SQL statement not provided");
        }
        if (updateUserStmt == null) {
            throw new FtpServerConfigurationException("Required update user SQL statement not provided");
        }
        if (deleteUserStmt == null) {
            throw new FtpServerConfigurationException("Required delete user SQL statement not provided");
        }
        if (selectUserStmt == null) {
            throw new FtpServerConfigurationException("Required select user SQL statement not provided");
        }
        if (selectAllStmt == null) {
            throw new FtpServerConfigurationException("Required select all users SQL statement not provided");
        }
        if (isAdminStmt == null) {
            throw new FtpServerConfigurationException("Required is admin user SQL statement not provided");
        }
        if (authenticateStmt == null) {
            throw new FtpServerConfigurationException("Required authenticate user SQL statement not provided");
        }
        
        return new DbUserManager(dataSource, selectAllStmt, selectUserStmt, 
                insertUserStmt, updateUserStmt, deleteUserStmt, authenticateStmt, 
                isAdminStmt, passwordEncryptor, adminName);
    }
    

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getSqlUserInsert() {
        return insertUserStmt;
    }

    public void setSqlUserInsert(String sql) {
        insertUserStmt = sql;
    }

    public String getSqlUserDelete() {
        return deleteUserStmt;
    }

    public void setSqlUserDelete(String sql) {
        deleteUserStmt = sql;
    }

    public String getSqlUserUpdate() {
        return updateUserStmt;
    }

    public void setSqlUserUpdate(String sql) {
        updateUserStmt = sql;
    }

    public String getSqlUserSelect() {
        return selectUserStmt;
    }

    public void setSqlUserSelect(String sql) {
        selectUserStmt = sql;
    }

    public String getSqlUserSelectAll() {
        return selectAllStmt;
    }

    public void setSqlUserSelectAll(String sql) {
        selectAllStmt = sql;
    }

    public String getSqlUserAuthenticate() {
        return authenticateStmt;
    }

    public void setSqlUserAuthenticate(String sql) {
        authenticateStmt = sql;
    }

    public String getSqlUserAdmin() {
        return isAdminStmt;
    }

    public void setSqlUserAdmin(String sql) {
        isAdminStmt = sql;
    }
}