package com.wanda.portal.constants;

public enum RepoProtocol {
    HTTP("HTTP", "HTTP"),
    HTTPS("HTTPS", "HTTPS"),
    SVN("SVN", "SVN"),
    GIT("GIT", "GIT");
    
    private String key;
    private String value;

    private RepoProtocol(String key, String value) {
        this.key=key;
        this.value=value;
    }

    public static RepoProtocol find(String key) {
        for (RepoProtocol tempEnum : RepoProtocol.values()) {
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

    public String toStr() {
        return name().toLowerCase() + "://";
    }
}
