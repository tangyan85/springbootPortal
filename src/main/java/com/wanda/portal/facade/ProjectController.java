package com.wanda.portal.facade;

import com.alibaba.fastjson.JSON;
import com.wanda.portal.constants.*;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.ProjectRepository;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.*;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.dto.jira.JiraProjectVersionDTO;
import com.wanda.portal.entity.*;
import com.wanda.portal.facade.model.input.*;
import com.wanda.portal.utils.ConversionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
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

@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
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
    public String toAdd(Model model) {
        logger.info("------Current path:/project/toAdd");
        setModelCommon(model);
        setCommonServerInfoAsync(model);

        return "project/toAdd";
    }

    @RequestMapping("/toAdd2")
    public String toAdd2(Model model) {
        logger.info("------Current path:/project/toAdd2");
        setModelCommon(model);
        setCommonServerInfoAsync(model);

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
    public String toList(Model model, String projectId, HttpSession session) {
        logger.info("------Current path:/project/toList");
        model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
        model.addAttribute("projectId", projectId);
        return "project/toList";
    }

    @RequestMapping(value = "/preview/{projectId}")
    public String preview(Model model, @PathVariable("projectId") String projectId, String backPath) {
        logger.info("------Current path:/project/preview");
        Project project = new Project();

        try {
            project = projectService.getProjectById(Long.valueOf(projectId));
            if (project != null) {
                Set<JiraProject> jiraProjects = project.getJiraProjects();
                for (JiraProject jiraProject : jiraProjects) {
                    Server server = null;
                    List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
                    for (Server s : jiraServers) {
                        if (jiraProject.getWebui().contains(s.getDomain())) {
                            server = s;
                            break;
                        }
                    }
                    setJiraProject(jiraProject, server);
                }

                Set<ConfluenceSpace> confProjects = project.getConfluenceSpaces();
                for (ConfluenceSpace confProject : confProjects) {
                    Server server = null;
                    List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE);
                    for (Server s : confServers) {
                        if (confProject.getWebui().contains(s.getDomain())) {
                            server = s;
                            break;
                        }
                    }
                    logger.info("preview() server:" + server.getLoginName());
                    setConfProject(confProject, server);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("project", project);
        model.addAttribute("backPath", backPath);

        return "project/toDetail";
    }

    private void setJiraProject(JiraProject jiraProject, Server server) {
        String allIssuesLink = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + "/issues/?jql=project %3D" + jiraProject.getJiraProjectKey();
        String finishIssuesLink = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + "/issues/?jql=project %3D" + jiraProject.getJiraProjectKey() + " AND status %3D 5";
        String versionLink = server.getProtocol() + "://" + server.getOuterServerIpAndPort()
                + "/issues/?jql=project %3D" + jiraProject.getJiraProjectKey() + " AND affectedVersion  %3D ";

        jiraProject.setAllIssuesLink(allIssuesLink);
        jiraProject.setFinishIssuesLink(finishIssuesLink);
        String versionsKey = jiraProject.getJiraProjectKey() + "_versions";
        List<JiraProjectVersionDTO> versions = getForCache(versionsKey, () -> jiraService.fetchProjectVersions(jiraProject.getJiraProjectKey(), server));
        for (JiraProjectVersionDTO dto : versions) {
            dto.setUrl(versionLink + dto.getName());
        }
        jiraProject.setProjectVersions(versions);

        String allIssuesKey = jiraProject.getJiraProjectKey() + "_allIssues";
        Integer allIssues = getForCache(allIssuesKey, () -> jiraService.fetchProjectAllIssues(jiraProject.getJiraProjectKey(), server));
        jiraProject.setAllIssues(allIssues);

        String finishIssuesKey = jiraProject.getJiraProjectKey() + "_finishIssues";
        Integer finishIssues = getForCache(finishIssuesKey, () -> jiraService.fetchProjectAllIssues(jiraProject.getJiraProjectKey(), server));
        jiraProject.setFinishIssues(finishIssues);
    }

    private void setConfProject(ConfluenceSpace confProject, Server server) {
        String allPagesLink, createPagesLink, modifyPagesLink;
        allPagesLink = server.getProtocol() + "://" + server.getDomain()
                + "/collector/pages.action?key=" + confProject.getSpaceKey();
        createPagesLink = server.getProtocol() + "://" + server.getDomain()
                + "/dosearchsite.action?cql=space+%3D+\"" + confProject.getSpaceKey() + "\"+and+created+>%3D+now(%27-1d%27)";
        modifyPagesLink = server.getProtocol() + "://" + server.getDomain()
                + "/dosearchsite.action?cql=space+%3D+\"" + confProject.getSpaceKey() + "\"+and+lastmodified+>%3D+now(%27-1d%27)";
        confProject.setAllPagesLink(allPagesLink);
        confProject.setCreatePagesLink(createPagesLink);
        confProject.setModifyPagesLink(modifyPagesLink);

        String allPagesKey = confProject.getSpaceKey() + "_allPages";
        Integer allPages = getForCache(allPagesKey, () -> confluenceService.fetchAllPages(confProject.getSpaceKey(), server));
        confProject.setAllPages(allPages);

        String createPagesKey = confProject.getSpaceKey() + "_createPages";
        Integer createPages = getForCache(createPagesKey, () -> confluenceService.fetchAllPagesByCreated(confProject.getSpaceKey(), server));
        confProject.setCreatePages(createPages);

        String modifyPagesKey = confProject.getSpaceKey() + "_modifyPages";
        Integer modifyPages = getForCache(modifyPagesKey, () -> confluenceService.fetchAllPagesByModified(confProject.getSpaceKey(), server));
        confProject.setModifyPages(modifyPages);
    }

    @ResponseBody
    @RequestMapping(value = "/member/{projectMemberId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProjectMember page(@PathVariable("projectMemberId") Long projectMemberId) {
        return projectMemberService.findByProjectMemberId(projectMemberId);
    }

    @ResponseBody
    @RequestMapping(value = "/page/{page}/{size}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Page<Project> page(@PathVariable("page") Integer page, @PathVariable("size") Integer size, String status) {
        Page<Project> pages = projectService.findAll(PageRequest.of(page - 1, size), status);
        if (StringUtils.isNotEmpty(status))  {
            for (Project project : pages) {
                if (project != null) {
                    Set<JiraProject> jiraProjects = project.getJiraProjects();
                    for (JiraProject jiraProject : jiraProjects) {
                        List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
                        for (Server s : jiraServers) {
                            if (jiraProject.getWebui().contains(s.getDomain())) {
                                setJiraProject(jiraProject, s);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return pages;
    }

    @ResponseBody
    @RequestMapping(value = "/pull", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Project> pull(String search) {
        return projectService.findByProjectNameLike("%" + search + "%");
    }

    @RequestMapping("/projects/{projectId}/{optype}")
    public String toEdit(Model model, @PathVariable("projectId") String projectId,
                         @PathVariable("optype") String optype) {
        logger.info("------Current path:/project/projects" + projectId);

        if (StringUtils.isNotEmpty(optype)) {
            model.addAttribute("optype", optype);
        }

        Project project = projectService.getProjectById(Long.valueOf(projectId));
        model.addAttribute("project", ConversionUtil.Con2Project(project));

        setModelCommon(model);
        setCommonServerInfoAsync(model);


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
    public String toImport(Model model) {
        logger.info("------Current path:/project/toImport");
        setModelCommon(model);
        setCommonServerInfoAsync(model);

        return "project/toImport";
    }

    @RequestMapping(value = "/toAddProjectTest", method = RequestMethod.POST)
    public String toAddProjectTest(@ModelAttribute(value = "project") Project project) throws Exception {
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

    public Model setCommonServerInfo(Model model) {
        fetchAllJiras(model);
        //创建项目时需要选择已有项目id来进行创建
        fetchAllConfluences(model);
        //创建项目时需要新建key来进行创建
        fetchAllJenkinses(model);
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
    public void setCommonServerInfoAsync(Model model) {
        try {
            // 先从db中获取jira的所有Server
            List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
            // 再轮询外部jira，过滤
            Future<List<JiraProjectInputParam>> existJiras = asyncTaskService.fetchAllJiras(jiraServers);
            // 先从db中获取confluence的所有Server
            List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE);
            // 再轮询外部confluence，过滤
            Future<List<ConfluenceSpaceInputParam>> existConfs = asyncTaskService.fetchAllConfluences(confServers);
            // 先从db中获取jenkins的所有Server
            List<Server> jenkinsServers = serverRepository.findByServerType(ServerType.JENKINS);
            // 再轮询外部jenkins，过滤
            Future<List<JenkinsInputParam>> existJenkinses = asyncTaskService.fetchAllJenkinses(jenkinsServers);
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

    public void fetchAllJiras(Model model) {
        List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
        List<JiraProjectInputParam> existJiras = new ArrayList<>();

        if (jiraServers != null && jiraServers.size() > 0) {
            for (Server jiraServer : jiraServers) {
                existJiras.addAll(jiraService.fetchUnusedJiraProject(jiraServer));
            }
        }
        model.addAttribute("existJiras", existJiras);
    }

    public void fetchAllConfluences(Model model) {
        List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE);
        List<ConfluenceSpaceInputParam> existConfs = new ArrayList<>();
        if (confServers != null && confServers.size() > 0) {
            for (Server confServer : confServers) {
                existConfs.addAll(confluenceService.fetchUnusedConfs(confServer));
            }
        }
        model.addAttribute("existConfs", existConfs);
    }

    public void fetchAllJenkinses(Model model) {
        List<Server> jenkinsServers = serverRepository.findByServerType(ServerType.JENKINS);
        List<JenkinsInputParam> existJenkinses = new ArrayList<>();
        if (jenkinsServers != null && jenkinsServers.size() > 0) {
            for (Server jenkinsServer : jenkinsServers) {
                existJenkinses.addAll(jenkinsService.fetchUnusedJekins(jenkinsServer));
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
                existSvns.addAll(repoService.fetchUnusedSvnRepos(svnServer));
                svnTemplates.addAll(repoService.fetchAllTemplates(svnServer));
            }
        }
        model.addAttribute("existSvns", existSvns);
        model.addAttribute("svnTemplates", svnTemplates);
    }

    @Scheduled(initialDelay = 5000, fixedRate = 180000)
    public void jiraCache() {
        logger.info("jira cache start...");
        try {
            List<JiraProject> jiraProjects = jiraService.findAll();
            for (JiraProject jiraProject : jiraProjects) {
                Server server = null;
                List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
                for (Server s : jiraServers) {
                    if (jiraProject.getWebui().contains(s.getDomain())) {
                        server = s;
                        server.setAuthAdmin(true);
                        break;
                    }
                }
                logger.info("cache jira: " + jiraProject.getWebui());
                setJiraProject(jiraProject, server);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("jira cache end...");
    }

    @Scheduled(initialDelay = 10000, fixedRate = 180000)
    public void confluenceCache() {
        logger.info("confluence cache start...");
        try {
            List<ConfluenceSpace> confProjects = confluenceService.findAll();
            for (ConfluenceSpace confProject : confProjects) {
                Server server = null;
                List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE);
                for (Server s : confServers) {
                    if (confProject.getWebui().contains(s.getDomain())) {
                        server = s;
                        server.setAuthAdmin(true);
                        break;
                    }
                }
                logger.info("cache confluence: " + confProject.getWebui());
                setConfProject(confProject, server);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("confluence cache end...");
    }


}
