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
package hx.nine.eleven.datasources.strategy;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机负载分配
 *
 * @author wml
 * @since 1.0.0
 */
public class RandomDynamicDataSourceStrategy implements DynamicDataSourceStrategy {

    @Override
    public String determineKey(List<String> dsNames) {
        return dsNames.get(ThreadLocalRandom.current().nextInt(dsNames.size()));
    }
}
