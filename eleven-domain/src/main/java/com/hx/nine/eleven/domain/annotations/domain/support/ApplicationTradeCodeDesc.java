package com.hx.nine.eleven.domain.annotations.domain.support;

import java.lang.annotation.*;
/**
 * 应用访问交易码实体类
 * <p>
 *     </br> 主交易码：
 *     交易码划分规则：
 *     交易码长度一共16位，
 *     1-4：  应用系统简称
 *     5-10： 系统编号
 *     11-12: 子模块编号或者系统编号后追加两位顺序号
 *     13-14：应用功能模块编码或者领域编码（按照领域规则拆分）
 *     15-16：交易类型编号
 *     17-18：交易顺序号
 *     </br> 子交易码：
 *     主交易码+SUB-2位操作类型编号-3位循序号
 * </p>
 *
 *
 * @author wml
 * @date 2022-11-28
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApplicationTradeCodeDesc {

    /**
     * 主交易码
     */
    String tradeCode() default "";
    /**
     *  子交易码
     */
    String subTradeCode() default "";
    /**
     * 应用模块
     */
    String applicationName() default "";
    /**
     * 子交易码操作说明
     */
    String subDescription() default "";
}
