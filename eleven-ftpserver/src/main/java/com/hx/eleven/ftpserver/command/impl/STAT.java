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
import com.hx.eleven.ftpserver.command.impl.listing.DirectoryLister;
import com.hx.eleven.ftpserver.command.impl.listing.LISTFileFormater;
import com.hx.eleven.ftpserver.command.impl.listing.ListArgument;
import com.hx.eleven.ftpserver.command.impl.listing.ListArgumentParser;
import com.hx.eleven.ftpserver.ftplet.FtpException;
import com.hx.eleven.ftpserver.ftplet.FtpFile;
import com.hx.eleven.ftpserver.ftplet.FtpReply;
import com.hx.eleven.ftpserver.ftplet.FtpRequest;
import com.hx.eleven.ftpserver.context.FtpServerContext;
import com.hx.eleven.ftpserver.impl.LocalizedDataTransferFtpReply;
import com.hx.eleven.ftpserver.impl.LocalizedFileActionFtpReply;
import com.hx.eleven.ftpserver.impl.LocalizedFtpReply;

/**
 * <strong>Internal class, do not use directly.</strong>
 * 
 * <code>STAT [&lt;SP&gt; &lt;pathname&gt;] &lt;CRLF&gt;</code><br>
 * 
 * This command shall cause a status response to be sent over the control
 * connection in the form of a reply.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class STAT extends AbstractCommand {

    private static final LISTFileFormater LIST_FILE_FORMATER = new LISTFileFormater();

    private final DirectoryLister directoryLister = new DirectoryLister();
    
    /**
     * Execute command
     */
    public void execute(final FtpIoSession session,
            final FtpServerContext context, final FtpRequest request)
            throws IOException {

        // reset state variables
        session.resetState();

        if(request.getArgument() != null) {
            ListArgument parsedArg = ListArgumentParser.parse(request.getArgument());

            // check that the directory or file exists
            FtpFile file = null;
            try {
                file = session.getFileSystemView().getFile(parsedArg.getFile());
                if(!file.doesExist()) {
                    session.write(LocalizedDataTransferFtpReply.translate(session, request, context,
                            FtpReply.REPLY_450_REQUESTED_FILE_ACTION_NOT_TAKEN, "LIST",
                            null, file));             
                    return;
                }
                
                String dirList = directoryLister.listFiles(parsedArg, 
                        session.getFileSystemView(), LIST_FILE_FORMATER);

                int replyCode;
                if(file.isDirectory()) {
                    replyCode = FtpReply.REPLY_212_DIRECTORY_STATUS;
                } else {
                    replyCode = FtpReply.REPLY_213_FILE_STATUS;
                }
                
                session.write(LocalizedFileActionFtpReply.translate(session, request, context,
                        replyCode, "STAT", dirList, file));
                
            } catch (FtpException e) {
                session
                .write(LocalizedFileActionFtpReply
                        .translate(
                                session,
                                request,
                                context,
                                FtpReply.REPLY_450_REQUESTED_FILE_ACTION_NOT_TAKEN,
                                "STAT", null, file));
            }
        
        } else {
            // write the status info
            session.write(LocalizedFtpReply.translate(session, request, context,
                    FtpReply.REPLY_211_SYSTEM_STATUS_REPLY, "STAT", null));
        }
    }

}
