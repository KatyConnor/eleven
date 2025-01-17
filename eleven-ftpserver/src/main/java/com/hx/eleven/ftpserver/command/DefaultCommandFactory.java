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

package com.hx.eleven.ftpserver.command;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 命令工厂根据 FTP 请求命令字符串返回适当的命令实现。
 *
 * @author
 */
public class DefaultCommandFactory implements CommandFactory {

    private final Map<String, Command> commandMap;

    public DefaultCommandFactory() {
        this(new HashMap<>());
    }

    public DefaultCommandFactory(Map<String, Command> commandMap) {
        this.commandMap = commandMap;
    }

    @Override
    public Command getCommand(final String cmdName) {
        if (cmdName == null || cmdName.isEmpty()) {
            return null;
        }
        String upperCaseCmdName = cmdName.toUpperCase();
        return commandMap.get(upperCaseCmdName);
    }
}
