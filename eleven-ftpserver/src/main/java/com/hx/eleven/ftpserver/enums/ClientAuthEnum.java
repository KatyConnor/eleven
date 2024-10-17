/*
 * Copyright 1999-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hx.eleven.ftpserver.enums;

/**
 *
 * 在SSL会话期间可能的客户端身份验证级别的枚举
 *
 * @author wml
 */
public enum ClientAuthEnum {

    /**
     * 需要客户端身份验证
     */
    NEED,

    /**
     * 请求客户端身份验证，但不是必需的
     */
    WANT,

    /**
     * 不需要客户端身份验证
     */
    NONE
}