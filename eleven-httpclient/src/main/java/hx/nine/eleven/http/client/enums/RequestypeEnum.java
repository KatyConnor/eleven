package hx.nine.eleven.http.client.enums;

/**
 * <p>
 *
 * </p>
 */
public enum RequestypeEnum {
    body_form("Body_form"),
    body_json("Body_form"),
    body_jsons("Body_form"),
    ;

    private String code;

    RequestypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
