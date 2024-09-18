package com.hx.lang.commons.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangml
 * @Date 2019-09-26
 */
public enum DemoEnum {

    CONTRACT_INFO ("CONTRACT_INFO","合同");

    private String code;
    private String desc;
    private static Map<String, List<Integer>> keyIndexs;

    public static void group(){
        keyIndexs = new HashMap<>();
        keyIndexs.put(CONTRACT_INFO.getCode(), Arrays.asList(1,2,3));
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    DemoEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
