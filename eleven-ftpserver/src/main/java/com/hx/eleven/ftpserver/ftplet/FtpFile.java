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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * ftp服务器使用的文件抽象
 * @author
 */
public interface FtpFile {

    /**
     * 根据 FileSystemView 获取文件的全路径
     * @return
     */
    String getAbsolutePath();

    /**
     * 文件名称
     * @return
     */
    String getName();

    /**
     * 文件是否隐藏
     * @return true|false
     */
    boolean isHidden();

    /**
     * 判断是否是文件夹/文件目录
     * @return true|false
     */
    boolean isDirectory();

    /**
     * 判断是否是一个文件
     * @return true|false
     */
    boolean isFile();

    /**
     *  判断文件目录/文件是否存在
     * @return true|false
     */
    boolean doesExist();

    /**
     * 判断是否有读权限
     * @return true|false
     */
    boolean isReadable();

    /**
     * 是否有写权限
     * @return true|false
     */
    boolean isWritable();

    /**
     * 是否有删除权限
     * @return true|false
     */
    boolean isRemovable();

    /**
     * 获取文件的所有者
     * @return
     */
    String getOwnerName();

    /**
     * 获取文件的所有者所属用户组
     * @return
     */
    String getGroupName();

    /**
     * 获取链接数
     * @return
     */
    int getLinkCount();

    /**
     * 获取UTC格式的最后修改时间
     * @return 最后修改时间的时间戳
     */
    long getLastModified();

    /**
     * 设置文件最后一次修改的时间戳
     * @param time 上次修改的时间，以毫秒为单位
     * @return
     */
    boolean setLastModified(long time);

    /**
     *  文件大小
     * @return
     */
    long getSize();

    /**
     *  返回文件的物理位置或路径，这完全取决于实现是否根据文件系统实现返回适当的值
     * @return 文件的物理位置或路径
     */
    Object getPhysicalFile();

    /**
     * 创建目录
     * @return  true|false
     */
    boolean mkdir();

    /**
     * 删除文件
     * @return true|false
     */
    boolean delete();

    /**
     * 移动文件
     * @param destination 移动的目标目录
     * @return true|false
     */
    boolean move(FtpFile destination);

    /**
     *
     * 列出文件清单
     *
     * @return
     */
    List<? extends FtpFile> listFiles();

    /**
     * 创建用于写入的输出流
     * @param offset 开始写入位置的字节数。如果文件不是随机访问的，除零以外的任何偏移量都将抛出异常
     * @return
     * @throws IOException
     */
    OutputStream createOutputStream(long offset) throws IOException;

    /**
     * 创建用于读取的输入流
     * @param offset 开始读取的字节数。如果文件不是随机访问的，除零以外的任何偏移量都将抛出异常。
     *
     * @return
     * @throws IOException
     */
    InputStream createInputStream(long offset) throws IOException;
}
