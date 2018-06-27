package com.wanda.portal.constants;

public enum ServerType {
    NORMAL, JIRA, CONFLUENCE, SVN(RepoType.SVN), GIT(RepoType.GIT), JENKINS, ARTIFACT, SONAR,PORTAL,TESTLINK,VIEWVC;
    private RepoType repoType;

    private ServerType() {
    }

    private ServerType(RepoType repoType) {
        this.repoType = repoType;
    }

    public static ServerType find(RepoType repoType) {
        for (ServerType tempEnum : ServerType.values()) {
            if (repoType.equals(tempEnum.repoType)) {
                return tempEnum;
            }
        }
        return null;
    }

}
