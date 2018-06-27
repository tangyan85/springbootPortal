package com.wanda.portal.test;

import com.alibaba.fastjson.JSONObject;
import com.wanda.portal.RootApplication;
import com.wanda.portal.config.biz.ConfluenceConfig;
import com.wanda.portal.config.biz.IdConfig;
import com.wanda.portal.config.biz.JenkinsConfig;
import com.wanda.portal.config.biz.JiraConfig;
import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.*;
import com.wanda.portal.dao.remote.*;
import com.wanda.portal.dto.confluence.GenericConfluenceSpaceDTO;
import com.wanda.portal.dto.jenkins.JenkinsJobDTO;
import com.wanda.portal.dto.jira.GenericJiraProjectDTO;
import com.wanda.portal.dto.svn.SubversionRepoDTO;
import com.wanda.portal.entity.*;
import com.wanda.portal.facade.ProjectController;
import com.wanda.portal.facade.model.input.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class TestCase3 {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JiraConfig jiraConfig;

    @Autowired
    private TestRestTemplate testRestTemplate;
    
    /*
     * 测试带参add Project
     * */
    @Test
    public void tryAddProjectSCM() {

        ProjectInputParam param = new ProjectInputParam();
        param.setProjectKey("DD");
        param.setProjectName("loan");
        param.setRank(13);
        param.setStatus("NORMAL");
        param.setRemark("haha this is good");
        param.setDescription("this is a bad project");
        
        List<ScmRepoInputParam> sCMRepoLists = new ArrayList<>();
        ScmRepoInputParam scm = new ScmRepoInputParam();
        scm.setInputActionType(InputActionType.REMOTE_CREATE);
//        scm.setBrowsingServerIP("10.214.170.65:4434");
//        scm.setBrowsingServerRoot("viewvc");
        scm.setServerIP("10.214.170.65:4434");
//        scm.setServerRoot("svn");
        scm.setRepoName("tryitfree");
        scm.setTemplateId(10l);
        scm.setRemark("hahaha");
        scm.setRepoType(RepoType.SVN);
//        scm.setProtocol(RepoProtocol.SVN);
        sCMRepoLists.add(scm);
        param.setScmRepositories(sCMRepoLists);  
        
        String requestBody = JSONObject.toJSONString(param);
        // 入参
        System.out.println(requestBody);

        Object[] x = {null};
        //String result = testRestTemplate.postForObject("/project/toAddProjectX", param, String.class, x);     
        //System.out.println(result);
        
    }
    
    /*
     * 测试带参添加已有 Project
     * */
//    @Test
//    public void tryAddLocalProjectSCM() {
//
//        ProjectInputParam param = new ProjectInputParam();
//        param.setProjectKey("QQ");
//        param.setProjectName("invest");
//        param.setRank(13);
//        param.setStatus("NORMAL");
//        param.setRemark("hehhe");
//        param.setDescription("this is a money project");
//        
//        List<ScmRepoInputParam> sCMRepoLists = new ArrayList<>();
//        ScmRepoInputParam scm = new ScmRepoInputParam();
//        scm.setInputActionType(InputActionType.ATTACH_OLD);
//        scm.setRepoName("tryitfree");
//        scm.setRemark("hahaha");
//        scm.setRepoType(RepoType.SVN);
//        scm.setBrowsingServerIP("10.214.170.65:4434");
//        scm.setBrowsingServerRoot("viewvc");
//        scm.setProtocol(RepoProtocol.SVN);
//        scm.setRepoStyle("Branch");
//        scm.setServerIP("10.214.170.65:4434");
//        scm.setServerRoot("svn");
//        sCMRepoLists.add(scm);
//        param.setScmRepositories(sCMRepoLists);  
//        
//        String requestBody = JSONObject.toJSONString(param);
//        // 入参
//        System.out.println("param json---" + requestBody);
//
//        Object[] x = {null};
//        String result = testRestTemplate.postForObject("/project/toAddProjectX", param, String.class, x);     
//        System.out.println(result);
//        
//    }
    /*
     * 获得server的信息
     * */
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Test
    public void testServer() {
        System.out.println("ALL the servers are: " + serverRepository.findAll());
        System.out.println("ALL the JIRA servers are: " + serverRepository.findByServerType(ServerType.JIRA));
        System.out.println("The Selected Server is: " + serverRepository.findById(1l));
    }
     
    @Test
    public void testjiraServer() {
//        List<Server> s = serverRepository.findByServerType(ServerType.JIRA);
//        JiraProject jira = new JiraProject();
//        jira.setCreateTime(new Date());
//        jira.setJiraProjectDescription("wwee");
//        jira.setJiraProjectKey("AQS");
//        jira.setJiraProjectName("just a pro");
//        jira.setProject(projectRepository.findById(1l).get());
//        jira.setRemark("blabla");
//        jira.setServer(s.get(0));
//        try {
//            jira = jiraProjectRepository.saveAndFlush(jira);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        System.out.println(jira.getServer().getInnerServerIpAndPort());
//        System.out.println("===starting to purge JIRA AQS");
//        jiraProjectRepository.delete(jira);
    }
    
    @Autowired
    ProjectService projectService;
    @Test
    public void testProjectId() {
        
//       Long ix = Long.valueOf("1"); 
//        
//       Project k = projectService.getProjectById(ix);
//      // ProjectInputParam pip=ConversionUtil.con2ProjectInputParam(k);
//       System.out.println(k.getProjectName());
//       Set<SCMRepo> a = k.getScmRepositories();
//       for (SCMRepo s : a) {
//           System.out.println(s.getRemark());
//       }
       
    }
    @Autowired
    ProjectMemberRepository projectMemberRepository;
    @Test
    public void testProjectMember() {
//        ProjectMember pm = new ProjectMember();
//        pm.setCreateTime(new Date());
//        Project project = projectService.getProjectById(1l);
//        pm.setProject(project );
//        pm.setRole(ProjectMemberRole.DEV);
//        pm.setUsername("huangshiren");
//        ProjectMember zz = projectMemberRepository.saveAndFlush(pm);
//        System.out.println("ProjectMember inserted" + zz.getProjectMemberId());
//        /*-------------------------------------------------------------------*/
//        System.out.println("Begin deleteing the inserted huangshiren");
//        projectMemberRepository.deleteById(zz.getProjectMemberId());
//        
    }

    @Autowired
    IdConfig idConfig;
    @Test
    public void testWhoAmI() {
        System.out.println(idConfig.getWhoami());
    }
    
    @Autowired
    ConfluenceConfig confluenceConfig;
    @Autowired
    JenkinsConfig jenkinsConfig;
    @Test
    public void confluenceConfig() {
        System.out.println(confluenceConfig.getPureIp());
        System.out.println(confluenceConfig.getUrlHead());
        System.out.println(jenkinsConfig.getJsonQueryJobsApi());
        
    }
    
    @Autowired
    ProjectController pj;
    @Test
    public void testFetchAll() {
        Long t1 = System.currentTimeMillis();
        Model model = new BindingAwareModelMap();
        pj.fetchAllConfluences(model);
        pj.fetchAllJenkinses(model);
        pj.fetchAllJiras(model, null);
        pj.fetchAllSvnAndTemplates(model);
        System.out.println(JSONObject.toJSONString(model));
        Long t2 = System.currentTimeMillis();
        System.out.println("Serial time is " + (t2 - t1) + "ms");
    }
    
    @Autowired
    AsyncTaskService asyncTaskService;
    
    @Test
    public void testFetchAllTogether() {
        Long t1 = System.currentTimeMillis();
        Model model1 = new BindingAwareModelMap();
        pj.setCommonServerInfo(model1, null);
        //System.out.println(JSONObject.toJSONString(model1));
        Long t2 = System.currentTimeMillis();
        System.out.println("Serial time is " + (t2 - t1) + "ms");
        
        Long t3 = System.currentTimeMillis();
        Model model2 = new BindingAwareModelMap();
        pj.setCommonServerInfoAsync(model2, null);
        //System.out.println(JSONObject.toJSONString(model));
        Long t4 = System.currentTimeMillis();
        System.out.println("Async time is " + (t4 - t3) + "ms");
        
    }
    
    @Test
    public void testprojectService() { // 测Project的排序
        List<Project> pro = projectService.findAllByRankDesc();
        System.out.println(JSONObject.toJSONString(pro));
    }

    @Autowired
    RepoService repoService;
    @Autowired
    SCMRepoRepository sCMRepoRepository;
    @Test
    public void fetchUnusedSvnRepos() {
        List<Server> svnServers = serverRepository.findByServerType(ServerType.SVN); // 先从db获取svn的所有Server
        repoService.setServer(svnServers.get(0));
        List<SubversionRepoDTO> allSvns = repoService.fetchAllSvnRepos(); // subversion所有的
        List<SCMRepo> usedSvns = sCMRepoRepository.findAll(); // 本地已经有的
        List<ScmRepoInputParam> retSvns = new ArrayList<>();
        Long t1 = System.currentTimeMillis();

        for (Iterator<SubversionRepoDTO> alljira = allSvns.iterator(); alljira.hasNext();) {
            SubversionRepoDTO allJiraDto = (SubversionRepoDTO) alljira.next();
            for (Iterator<SCMRepo> userdjira = usedSvns.iterator(); userdjira.hasNext();) {
                SCMRepo usedJiraProject = (SCMRepo) userdjira.next();
                if (allJiraDto.getName().equalsIgnoreCase(usedJiraProject.getRepoName())) {
                    alljira.remove();
                    userdjira.remove();
                    break;
                }
            }
        }
        for (SubversionRepoDTO svn : allSvns) {
            retSvns.add(svn.getSCMRepo());
        }
        System.out.println("new res = " + JSONObject.toJSONString(retSvns));
        
        Long t2 = System.currentTimeMillis();
        System.out.println("old algo time is " + (t2 - t1) + "ms");
        retSvns = new ArrayList<>();
        Long t3 = System.currentTimeMillis();

        Set<String> usedRepoSet = new HashSet<String>();
        for (SCMRepo rep : usedSvns) {
            usedRepoSet.add(rep.getRepoName());
        }
        for (SubversionRepoDTO svn : allSvns) {
            if (!usedRepoSet.contains(svn.getName())) {
                retSvns.add(svn.getSCMRepo());
            }
        }

        Long t4 = System.currentTimeMillis();
        System.out.println("new algo time is " + (t4 - t3) + "ms");
        System.out.println("new res = " + JSONObject.toJSONString(retSvns));
    }
    
    @Autowired
    JenkinsProjectRepository jenkinsProjectRepository;
    @Autowired
    JenkinsService jenkinsService;
    @Test
    public void fetchUnusedJenkins() {
        List<Server> jenkServers = serverRepository.findByServerType(ServerType.JENKINS); // 先从db获取jenkins的所有Server
        jenkinsService.setServer(jenkServers.get(0));
        List<JenkinsJobDTO> allJenkins = jenkinsService.fetchAllJenkinsJobs();
        List<JenkinsProject> usedJenkins = jenkinsProjectRepository.findAll();
        List<JenkinsInputParam> retJenkins = new ArrayList<>();
        
        Long t1 = System.currentTimeMillis();
        for (Iterator<JenkinsJobDTO> alljira = allJenkins.iterator(); alljira.hasNext();) {
            JenkinsJobDTO allJiraDto = (JenkinsJobDTO) alljira.next();
            for (Iterator<JenkinsProject> userdjira = usedJenkins.iterator(); userdjira.hasNext();) {
                JenkinsProject usedJiraProject = (JenkinsProject) userdjira.next();
                if (allJiraDto.getName().equalsIgnoreCase(usedJiraProject.getJenkinsProjKey())) {
                    alljira.remove();
                    userdjira.remove();
                    break;
                }
            }
        }
        for (JenkinsJobDTO jenkin : allJenkins) {
            retJenkins.add(jenkin.getJenkinsProject());
        }
        System.out.println("old res = " + JSONObject.toJSONString(retJenkins));
        
        Long t2 = System.currentTimeMillis();
        System.out.println("old algo time is " + (t2 - t1) + "ms");
        retJenkins = new ArrayList<>();
        Long t3 = System.currentTimeMillis();
        
        Set<String> usedJenkinsSet = new HashSet<String>();
        for (JenkinsProject usd : usedJenkins) {
            usedJenkinsSet.add(usd.getJenkinsProjKey());
        }
        for (JenkinsJobDTO jenk : allJenkins) {
            if (!usedJenkinsSet.contains(jenk.getName())) {
                retJenkins.add(jenk.getJenkinsProject());
            }
        }
        Long t4 = System.currentTimeMillis();
        System.out.println("new algo time is " + (t4 - t3) + "ms");
        System.out.println("new res = " + JSONObject.toJSONString(retJenkins));
        
    }

    @Autowired
    ConfluenceSpaceRepository confluenceSpaceRepository;
    @Autowired
    ConfluenceService confluenceService;
    @Test
    public void fetchUnusedConfs() {
        List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE); // 先从db获取confluence的所有Server
        confluenceService.setServer(confServers.get(0));
        List<GenericConfluenceSpaceDTO> allConfs = confluenceService.fetchAllConfluenceSpaces();
        List<ConfluenceSpace> usedConfs = confluenceSpaceRepository.findAll();
        List<ConfluenceSpaceInputParam> retConfs = new ArrayList<>();
        Long t1 = System.currentTimeMillis();
        for (Iterator<GenericConfluenceSpaceDTO> alljira = allConfs.iterator(); alljira.hasNext();) {
            GenericConfluenceSpaceDTO allJiraDto = (GenericConfluenceSpaceDTO) alljira.next();
            for (Iterator<ConfluenceSpace> userdjira = usedConfs.iterator(); userdjira.hasNext();) {
                ConfluenceSpace usedJiraProject = (ConfluenceSpace) userdjira.next();
                if (allJiraDto.getKey().equalsIgnoreCase(usedJiraProject.getSpaceKey())) {
                    alljira.remove();
                    userdjira.remove();
                    break;
                }
            }
        }
        for (GenericConfluenceSpaceDTO conf : allConfs) {
            retConfs.add(conf.getConfluenceSpace());
        }
        System.out.println("old res = " + JSONObject.toJSONString(retConfs));
        
        Long t2 = System.currentTimeMillis();
        System.out.println("old algo time is " + (t2 - t1) + "ms");
        retConfs = new ArrayList<>();
        Long t3 = System.currentTimeMillis();
        
        Set<String> usedConfSet = new HashSet<String>();
        for (ConfluenceSpace usd : usedConfs) {
            usedConfSet.add(usd.getSpaceKey());
        }
        for (GenericConfluenceSpaceDTO conf : allConfs) {
            if (!usedConfSet.contains(conf.getKey())) {
                retConfs.add(conf.getConfluenceSpace());
            }
        }
        Long t4 = System.currentTimeMillis();
        System.out.println("new algo time is " + (t4 - t3) + "ms");
        System.out.println("new res = " + JSONObject.toJSONString(retConfs));
    }
    
    @Autowired
    JiraService jiraService;
    @Autowired
    JiraProjectRepository jiraProjectRepository;
    @Test
    public void fetchUnusedJiraProject() {
        List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA); // 先从db获取confluence的所有Server
        jiraService.setServer(jiraServers.get(0));
        List<GenericJiraProjectDTO> allJiras = jiraService.fetchAllJiraProjects(null);
        List<JiraProject> usedJiras = jiraProjectRepository.findAll();
        List<JiraProjectInputParam> retJiras = new ArrayList<>();
        Long t1 = System.currentTimeMillis();
        for (Iterator<GenericJiraProjectDTO> alljira = allJiras.iterator(); alljira.hasNext();) {
            GenericJiraProjectDTO allJiraDto = (GenericJiraProjectDTO) alljira.next();
            for (Iterator<JiraProject> userdjira = usedJiras.iterator(); userdjira.hasNext();) {
                JiraProject usedJiraProject = (JiraProject) userdjira.next();
                if (allJiraDto.getKey().equalsIgnoreCase(usedJiraProject.getJiraProjectKey())) {
                    alljira.remove();
                    userdjira.remove();
                    break;
                }
            }
        }
        for (GenericJiraProjectDTO jira : allJiras) {
            retJiras.add(jira.getJiraProject());
        }
        System.out.println("old res = " + JSONObject.toJSONString(retJiras));
        
        Long t2 = System.currentTimeMillis();
        System.out.println("old algo time is " + (t2 - t1) + "ms");
        retJiras = new ArrayList<>();
        Long t3 = System.currentTimeMillis();
        
        Set<String> usedJirasSet = new HashSet<String>();
        for (JiraProject usd : usedJiras) {
            usedJirasSet.add(usd.getJiraProjectKey());
        }
        for (GenericJiraProjectDTO jira : allJiras) {
            if (!usedJirasSet.contains(jira.getKey())) {
                retJiras.add(jira.getJiraProject());
            }
        }
        Long t4 = System.currentTimeMillis();
        System.out.println("new algo time is " + (t4 - t3) + "ms");
        System.out.println("new res = " + JSONObject.toJSONString(retJiras));
    }
}
