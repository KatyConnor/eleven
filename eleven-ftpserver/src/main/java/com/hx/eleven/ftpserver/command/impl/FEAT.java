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

package com.hx.eleven.ftpserver.command.impl;

import java.io.IOException;

import com.hx.eleven.ftpserver.session.FtpIoSession;
import com.hx.eleven.ftpserver.command.AbstractCommand;
import com.hx.eleven.ftpserver.ftplet.FtpException;
import com.hx.eleven.ftpserver.ftplet.FtpReply;
import com.hx.eleven.ftpserver.ftplet.FtpRequest;
import com.hx.eleven.ftpserver.context.FtpServerContext;
import com.hx.eleven.ftpserver.impl.LocalizedFtpReply;

/**
 * <strong>Internal class, do not use directly.</strong>
 * 
 * The FEAT command (introduced in [RFC-2389]) allows servers with additional
 * features to advertise these to a client by responding to the FEAT command. If
 * a server supports the FEAT command then it MUST advertise supported AUTH,
 * PBSZ and PROT commands in the reply.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class FEAT extends AbstractCommand {

    public void execute(final FtpIoSession session,
            final FtpServerContext context, final FtpRequest request)
            throws IOException, FtpException {

        // reset state variables
        session.resetState();

        session.write(LocalizedFtpReply.translate(session, request, context,
                FtpReply.REPLY_211_SYSTEM_STATUS_REPLY, "FEAT", null));
    }
}
