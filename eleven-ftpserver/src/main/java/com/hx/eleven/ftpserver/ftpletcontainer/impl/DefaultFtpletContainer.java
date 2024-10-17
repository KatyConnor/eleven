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

package com.hx.eleven.ftpserver.ftpletcontainer.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.hx.eleven.ftpserver.ftplet.FtpException;
import com.hx.eleven.ftpserver.ftplet.FtpReply;
import com.hx.eleven.ftpserver.ftplet.FtpRequest;
import com.hx.eleven.ftpserver.ftplet.FtpSession;
import com.hx.eleven.ftpserver.ftplet.Ftplet;
import com.hx.eleven.ftpserver.ftplet.FtpletContext;
import com.hx.eleven.ftpserver.ftplet.FtpletResult;
import com.hx.eleven.ftpserver.ftpletcontainer.FtpletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *  ftplet 容器
 *
 * @author
 */
public class DefaultFtpletContainer implements FtpletContainer {

    private final Logger LOG = LoggerFactory.getLogger(DefaultFtpletContainer.class);

    private final Map<String, Ftplet> ftplets ;
    public DefaultFtpletContainer() {
        this(new ConcurrentHashMap<>());
    }
    
    public DefaultFtpletContainer(Map<String, Ftplet> ftplets) {
        this.ftplets = ftplets;
    }

    public synchronized Ftplet getFtplet(String name) {
        if (name == null) {
            return null;
        }
        return ftplets.get(name);
    }

    public synchronized void init(FtpletContext ftpletContext) throws FtpException {
        for (Entry<String, Ftplet> entry : ftplets.entrySet()) {
            entry.getValue().init(ftpletContext);
        }
    }

    public synchronized Map<String, Ftplet> getFtplets() {
        return ftplets;
    }

    /**
     * 销毁所有 Ftplet
     */
    public void destroy() {
        for (Entry<String, Ftplet> entry : ftplets.entrySet()) {
            try {
                entry.getValue().destroy();
            } catch (Exception ex) {
                LOG.error(entry.getKey() + " :: FtpletHandler.destroy()", ex);
            }
        }
    }

    /**
     *
     */
    public FtpletResult onConnect(FtpSession session) throws FtpException,
            IOException {
        FtpletResult retVal = FtpletResult.DEFAULT;
        for (Entry<String, Ftplet> entry : ftplets.entrySet()) {
            retVal = entry.getValue().onConnect(session);
            if (retVal == null) {
                retVal = FtpletResult.DEFAULT;
            }

            // proceed only if the return value is FtpletResult.DEFAULT
            if (retVal != FtpletResult.DEFAULT) {
                break;
            }
        }
        return retVal;
    }

    /**
     *
     */
    public FtpletResult onDisconnect(FtpSession session) throws FtpException,
            IOException {
        FtpletResult retVal = FtpletResult.DEFAULT;
        for (Entry<String, Ftplet> entry : ftplets.entrySet()) {

            retVal = entry.getValue().onDisconnect(session);
            if (retVal == null) {
                retVal = FtpletResult.DEFAULT;
            }

            // proceed only if the return value is FtpletResult.DEFAULT
            if (retVal != FtpletResult.DEFAULT) {
                break;
            }
        }
        return retVal;
    }

    public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply)
            throws FtpException, IOException {
        FtpletResult retVal = FtpletResult.DEFAULT;
        for (Entry<String, Ftplet> entry : ftplets.entrySet()) {

            retVal = entry.getValue().afterCommand(session, request, reply);
            if (retVal == null) {
                retVal = FtpletResult.DEFAULT;
            }

            // proceed only if the return value is FtpletResult.DEFAULT
            if (retVal != FtpletResult.DEFAULT) {
                break;
            }
        }
        return retVal;
    }

    public FtpletResult beforeCommand(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        FtpletResult retVal = FtpletResult.DEFAULT;
        for (Entry<String, Ftplet> entry : ftplets.entrySet()) {

            retVal = entry.getValue().beforeCommand(session, request);
            if (retVal == null) {
                retVal = FtpletResult.DEFAULT;
            }

            // proceed only if the return value is FtpletResult.DEFAULT
            if (retVal != FtpletResult.DEFAULT) {
                break;
            }
        }
        return retVal;
    }

}
