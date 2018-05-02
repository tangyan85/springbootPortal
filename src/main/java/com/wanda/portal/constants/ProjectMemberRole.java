package com.wanda.portal.constants;

public enum ProjectMemberRole {
    PM("PM", "项目经理"),
    DEV("DEV", "开发人员"),
    QA("QA", "测试人员"),
    OP("OP", "运维人员");
    
    private String key;
    private String value;

    private ProjectMemberRole(String key, String value) {
        this.key=key;
        this.value=value;
    }

    public static ProjectMemberRole find(String key) {
        for (ProjectMemberRole tempEnum : ProjectMemberRole.values()) {
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
