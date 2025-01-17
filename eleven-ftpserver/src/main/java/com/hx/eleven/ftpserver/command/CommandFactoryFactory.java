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
import java.util.Map.Entry;

import com.hx.eleven.ftpserver.command.impl.ABOR;
import com.hx.eleven.ftpserver.command.impl.ACCT;
import com.hx.eleven.ftpserver.command.impl.APPE;
import com.hx.eleven.ftpserver.command.impl.AUTH;
import com.hx.eleven.ftpserver.command.impl.CDUP;
import com.hx.eleven.ftpserver.command.impl.CWD;
import com.hx.eleven.ftpserver.command.impl.DELE;
import com.hx.eleven.ftpserver.command.impl.EPRT;
import com.hx.eleven.ftpserver.command.impl.EPSV;
import com.hx.eleven.ftpserver.command.impl.FEAT;
import com.hx.eleven.ftpserver.command.impl.HELP;
import com.hx.eleven.ftpserver.command.impl.LANG;
import com.hx.eleven.ftpserver.command.impl.LIST;
import com.hx.eleven.ftpserver.command.impl.MD5;
import com.hx.eleven.ftpserver.command.impl.MDTM;
import com.hx.eleven.ftpserver.command.impl.MFMT;
import com.hx.eleven.ftpserver.command.impl.MKD;
import com.hx.eleven.ftpserver.command.impl.MLSD;
import com.hx.eleven.ftpserver.command.impl.MLST;
import com.hx.eleven.ftpserver.command.impl.MODE;
import com.hx.eleven.ftpserver.command.impl.NLST;
import com.hx.eleven.ftpserver.command.impl.NOOP;
import com.hx.eleven.ftpserver.command.impl.OPTS;
import com.hx.eleven.ftpserver.command.impl.PASS;
import com.hx.eleven.ftpserver.command.impl.PASV;
import com.hx.eleven.ftpserver.command.impl.PBSZ;
import com.hx.eleven.ftpserver.command.impl.PORT;
import com.hx.eleven.ftpserver.command.impl.PROT;
import com.hx.eleven.ftpserver.command.impl.PWD;
import com.hx.eleven.ftpserver.command.impl.QUIT;
import com.hx.eleven.ftpserver.command.impl.REIN;
import com.hx.eleven.ftpserver.command.impl.REST;
import com.hx.eleven.ftpserver.command.impl.RETR;
import com.hx.eleven.ftpserver.command.impl.RMD;
import com.hx.eleven.ftpserver.command.impl.RNFR;
import com.hx.eleven.ftpserver.command.impl.RNTO;
import com.hx.eleven.ftpserver.command.impl.SITE;
import com.hx.eleven.ftpserver.command.impl.SIZE;
import com.hx.eleven.ftpserver.command.impl.STAT;
import com.hx.eleven.ftpserver.command.impl.STOR;
import com.hx.eleven.ftpserver.command.impl.STOU;
import com.hx.eleven.ftpserver.command.impl.STRU;
import com.hx.eleven.ftpserver.command.impl.SYST;
import com.hx.eleven.ftpserver.command.impl.TYPE;
import com.hx.eleven.ftpserver.command.impl.USER;
import com.hx.eleven.ftpserver.command.impl.SITE_DESCUSER;
import com.hx.eleven.ftpserver.command.impl.SITE_HELP;
import com.hx.eleven.ftpserver.command.impl.SITE_STAT;
import com.hx.eleven.ftpserver.command.impl.SITE_WHO;
import com.hx.eleven.ftpserver.command.impl.SITE_ZONE;

/**
 *
 *
 * @author
 */
public class CommandFactoryFactory {

    private static final HashMap<String, Command> DEFAULT_COMMAND_MAP = new HashMap<>();

