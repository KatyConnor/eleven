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

import java.net.InetAddress;
import java.security.cert.Certificate;

/**
 *
 *
 * @author
 */
public class UserMetadata {

    private Certificate[] certificateChain;

    private InetAddress inetAddress;

    public Certificate[] getCertificateChain() {
        if (certificateChain != null) {
            return certificateChain.clone();
        } else {
            return null;
        }
    }

    public void setCertificateChain(final Certificate[] certificateChain) {
        if (certificateChain != null) {
            this.certificateChain = certificateChain.clone();
        } else {
            this.certificateChain = null;
        }
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(final InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

}
