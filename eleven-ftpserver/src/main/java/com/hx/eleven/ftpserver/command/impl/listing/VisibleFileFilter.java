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
package com.hx.eleven.ftpserver.command.impl.listing;

import com.hx.eleven.ftpserver.ftplet.FtpFile;

/**
 * <strong>Internal class, do not use directly.</strong>
 * 
 * Selects files that are visible
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class VisibleFileFilter implements FileFilter {

    private final FileFilter wrappedFilter;

    /**
     * Default constructor
     */
    public VisibleFileFilter() {
        this(null);
    }

    /**
     * Constructor with a wrapped filter, allows for chaining filters
     * 
     * @param wrappedFilter
     *            The {@link FileFilter} to wrap
     */
    public VisibleFileFilter(FileFilter wrappedFilter) {
        this.wrappedFilter = wrappedFilter;
    }

    /**
     * @see FileFilter#accept(FtpFile)
     */
    public boolean accept(FtpFile file) {
        if (wrappedFilter != null && !wrappedFilter.accept(file)) {
            return false;
        }

        return !file.isHidden();
    }
}
