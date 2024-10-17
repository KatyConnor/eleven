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

package com.hx.eleven.ftpserver.main;

import com.hx.eleven.ftpserver.db.HSQLServer;
import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.core.constant.DefualtProperType;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.context.ClassPathBeanDefinitionScanner;
import com.hx.nine.eleven.core.env.ElevenYamlReadUtils;
import com.hx.eleven.ftpserver.FtpServer;
import com.hx.eleven.ftpserver.context.DefaultFtpServerContext;
import com.hx.eleven.ftpserver.impl.DefaultFtpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 命令行模式启动FtpServer
 *
 * @author
 */
public class FtpServerCommandLine {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpServerCommandLine.class);

    protected FtpServerCommandLine() {
    }

    /**
     * 服务启动入口
     * @param args
     */
    public static void main(String args[]) {
        FtpServerCommandLine cli = new FtpServerCommandLine();
        try {
            // 加载配置文件
            String properties = null;
            if (args != null && args.length > 0){
                for (String arg : args){
                    LOGGER.info("入参：{}！", arg);
                    if (arg.startsWith("-D"+ DefualtProperType.CONFIG_PATH) || arg.startsWith(DefualtProperType.CONFIG_PATH)){
                        properties = arg;
                        break;
                    }
                }
            }
            // 1、加载配置文件，初始化环境变量
            if (StringUtils.isNotEmpty(properties)){
                String[] prop = properties.split("=");
                if (prop[0].startsWith("-D")){
                    prop[0] = prop[0].substring(2);
                }
                ElevenYamlReadUtils.build().addProperties(prop[0],prop[1]).readYamlConfiguration(prop[1]);
            }else {
                ElevenYamlReadUtils.build().readYamlConfiguration(null);
            }
            // 初始化bean容器，和上下文HXVertxApplicationContext
            ClassPathBeanDefinitionScanner.build().initClass();
            // 初始化 hsqldb 数据库
            HSQLServer.runStart(args);
//
//            Reflections reflections = new Reflections((new ConfigurationBuilder()).forPackages("org.apache.ftpserver"));
//
            LOGGER.info("容器初始化完成！");

            // get configuration
            FtpServer server = cli.getConfiguration(args);
            if (server == null) {
                return;
            }
            // start the server
            server.start();
            System.out.println("FtpServer started");

            // 服务停止钩子
            cli.addShutdownHook(server);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Add shutdown hook.
     */
    private void addShutdownHook(final FtpServer engine) {
        //启动一个
        Runnable shutdownHook = new Runnable() {
            public void run() {
                System.out.println("Stopping server...");
                engine.stop();
            }
        };

        // add shutdown hook
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(shutdownHook));
    }

    /**
     * Print the usage message.
     */
    protected void usage() {
        System.err
                .println("Usage: java org.apache.ftpserver.main.CommandLine [OPTION] [CONFIGFILE]");
        System.err
                .println("Starts FtpServer using the default configuration of the ");
        System.err.println("configuration file if provided.");
        System.err.println("");
        System.err
                .println("      --default              use the default configuration, ");
        System.err
                .println("                             also used if no command line argument is given ");
        System.err.println("  -?, --help                 print this message");
    }

    /**
     * Get the configuration object.
     */
    protected FtpServer getConfiguration(String[] args) throws Exception {
        FtpServer server = null;
        if (args.length == 0) {
            System.out.println("Using default configuration");
            server =  new DefaultFtpServer(ElevenApplicationContextAware.getBean(DefaultFtpServerContext.class));
        } else if ((args.length == 1) && args[0].equals("-default")) {
            // supported for backwards compatibility, but not documented
            System.out.println("The -default switch is deprecated, please use --default instead");
            System.out.println("Using default configuration");
            server = new DefaultFtpServer(ElevenApplicationContextAware.getBean(DefaultFtpServerContext.class));
        } else if ((args.length == 1) && args[0].equals("--default")) {
            System.out.println("Using default configuration");
            server = new DefaultFtpServer(ElevenApplicationContextAware.getBean(DefaultFtpServerContext.class));
        } else if ((args.length == 1) && args[0].equals("--help")) {
            usage();
        } else if ((args.length == 1) && args[0].equals("-?")) {
            usage();
        }
        else {
            usage();
        }
        return server;
    }
}