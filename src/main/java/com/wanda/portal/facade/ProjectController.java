package com.wanda.portal.facade;

import com.alibaba.fastjson.JSON;
import com.wanda.portal.constants.*;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.ProjectRepository;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.*;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.dto.jira.JiraProjectVersionDTO;
import com.wanda.portal.entity.JiraProject;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.ProjectMember;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.*;
import com.wanda.portal.security.SecurityConfigCrowd;
import com.wanda.portal.utils.ConversionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/project")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    ProjectService projectService;
    @Autowired
    JiraService jiraService;
    @Autowired
    ConfluenceService confluenceService;
    @Autowired
    JenkinsService jenkinsService;
    @Autowired
    RepoService repoService;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    ProjectMemberService projectMemberService;

    @Autowired
    AsyncTaskService asyncTaskService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/toAdd")
    public String toAdd(Model model, HttpSession session) {
        logger.info("------Current path:/project/toAdd");
        setModelCommon(model);
        UserDetails user = (UserDetails) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
        setCommonServerInfoAsync(model, user);

        return "project/toAdd";
    }

    @RequestMapping("/toAdd2")
    public String toAdd2(Model model, HttpSession session) {
        logger.info("------Current path:/project/toAdd2");
        setModelCommon(model);
        UserDetails user = (UserDetails) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
        setCommonServerInfoAsync(model, user);

        return "project/toAdd2";
    }

    @RequestMapping("/rename")
    @ResponseBody
    public String rename(String projectId, String projectKey, String projectName) {
        if (StringUtils.isEmpty(projectId)) {
            return "该项目不存在或已删除！";
        }

        Project project = projectService.getProjectById(Long.valueOf(projectId));

        if (StringUtils.isNotEmpty(projectKey)) {
            List<Project> projects = projectService.findByProjectKey(projectKey);
            if (projects.size() > 0) {
                return "项目英文名称已存在！";
            }
            project.setProjectKey(projectKey);
        }

        if (StringUtils.isNotEmpty(projectName)) {
            List<Project> projects = projectService.findByProjectName(projectName);
            if (projects.size() > 0) {
                return "项目中文名称已存在！";
            }
            project.setProjectName(projectName);
        }

        projectService.updateProject(project);
        return "ok";
    }

    @RequestMapping("/findAllProjects/{optype}")
    public String findAllProjects(Model model, @PathVariable("optype") String optype) {
        logger.info("------Current path:/project/findAllProjects");
        List<Project> projects = projectService.findAllByRankDesc();
        model.addAttribute("projects", projects);
        if (StringUtils.isNotEmpty(optype)) {
            model.addAttribute("optype", optype);
        }
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));

        return "project/findAllProjects";
    }

    @RequestMapping("/toList")
    public String toList(Model model, String projectId) {
        logger.info("------Current path:/project/toList");
        Sort sort = new Sort(Sort.Direction.DESC, "rank", "createTime");
        model.addAttribute("projects", projectService.findAll(PageRequest.of(0, 10, sort)));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        model.addAttribute("projectId", projectId);
        return "project/toList";
    }

    @RequestMapping(value = "/preview/{projectId}")
    public String preview(Model model, @PathVariable("projectId") String projectId) {
        logger.info("------Current path:/project/preview");
        Project project = new Project();
        try {
            project = projectService.getProjectById(Long.valueOf(projectId));
            if (project != null) {
                Set<JiraProject> jiraProjects = project.getJiraProjects();
                for (JiraProject jiraProject : jiraProjects) {
                    Long versionExpire = redisTemplate.getExpire(jiraProject.getJiraProjectKey() + "_versions");
                    List<JiraProjectVersionDTO> versions;

                    List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
                    for (Server server : jiraServers) {
                        if (jiraProject.getWebui().contains(server.getOuterServerIpAndPort())) {
                            jiraService.setServer(server);
                            break;
                        }
                    }

                    if (versionExpire <= 0) {
                        versions = jiraService.fetchProjectVersions(jiraProject.getJiraProjectKey());
                        if (versions.size() > 0) {
                            redisTemplate.opsForList().leftPush(jiraProject.getJiraProjectKey() + "_versions", versions);
                            redisTemplate.expire(jiraProject.getJiraProjectKey() + "_versions", 5, TimeUnit.MINUTES);
                        }
                    } else {
                        versions = (List<JiraProjectVersionDTO>) redisTemplate.opsForList().leftPop(jiraProject.getJiraProjectKey() + "_versions");
                    }

                    jiraProject.setProjectVersions(versions);

                    Long allIssuesExpire = redisTemplate.getExpire(jiraProject.getJiraProjectKey() + "_allIssues");
                    Integer allIssues;

                    if (allIssuesExpire <= 0) {
                        allIssues = jiraService.fetchProjectAllIssues(jiraProject.getJiraProjectKey());
                        if (allIssues != null) {
                            redisTemplate.opsForList().leftPush(jiraProject.getJiraProjectKey() + "_allIssues", allIssues);
                            redisTemplate.expire(jiraProject.getJiraProjectKey() + "_allIssues", 3, TimeUnit.MINUTES);
                        }
                    } else {
                        allIssues = (Integer) redisTemplate.opsForList().leftPop(jiraProject.getJiraProjectKey() + "_allIssues");
                    }

                    jiraProject.setAllIssues(allIssues);

                    Long finishIssuesExpire = redisTemplate.getExpire(jiraProject.getJiraProjectKey() + "_finishIssues");
                    Integer finishIssues;
                    if (finishIssuesExpire <= 0) {
                        finishIssues = jiraService.fetchProjectAllIssues(jiraProject.getJiraProjectKey());
                        if (finishIssues != null) {
                            redisTemplate.opsForList().leftPush(jiraProject.getJiraProjectKey() + "_finishIssues", finishIssues);
                            redisTemplate.expire(jiraProject.getJiraProjectKey() + "_finishIssues", 3, TimeUnit.MINUTES);
                        }
                    } else {
                        finishIssues = (Integer) redisTemplate.opsForList().leftPop(jiraProject.getJiraProjectKey() + "_finishIssues");
                    }

                    jiraProject.setFinishIssues(finishIssues);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("project", project);

        return "project/toDetail";
    }

    @ResponseBody
    @RequestMapping(value = "/member/{projectMemberId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProjectMember page(@PathVariable("projectMemberId") Long projectMemberId) {
        return projectMemberService.findByProjectMemberId(projectMemberId);
    }

    @ResponseBody
    @RequestMapping(value = "/page/{page}/{size}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<Project> page(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        return projectService.findAll(PageRequest.of(page - 1, size));
    }

    @ResponseBody
    @RequestMapping(value = "/pull", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Project> pull(String search) {
        return projectService.findByProjectNameLike("%" + search + "%");
    }

    @RequestMapping("/projects/{projectId}/{optype}")
    public String toEdit(Model model, @PathVariable("projectId") String projectId,
                         @PathVariable("optype") String optype, HttpSession session) {
        logger.info("------Current path:/project/projects" + projectId);

        if (StringUtils.isNotEmpty(optype)) {
            model.addAttribute("optype", optype);
        }

        Project project = projectService.getProjectById(Long.valueOf(projectId));
        model.addAttribute("project", ConversionUtil.Con2Project(project));

        setModelCommon(model);
        UserDetails user = (UserDetails) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
        setCommonServerInfoAsync(model, user);


        logger.debug("------Current path:/project/toAddProject:" + project);
        if (StringUtils.isNotEmpty(optype) && optype.equalsIgnoreCase("end")) {
            return "project/toEnd";
        }
        return "project/toEdit";
    }

    /**
     * 加载慢，需要优化
     *
     * @param model
     * @return
     */
    @RequestMapping("/toImport")
    public String toImport(Model model, HttpSession session) {
        logger.info("------Current path:/project/toImport");
        setModelCommon(model);
        UserDetails user = (UserDetails) session.getAttribute(SecurityConfigCrowd.SESSION_KEY);
        setCommonServerInfoAsync(model, user);

        return "project/toImport";
    }

    @RequestMapping(value = "/toAddProjectTest", method = RequestMethod.POST)
    public String toAddProjectTest(Model model, @ModelAttribute(value = "project") Project project) throws Exception {
        projectService.createProject(project);
        return "forward:/";
    }

    @RequestMapping(value = "/toViewProject/{projectId}", method = RequestMethod.GET)
    public String toViewProject(Model model, @PathVariable("projectId") String projectId) {
        logger.info("toViewProject:" + projectId);
        Project project = new Project();
        try {
            project = projectService.getProjectById(Long.valueOf(projectId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("project", project);

        return "project/toView";
    }


    @RequestMapping(value = "/toEditProject", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody toEditProject(Model model, @RequestBody ProjectInputParam projectInputParam, RedirectAttributes redirectAttributes) {
        logger.info("input" + JSON.toJSONString(projectInputParam));
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        Project project;
        try {
            project = projectService.updateProject(projectInputParam); // 调远程服务创建
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }

        //projectRepository.save(project); // 全部成功才持久化
        redirectAttributes.addFlashAttribute("message", "操作成功");
        logger.info("------Current path:/project/toEditProject:操作成功");

        return response;
    }

    @RequestMapping(value = "/toImportProject", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody toImportProject(Model model, @RequestBody ProjectInputParam projectInputParam,
                                                  RedirectAttributes redirectAttributes) {
        logger.info("input" + JSON.toJSONString(projectInputParam));
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        Project project;
        try {
            project = projectService.createProject(projectInputParam); // 调远程服务创建
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        // project = projectInputParam.prepareToPesist();
        //projectRepository.save(project); // 全部成功才持久化
        redirectAttributes.addFlashAttribute("message", "操作成功");
        logger.info("------Current path:/project/toAddProject:操作成功");

        return response;
    }

    @RequestMapping(value = "/toEndProject/{projectId}", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody toEndProject(Model model, @PathVariable("projectId") String projectId) {
        logger.info("toEndProject:" + projectId);
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        String ret = "success";
        try {
            ret = projectService.endProject(Long.valueOf(projectId));
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        logger.info("------Current path:/project/toAddProject:操作成功");

        return response;
    }

    @RequestMapping(value = "/toAddProjectX", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody toAddProjectX(Model model, @RequestBody ProjectInputParam projectInputParam,
                                                RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        logger.info("input" + JSON.toJSONString(projectInputParam));
        Project project;
        try {
            // 调远程服务创建
            project = projectService.createProject(projectInputParam);
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        // projectRepository.save(project); // 全部成功才持久化
        redirectAttributes.addFlashAttribute("message", "操作成功");
        logger.info("------Current path:/project/toAddProject:操作成功");
        return response;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody save(@RequestBody ProjectInputParam projectInputParam,
                                       RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        logger.info("input" + JSON.toJSONString(projectInputParam));
        Project project;
        try {
            // 调远程服务创建
            project = projectService.createProject(projectInputParam);
            response.setResponseMsg(project != null ? project.getProjectId().toString() : "");
        } catch (Exception e) {
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }
        logger.info("------Current path:/project/save:操作成功");
        return response;
    }

    @RequestMapping(value = "/toEditProjectX", method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody toEditProjectX(Model model, @RequestBody ProjectInputParam projectInputParam, RedirectAttributes redirectAttributes) {
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        logger.info("input" + JSON.toJSONString(projectInputParam));
        try {
            // 调远程服务创建
            Project project = projectService.updateProject(projectInputParam);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }

        redirectAttributes.addFlashAttribute("message", "操作成功");
        logger.info("------Current path:/project/toAddProject:操作成功");
        return response;
    }


    /**
     * 通过配合@ResponseBody来将内容或者对象作为HTTP响应正文返回（适合做即时校验）；
     */
    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    @ResponseBody
    public String valid(@RequestParam(value = "userId", required = false) Integer userId,
                        @RequestParam(value = "name") String name) {
        return String.valueOf(true);
    }

    private void setModelCommon(Model model) {
        model.addAttribute("repoTypes", EnumSet.allOf(RepoType.class));
        model.addAttribute("protocols", EnumSet.allOf(RepoProtocol.class));
        model.addAttribute("aritifactTypes", EnumSet.allOf(AritifactType.class));
        model.addAttribute("projectMemberRoles", EnumSet.allOf(ProjectMemberRole.class));
        model.addAttribute("inputActionTypes", EnumSet.allOf(InputActionType.class));
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        model.addAttribute("serverIPs", serverRepository.findAll());
    }

    public Model setCommonServerInfo(Model model, UserDetails user) {
        fetchAllJiras(model, user);
        //创建项目时需要选择已有项目id来进行创建
        fetchAllConfluences(model);
        //创建项目时需要新建key来进行创建
        fetchAllJenkinses(model, user);
        //创建项目时需要选择已有项目key来进行创建
        fetchAllSvnAndTemplates(model);
        //创建项目时需要选择已有项目template来进行创建
        return model;
    }

    /**
     * 上述代码的线程池并行改造
     *
     * @param model
     */
    public void setCommonServerInfoAsync(Model model, UserDetails user) {
        try {
            // 先从db中获取jira的所有Server
            List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
            // 再轮询外部jira，过滤
            Future<List<JiraProjectInputParam>> existJiras = asyncTaskService.fetchAllJiras(jiraServers, user);
            // 先从db中获取confluence的所有Server
            List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE);
            // 再轮询外部confluence，过滤
            Future<List<ConfluenceSpaceInputParam>> existConfs = asyncTaskService.fetchAllConfluences(confServers);
            // 先从db中获取jenkins的所有Server
            List<Server> jenkinsServers = serverRepository.findByServerType(ServerType.JENKINS);
            // 再轮询外部jenkins，过滤
            Future<List<JenkinsInputParam>> existJenkinses = asyncTaskService.fetchAllJenkinses(jenkinsServers, user);
            // 先从db获取svn的所有Server
            List<Server> svnServers = serverRepository.findByServerType(ServerType.SVN);
            // 轮询外部svn，过滤
            Future<List<ScmRepoInputParam>> existSvns = asyncTaskService.fetchAllSvnRepos(svnServers);
            // template似乎要去重
            Future<List<ScmRepoInputParam>> svnTemplates = asyncTaskService.fetchAllSvnTemplates(svnServers);

            model.addAttribute("existJiras", existJiras.get());
            model.addAttribute("existConfs", existConfs.get());
            model.addAttribute("existJenkins", existJenkinses.get());
            model.addAttribute("existSvns", existSvns.get());
            model.addAttribute("svnTemplates", svnTemplates.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void fetchAllJiras(Model model, UserDetails user) {
        List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
        List<JiraProjectInputParam> existJiras = new ArrayList<>();

        if (jiraServers != null && jiraServers.size() > 0) {
            for (Server jiraServer : jiraServers) {
                jiraService.setServer(jiraServer);
                if (JiraConstants.LOGIN_MODE.CURR_USER.getModeCode().equals(jiraServer.getLoginMode())) {
                    jiraServer.setLoginName(user.getUsername());
                    jiraServer.setPasswd(user.getPassword());
                }
                existJiras.addAll(jiraService.fetchUnusedJiraProject());
            }
        }
        model.addAttribute("existJiras", existJiras);
    }

    public void fetchAllConfluences(Model model) {
        List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE);
        List<ConfluenceSpaceInputParam> existConfs = new ArrayList<>();
        if (confServers != null && confServers.size() > 0) {
            for (Server confServer : confServers) {
                confluenceService.setServer(confServer);
                existConfs.addAll(confluenceService.fetchUnusedConfs());
            }
        }
        model.addAttribute("existConfs", existConfs);
    }

    public void fetchAllJenkinses(Model model, UserDetails user) {
        List<Server> jenkinsServers = serverRepository.findByServerType(ServerType.JENKINS);
        List<JenkinsInputParam> existJenkinses = new ArrayList<>();
        if (jenkinsServers != null && jenkinsServers.size() > 0) {
            for (Server jenkinsServer : jenkinsServers) {
                if (JiraConstants.LOGIN_MODE.CURR_USER.getModeCode().equals(jenkinsServer.getLoginMode())) {
                    jenkinsServer.setLoginName(user.getUsername());
                    jenkinsServer.setPasswd(user.getPassword());
                }
                jenkinsService.setServer(jenkinsServer);
                existJenkinses.addAll(jenkinsService.fetchUnusedJekins());
            }
        }
        model.addAttribute("existJenkins", existJenkinses);
    }

    public void fetchAllSvnAndTemplates(Model model) {
        List<Server> svnServers = serverRepository.findByServerType(ServerType.SVN);
        List<ScmRepoInputParam> existSvns = new ArrayList<>();
        List<ScmRepoInputParam> svnTemplates = new ArrayList<>();
        if (svnServers != null && svnServers.size() > 0) {
            for (Server svnServer : svnServers) {
                repoService.setServer(svnServer);
                existSvns.addAll(repoService.fetchUnusedSvnRepos());
                svnTemplates.addAll(repoService.fetchAllTemplates());
            }
        }
        model.addAttribute("existSvns", existSvns);
        model.addAttribute("svnTemplates", svnTemplates);
    }
}
