package com.hx.sys.message.code.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**

 *
 *
 * @author wangml
 * @date 2019-08-14
 */
public enum SystemCode {

    SUCCESS("SS","成功"),
    PROCESS("DD","处理中"),
    FAILED("FF","失败");

    private String code;
    private String msg;

    SystemCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static List<SystemCode> getAll(){
        return Arrays.asList(SystemCode.values());
    }

    public static SystemCode getByCode(String code){
        return getAll().stream().filter( o -> o.code.equals(code)).findFirst().orElse(null);
    }

    public static String getNameByCode(String code){
        SystemCode systemCode = getByCode(code);
        if (Optional.ofNullable(systemCode).isPresent()){
            return systemCode.getMsg();
        }
        return null;
    }

    public static List<String> getAllNames(){
        return getAll().stream().map(v -> v.getMsg()).collect(Collectors.toList());
    }
}
