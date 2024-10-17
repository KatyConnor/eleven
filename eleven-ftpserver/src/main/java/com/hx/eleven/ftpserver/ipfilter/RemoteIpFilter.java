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

package com.hx.eleven.ftpserver.ipfilter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.firewall.Subnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端 IP 黑名单配置
 * 
 * @author
 * @Date
 */

public class RemoteIpFilter extends CopyOnWriteArraySet<Subnet> implements SessionFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(RemoteIpFilter.class);

    /**
     *  filter 拦截类型
     */
    private IpFilterType type = null;

    public RemoteIpFilter(IpFilterType type) {
        this(type, new HashSet<>(0));
    }

    public RemoteIpFilter(IpFilterType type, Collection<? extends Subnet> collection) {
        super(collection);
        this.type = type;
    }

    public RemoteIpFilter(IpFilterType type, String[] blacklist) throws NumberFormatException, UnknownHostException {
        super();
        this.type = type;
        if (blacklist != null) {
            for (String token : blacklist) {
                if (token.trim().length() > 0) {
                    add(token);
                }
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Created DefaultIpFilter of type {} with the subnets {}", type, this);
        }
    }

    public IpFilterType getType() {
        return type;
    }

    public void setType(IpFilterType type) {
        this.type = type;
    }

    /**
     * 添加配置的黑名单IP地址
     * @param str IP字符串
     * @return
     * @throws NumberFormatException
     * @throws UnknownHostException
     */
    public boolean add(String str) throws NumberFormatException, UnknownHostException {
        if (str.trim().length() < 1) {
            throw new IllegalArgumentException("Invalid IP Address or Subnet: " + str);
        }
        String[] tokens = str.split("/");
        if (tokens.length == 2) {
            return add(new Subnet(InetAddress.getByName(tokens[0]), Integer.parseInt(tokens[1])));
        } else {
            return add(new Subnet(InetAddress.getByName(tokens[0]), 32));
        }
    }

    @Override
    public boolean accept(IoSession session) {
        InetAddress address = ((InetSocketAddress) session.getRemoteAddress()).getAddress();
        switch (type) {
        case ALLOW:
            for (Subnet subnet : this) {
                if (subnet.inSubnet(address)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Allowing connection from {} because it matches with the whitelist subnet {}", address, subnet);
                    }
                    return true;
                }
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Denying connection from {} because it does not match any of the whitelist subnets", address);
            }
            return false;
        case DENY:
            if (isEmpty()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Allowing connection from {} because blacklist is empty", address);
                }
                return true;
            }
            for (Subnet subnet : this) {
                if (subnet.inSubnet(address)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Denying connection from {} because it matches with the blacklist subnet {}", address, subnet);
                    }
                    return false;
                }
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Allowing connection from {} because it does not match any of the blacklist subnets",
                                address);
            }
            return true;
        default:
            throw new RuntimeException("Unknown or unimplemented filter type: " + type);
        }
    }
}
