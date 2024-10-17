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

package com.hx.eleven.ftpserver.ftplet;

/**
 * 用于描述结构的类型安全枚举
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public enum Structure {

    /**
     * 文件结构
     */
    FILE;

    /**
     * 将来自STRU命令的参数值解析到类型安全类中
     * 
     * @param argument
     *            The argument value from the STRU command. Not case sensitive
     * @return The appropriate structure
     * @throws IllegalArgumentException
     *             If the structure is unknown
     */

    public static Structure parseArgument(char argument) {
        switch (argument) {
        case 'F':
        case 'f':
            return FILE;
        default:
            throw new IllegalArgumentException("Unknown structure: " + argument);
        }
    }
}
