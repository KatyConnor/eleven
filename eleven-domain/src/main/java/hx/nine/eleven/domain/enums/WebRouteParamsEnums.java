package hx.nine.eleven.domain.enums;

import java.util.Arrays;

public enum WebRouteParamsEnums {
    HEADER_DTO("HEADER_DTO"),
    BODY_DTO("BODY_DTO"),
    HEADER_FORM("HEADER_FORM"),
    BODY_FORM("BODY_FORM"),
    HEADER_PARAM("HEADER_PARAM"),
    BODY_PARAM("BODY_PARAM"),
    MAPPER_FACTORY("MAPPER_FACTORY"),
    HEADER_VO("HEADER_VO");

    private String name;

    WebRouteParamsEnums(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static WebRouteParamsEnums getByName(String name){
        return Arrays.stream(values()).filter(o -> o.name.equals(name)).findFirst().orElse(null);
    }
}