    static {
        // first populate the default command list
        DEFAULT_COMMAND_MAP.put("ABOR", new ABOR());
        DEFAULT_COMMAND_MAP.put("ACCT", new ACCT());
        DEFAULT_COMMAND_MAP.put("APPE", new APPE());
        DEFAULT_COMMAND_MAP.put("AUTH", new AUTH());
        DEFAULT_COMMAND_MAP.put("CDUP", new CDUP());
        DEFAULT_COMMAND_MAP.put("CWD", new CWD());
        DEFAULT_COMMAND_MAP.put("DELE", new DELE());
        DEFAULT_COMMAND_MAP.put("EPRT", new EPRT());
        DEFAULT_COMMAND_MAP.put("EPSV", new EPSV());
        DEFAULT_COMMAND_MAP.put("FEAT", new FEAT());
        DEFAULT_COMMAND_MAP.put("HELP", new HELP());
        DEFAULT_COMMAND_MAP.put("LANG", new LANG());
        DEFAULT_COMMAND_MAP.put("LIST", new LIST());
        DEFAULT_COMMAND_MAP.put("MD5", new MD5());
        DEFAULT_COMMAND_MAP.put("MFMT", new MFMT());
        DEFAULT_COMMAND_MAP.put("MMD5", new MD5());
        DEFAULT_COMMAND_MAP.put("MDTM", new MDTM());
        DEFAULT_COMMAND_MAP.put("MLST", new MLST());
        DEFAULT_COMMAND_MAP.put("MKD", new MKD());
        DEFAULT_COMMAND_MAP.put("MLSD", new MLSD());
        DEFAULT_COMMAND_MAP.put("MODE", new MODE());
        DEFAULT_COMMAND_MAP.put("NLST", new NLST());
        DEFAULT_COMMAND_MAP.put("NOOP", new NOOP());
        DEFAULT_COMMAND_MAP.put("OPTS", new OPTS());
        DEFAULT_COMMAND_MAP.put("PASS", new PASS());
        DEFAULT_COMMAND_MAP.put("PASV", new PASV());
        DEFAULT_COMMAND_MAP.put("PBSZ", new PBSZ());
        DEFAULT_COMMAND_MAP.put("PORT", new PORT());
        DEFAULT_COMMAND_MAP.put("PROT", new PROT());
        DEFAULT_COMMAND_MAP.put("PWD", new PWD());
        DEFAULT_COMMAND_MAP.put("QUIT", new QUIT());
        DEFAULT_COMMAND_MAP.put("REIN", new REIN());
        DEFAULT_COMMAND_MAP.put("REST", new REST());
        DEFAULT_COMMAND_MAP.put("RETR", new RETR());
        DEFAULT_COMMAND_MAP.put("RMD", new RMD());
        DEFAULT_COMMAND_MAP.put("RNFR", new RNFR());
        DEFAULT_COMMAND_MAP.put("RNTO", new RNTO());
        DEFAULT_COMMAND_MAP.put("SITE", new SITE());
        DEFAULT_COMMAND_MAP.put("SIZE", new SIZE());
        DEFAULT_COMMAND_MAP.put("SITE_DESCUSER", new SITE_DESCUSER());
        DEFAULT_COMMAND_MAP.put("SITE_HELP", new SITE_HELP());
        DEFAULT_COMMAND_MAP.put("SITE_STAT", new SITE_STAT());
        DEFAULT_COMMAND_MAP.put("SITE_WHO", new SITE_WHO());
        DEFAULT_COMMAND_MAP.put("SITE_ZONE", new SITE_ZONE());

        DEFAULT_COMMAND_MAP.put("STAT", new STAT());
        DEFAULT_COMMAND_MAP.put("STOR", new STOR());
        DEFAULT_COMMAND_MAP.put("STOU", new STOU());
        DEFAULT_COMMAND_MAP.put("STRU", new STRU());
        DEFAULT_COMMAND_MAP.put("SYST", new SYST());
        DEFAULT_COMMAND_MAP.put("TYPE", new TYPE());
        DEFAULT_COMMAND_MAP.put("USER", new USER());
    }

    private Map<String, Command> commandMap = new HashMap<>();
    private boolean useDefaultCommands = true;

    public CommandFactory createCommandFactory() {
        Map<String, Command> mergedCommands = new HashMap<>();
        if(useDefaultCommands) {
            mergedCommands.putAll(DEFAULT_COMMAND_MAP);
        }
        if (commandMap.size() > 0){
            mergedCommands.putAll(commandMap);
        }
        return new DefaultCommandFactory(mergedCommands);
    }

    public boolean isUseDefaultCommands() {
        return useDefaultCommands;
    }

    public void setUseDefaultCommands(final boolean useDefaultCommands) {
        this.useDefaultCommands = useDefaultCommands;
    }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }

    public void addCommand(String commandName, Command command) {
        if(commandName == null) {
            throw new NullPointerException("commandName can not be null");
        }
        if(command == null) {
            throw new NullPointerException("command can not be null");
        }
        commandMap.put(commandName.toUpperCase(), command);
    }

    public void setCommandMap(final Map<String, Command> commandMap) {
        if (commandMap == null) {
            throw new NullPointerException("commandMap can not be null");
        }
        this.commandMap.clear();
        for (Entry<String, Command> entry : commandMap.entrySet()) {
            this.commandMap.put(entry.getKey().toUpperCase(), entry.getValue());
        }
    }
}
