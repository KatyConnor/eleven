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

package com.hx.eleven.ftpserver.ftplet.enums;

/**
 * 数据类型描述枚举
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public enum DataTypeEnum {

    /**
     * 二进制数据类型
     */
    BINARY,

    /**
     * ASCII data type
     */
    ASCII;

    /**
     *  根据命令参数，映射对应的枚举类
     *
     * @param argument 命令参数
     *
     * @return
     * @throws IllegalArgumentException
     */
    public static DataTypeEnum parseArgument(char argument) {
        switch (argument) {
        case 'A':
        case 'a':
            return ASCII;
        case 'I':
        case 'i':
            return BINARY;
        default:
            throw new IllegalArgumentException("Unknown data type: " + argument);
        }
    }
}
