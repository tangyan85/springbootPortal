package com.wanda.portal.constants;

public enum ProjectStatus {
    START("START", "在建"),
    END("END", "归档");
    
    private String key;
    private String value;

    private ProjectStatus(String key, String value) {
        this.key=key;
        this.value=value;
    }

    public static ProjectStatus find(String key) {
        for (ProjectStatus tempEnum : ProjectStatus.values()) {
            if (tempEnum.getKey().equals(key)) {
                return tempEnum;
            }
        }
        return null;
    }

	public String getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}
}
