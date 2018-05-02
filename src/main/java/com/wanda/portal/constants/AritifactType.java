package com.wanda.portal.constants;

public enum AritifactType {
    MAVEN("MAVEN", "MAVEN"),
    PYTHON("PYTHON", "PYTHON"),
    NPM("NPM", "NPM"),
    RAW("RAW", "RAW");
    
    private String key;
    private String value;

    private AritifactType(String key, String value) {
        this.key=key;
        this.value=value;
    }

    public static AritifactType find(String key) {
        for (AritifactType tempEnum : AritifactType.values()) {
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

    public String getPrefix() {
        String result = "";
        switch (this) {
            case MAVEN:
                result = Constants.MAVEN_ARTIFACTS_PREFIX;
                break;
            case PYTHON:
                result = Constants.PYTHON_ARTIFACTS_PREFIX;
                break;
            case NPM:
                result = Constants.NPM_ARTIFACTS_PREFIX;
                break;
            case RAW:
                result = Constants.RAW_ARIFACTS_PREFIX;
                break;

        }
        return result;
    }
}
