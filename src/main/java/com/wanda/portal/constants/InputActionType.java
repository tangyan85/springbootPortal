package com.wanda.portal.constants;

public enum InputActionType {
    UPDATE_OR_NOTHING("UPDATE_OR_NOTHING", "--------"),
    ATTACH_OLD("ATTACH_OLD", "关联已有"),
    REMOTE_CREATE("REMOTE_CREATE", "远程创建");
    
    
    private String key;
    private String value;

    private InputActionType(String key, String value) {
        this.key=key;
        this.value=value;
    }

    public static InputActionType find(String key) {
        for (InputActionType tempEnum : InputActionType.values()) {
            if (tempEnum.getKey().equals(key)) {
                return tempEnum;
            }
        }
        return null;
    }

    public static enum RESULT {
        VALID_ATTACH_OLD,
        VALID_REMOTE_CREATE,
        VALID_UPDATE_OR_NOTHING,
        INVALID
    }
    
    public RESULT checkWithMainId(Long id) {
        if ((this == ATTACH_OLD) && (id == null)) {
            return RESULT.VALID_ATTACH_OLD;
        } else if ((this == REMOTE_CREATE) && (id == null)) {
            return RESULT.VALID_REMOTE_CREATE;
        } else if ((this == UPDATE_OR_NOTHING) && (id != null)) {
            return RESULT.VALID_UPDATE_OR_NOTHING;
        }
        return RESULT.INVALID;
    }
    
    
    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    
}
