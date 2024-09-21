/*
 * Copyright © 2018 organization baomidou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hx.nine.eleven.datasources.annotation;


import java.lang.annotation.*;

/**
 *
 * 数据源切换注解，可以使用在类和方法上，如果放在类上整个类使用，
 * 如果行放在方法上只有当前方法使用
 * @author wml
 * @date 2022-11-01
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HXDataSource {

    /**
     *  需要使用的数据源匹配名称
     *
     * @return 想要使用的数据源名称
     */
    String value();
}