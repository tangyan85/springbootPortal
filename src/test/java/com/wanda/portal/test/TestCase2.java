package com.wanda.portal.test;

import com.alibaba.fastjson.JSONObject;
import com.wanda.portal.RootApplication;
import com.wanda.portal.config.biz.JiraConfig;
import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.ProjectMemberRole;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.ConfluenceService;
import com.wanda.portal.dao.remote.JenkinsService;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dao.remote.RepoService;
import com.wanda.portal.dto.confluence.CreateConfluenceSpaceParamDTO;
import com.wanda.portal.dto.confluence.GenericConfluenceSpaceDTO;
import com.wanda.portal.dto.jenkins.JenkinsJobDTO;
import com.wanda.portal.dto.jira.GenericJiraProjectDTO;
import com.wanda.portal.dto.svn.SubversionRepoDTO;
import com.wanda.portal.dto.svn.SvnTemplateWrapperDTO;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.*;
import com.wanda.portal.utils.RegexUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class TestCase2 {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JiraConfig jiraConfig;

     @Test
     public void testRest() {
    
//     HttpHeaders headers = RestUtils.packBasicAuthHeader(jiraConfig.getUsername(),
//     jiraConfig.getPassword());
//    
//     HttpEntity<String> request = new HttpEntity<String>(headers);
//     ResponseEntity<String> response = restTemplate.exchange(
//     "http://10.215.4.199:8080/rest/api/2/project", HttpMethod.GET, request,
//     String.class);
//     String account = response.getBody();
//     System.out.println("Query for new JIRA for all projects: " + account);
//    
     }
    //
    @Autowired
    RepoService repoService;

    @Test
    public void checkIfExist() throws Exception {
        System.out.println("bigdata-2017存在吗？" + repoService.checkIfSvnRepoExists("bigdata-2017"));
        System.out.println("bigdata-2016存在吗？" + repoService.checkIfSvnRepoExists("bigdata-2016"));
        try {
            System.out.println("测试空白空间是否存在" + repoService.checkIfSvnRepoExists(""));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void querySubversionTemplate() throws Exception {
        SvnTemplateWrapperDTO z = repoService.findSubversionTemplates();
        System.out.println("Query For all templates: " + JSONObject.toJSONString(z));
    }

    @Test
    public void fetchAllSvnRepos() throws Exception {
        List<Server> ll = serverRepository.findByServerType(ServerType.SVN);
        repoService.setServer(ll.get(0));
        List<SubversionRepoDTO> z = repoService.fetchAllSvnRepos();
        System.out.println("Query For all repos: " + JSONObject.toJSONString(z));
    }
    //
    // // 参考此建立repo
    // // 入参repoName
    // // templateId,传-1为不用模板
    // // layoutflag,默认为true
    // @Test
    // public void createRepo() throws Exception {
    // SubversionRepoDTO rep = sCMRepoService.createSvnRepo("hehe2", 11l, true);
    // System.out.println(JSONObject.toJSONString(rep));
    // }

    @Test
    public void testSvnRegex() {
        Assert.assertEquals(true, RegexUtils.isValidRepo("__")); // 允许
        Assert.assertEquals(true, RegexUtils.isValidRepo("aa")); // 允许
        Assert.assertEquals(true, RegexUtils.isValidRepo("a9__")); // 允许
        Assert.assertEquals(true, RegexUtils.isValidRepo("sww-")); // 允许
        Assert.assertEquals(false, RegexUtils.isValidRepo("-sww-")); // -开头，不允许
        Assert.assertEquals(false, RegexUtils.isValidRepo("哈哈")); // 中文，不允许
        Assert.assertEquals(true, RegexUtils.checkEmail("chwn@ww.com"));
    }

    @Autowired
    JiraService jiraService;

    @Test
    public void createJira() throws Exception { 
//        JiraInputDTO dto = new JiraInputDTO();
//        dto.setAssigneeType(JiraConstants.PROJECT_LEAD);
//        dto.setDescription("nihao, jira");
//        dto.setName("EXAMPLE-518");
//        dto.setKey("TTYE");
//        dto.setLead("admin");
//        dto.setProjectTypeKey(JiraConstants.software);
//        dto.setProjectTemplateKey(JiraConstants.TEMPLATE.SOFTWARE_BASIC_DEV.genFullString());
//        try {
//            JiraOutputDTO w = jiraService.createJiraProjectUsingTemplate(dto, false);
//            System.out.println(w);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }


    @Test
    public void checkJira() throws Exception {
        boolean x = jiraService.checkIfJiraProjectExist("TTT");
        boolean y = jiraService.checkIfJiraProjectExist("BBCC");
        try {
            jiraService.checkIfJiraProjectExist("aa");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        Assert.assertEquals(true, x);
//        Assert.assertEquals(false, y);
        System.out.println("TTT 这个JIRA存在吗？" + x);
        System.out.println("BBCC 这个JIRA存在吗？" + y);
    }

    @Test
    public void testJiraRegex() {
        Assert.assertEquals(true, RegexUtils.isValidJiraProject("AA")); // 允许
        Assert.assertEquals(false, RegexUtils.isValidJiraProject("Ac")); // 不允许
        Assert.assertEquals(false, RegexUtils.isValidJiraProject("V_1")); // 不允许
        Assert.assertEquals(false, RegexUtils.isValidJiraProject("")); // 不允许
        Assert.assertEquals(true, RegexUtils.isValidJiraProject("ABC360")); // 允许
        Assert.assertEquals(true, RegexUtils.isValidJiraProject("C3P0")); // 允许
        Assert.assertEquals(false, RegexUtils.isValidJiraProject("1A")); // 不允许
    }

    @Test
    public void findAllJiraFromRemote() throws Exception {
        List<Server> ll = serverRepository.findByServerType(ServerType.JIRA);
        jiraService.setServer(ll.get(0));
        List<GenericJiraProjectDTO> z = jiraService.fetchAllJiraProjects(null);
        System.out.println("Query For all JIRAs: " + JSONObject.toJSONString(z));
    }

    @Test
    public void createJiraProjectUsingExistingProject() throws Exception {
//        try {
//            Long z = jiraService.createJiraProjectUsingExistingProject(10302l, "IIOO", "admin", "TRY again", true);
//            System.out.println(JSONObject.toJSONString(z));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }

    @Autowired
    ConfluenceService confluenceService;

    /* 获得所有confluence的space */
    @Test
    public void fetchAllConfluenceSpaces() {
        List<Server> ll = serverRepository.findByServerType(ServerType.CONFLUENCE);
        confluenceService.setServer(ll.get(0));
        List<GenericConfluenceSpaceDTO> zzz = confluenceService.fetchAllConfluenceSpaces();
        System.out.println("fetchAllConfluenceSpaces res=" + JSONObject.toJSONString(zzz));
    }

    /* 创建confluence的space */
    @Test
    public void createSpace() throws Exception {
        List<Server> ll = serverRepository.findByServerType(ServerType.CONFLUENCE);
        confluenceService.setServer(ll.get(0));
        CreateConfluenceSpaceParamDTO param = new CreateConfluenceSpaceParamDTO();
        param.setDescription("这是一个高级示范DEMO项目"); // 项目描述
        param.setKey("UNIQUEKKK"); // Space唯一的Key，不对外展现
        param.setName("Confluence展现的Space名称"); // 项目名称
        param.setRepresentation("似乎没有太多用处的表述"); // description的表述
        String view = "";
        try {
            view = confluenceService.createSpace(param);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("view = " + view);
    }

    @Autowired
    JenkinsService jenkinsService;

    @Autowired
    ServerRepository serverRepository;
    
    
    /* query jenkins所有job */
    @Test
    public void fetchAllJenkinsJobs() {
        List<Server> ll = serverRepository.findByServerType(ServerType.JENKINS);
        jenkinsService.setServer(ll.get(0));
        List<JenkinsJobDTO> list = jenkinsService.fetchAllJenkinsJobs();
        System.out.println("Query for all jenkins jobs: " + list);
    }

    /* query jenkins所有job */
    @Test
    public void createJenkinsUsingCopy() {
//        try {
//            jenkinsService.createJenkinsUsingCopy("mammoth2", "TRIAL/DEMO_FREESTYLE");
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }

    @Autowired
    private TestRestTemplate testRestTemplate;

    /*
     * 测试带参add Project
     * */
    @Test
    public void tryAddProjectX() {

        ProjectInputParam param = new ProjectInputParam();
        param.setProjectKey("VV");
        param.setProjectName("loan");
        param.setRank(11);
        param.setStatus("NORMAL");
        param.setDescription("this is a good project");
        
        List<ConfluenceSpaceInputParam> confluenceSpaceLists = new ArrayList<>();
        ConfluenceSpaceInputParam conf = new ConfluenceSpaceInputParam();
        conf.setInputActionType(InputActionType.REMOTE_CREATE);
        conf.setSpaceKey("AAA");
        conf.setSpaceDescription("my conf");
        conf.setSpaceName("AAA space");
        confluenceSpaceLists.add(conf);      
        param.setConfluenceSpaces(confluenceSpaceLists);
        
        
        List<JenkinsInputParam> jenkinsProjectLists = new ArrayList<>();
        JenkinsInputParam jenkins = new JenkinsInputParam();
        jenkins.setInputActionType(InputActionType.ATTACH_OLD);
        jenkins.setJenkinsProjKey("PPP");
        jenkins.setReferProj("DEMO_STYLE");
        jenkins.setRemark("this is a jenkins");
        jenkinsProjectLists.add(jenkins);
        param.setJenkinsProjects(jenkinsProjectLists);
               
        
        List<JiraProjectInputParam> jiraProjectLists = new ArrayList<>();       
        JiraProjectInputParam jira = new JiraProjectInputParam();
        jira.setInputActionType(InputActionType.UPDATE_OR_NOTHING);
        jira.setReferJiraId(10302l);
        jira.setJiraProjectKey("JIRADEMO");
        jira.setJiraProjectName("jirahahaha");
        jira.setTeamleader("chenyuanjun");
        jira.setJiraProjectDescription("record locally");
        jira.setRemark("again in local");
        jiraProjectLists.add(jira);
        param.setJiraProjects(jiraProjectLists);
                
        
        List<ProjectMemberInputParam> projectMemberLists = new ArrayList<>();
        ProjectMemberInputParam member = new ProjectMemberInputParam();
        member.setRole(ProjectMemberRole.DEV);
        member.setUsername("chenyuanjun");
        projectMemberLists.add(member);
        param.setProjectMembers(projectMemberLists);        
        
        
        List<ScmRepoInputParam> sCMRepoLists = new ArrayList<>();
        ScmRepoInputParam scm = new ScmRepoInputParam();
        scm.setInputActionType(InputActionType.REMOTE_CREATE);
        scm.setRepoName("tryitfree");
        scm.setTemplateId(10l);
        scm.setRemark("hahaha");
        scm.setRepoType(RepoType.SVN);       
        sCMRepoLists.add(scm);
        param.setScmRepositories(sCMRepoLists);         
              
        
        List<ArtifactInputParam> artifactsLists= new ArrayList<>();
        param.setArtifacts(artifactsLists);       
        param.setRemark("Do not edit if you are not admin");

        String requestBody = JSONObject.toJSONString(param);
        // 入参
        System.out.println("-----:" + requestBody);

        Object[] x = {null};

        String result = testRestTemplate.postForObject("/project/toAddProjectX", param, String.class, x);      
        System.out.println(result);
    }
    
}
