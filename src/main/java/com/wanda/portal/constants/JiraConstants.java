package com.wanda.portal.constants;

import java.util.HashMap;
import java.util.Map;

/*
 * 如果"projectTypeKey": "Software", 则：projectTemplateKey可以为：
 * 
 * com.pyxis.greenhopper.jira:gh-scrum-template 
 * com.pyxis.greenhopper.jira:gh-kanban-template
 * com.pyxis.greenhopper.jira:basic-software-development-template
 * 
 * 如果"projectTypeKey": "business",则：projectTemplateKey可以为：
 * 
 * com.atlassian.jira-core-project-templates:jira-core-project-management
 * com.atlassian.jira-core-project-templates:jira-core-task-management
 * com.atlassian.jira-core-project-templates:jira-core-process-management 
 * 选定不同的模板。
 */
public class JiraConstants {

    public static final String PROJECT_LEAD = "PROJECT_LEAD";

    public static final String project = "project";
    
    public static final String business = "business";
    public static final String software = "software";

    private static final String gh_scrum_template = "gh-scrum-template";
    private static final String gh_kanban_template = "gh-kanban-template";
    private static final String basic_software_development_template = "basic-software-development-template";

    private static final String jira_core_project_management = "jira-core-project-management";
    private static final String jira_core_task_management = "jira-core-task-management";
    private static final String jira_core_process_management = "jira-core-process-management";

    private static final String SoftwareGroupId = "com.pyxis.greenhopper.jira";
    private static final String BusinessGroupId = "com.atlassian.jira-core-project-templates";

    private static final String COLON = ":";

    private static Map<TEMPLATE, String> innerMap = new HashMap<>();
    static {
        innerMap.put(TEMPLATE.SOFTWARE_SCRUM, SoftwareGroupId + COLON + gh_scrum_template);
        innerMap.put(TEMPLATE.SOFTWARE_KANBAN, SoftwareGroupId + COLON + gh_kanban_template);
        innerMap.put(TEMPLATE.SOFTWARE_BASIC_DEV, SoftwareGroupId + COLON + basic_software_development_template);
        innerMap.put(TEMPLATE.BUSINESS_ProjectManagement, BusinessGroupId + COLON + jira_core_project_management);
        innerMap.put(TEMPLATE.BUSINESS_TaskManagement, BusinessGroupId + COLON + jira_core_task_management);
        innerMap.put(TEMPLATE.BUSINESS_ProcessManagement, BusinessGroupId + COLON + jira_core_process_management);
    }

    public enum TEMPLATE {
        SOFTWARE_SCRUM, // scrum 开发方法模板
        SOFTWARE_KANBAN, // 看板方法模板
        SOFTWARE_BASIC_DEV, // 基本开发方法模板
        BUSINESS_ProjectManagement, // 项目管理
        BUSINESS_TaskManagement, // 任务管理
        BUSINESS_ProcessManagement; // 流程管理
        public String genFullString() {
            return innerMap.get(this);
        }
    }
    
    public static void main(String[] args) {
        System.out.println(JiraConstants.TEMPLATE.SOFTWARE_BASIC_DEV.genFullString());
        System.out.println(JiraConstants.TEMPLATE.SOFTWARE_KANBAN.genFullString());
        System.out.println(JiraConstants.TEMPLATE.SOFTWARE_SCRUM.genFullString());
        System.out.println(JiraConstants.TEMPLATE.BUSINESS_ProjectManagement.genFullString());
        System.out.println(JiraConstants.TEMPLATE.BUSINESS_TaskManagement.genFullString());
        System.out.println(JiraConstants.TEMPLATE.BUSINESS_ProcessManagement.genFullString());
    }
}
