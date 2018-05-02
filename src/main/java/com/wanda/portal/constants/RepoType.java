package com.wanda.portal.constants;

public enum RepoType {
    SVN("SVN", "SVN"),
    GIT("GIT", "GIT");
    
    private String key;
    private String value;

    private RepoType(String key, String value) {
        this.key=key;
        this.value=value;
    }

    public static RepoType find(String key) {
        for (RepoType tempEnum : RepoType.values()) {
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