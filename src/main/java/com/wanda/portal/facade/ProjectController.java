package com.wanda.portal.facade;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.wanda.portal.constants.AritifactType;
import com.wanda.portal.constants.InputActionType;
import com.wanda.portal.constants.ProjectMemberRole;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.constants.RepoProtocol;
import com.wanda.portal.constants.RepoType;
import com.wanda.portal.constants.ServerType;
import com.wanda.portal.dao.AsyncTaskService;
import com.wanda.portal.dao.jpa.ProjectRepository;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.ConfluenceService;
import com.wanda.portal.dao.remote.JenkinsService;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dao.remote.RepoService;
import com.wanda.portal.dto.common.CommonHttpResponseBody;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;
import com.wanda.portal.facade.model.input.JenkinsInputParam;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import com.wanda.portal.utils.ConversionUtil;

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
    AsyncTaskService asyncTaskService;
    
	@RequestMapping("/toAdd")    
    public String toAdd(Model model){  
		logger.info("------Current path:/project/toAdd");
		model=setModelCommon(model);
		model=setCommonServerInfoAsync(model);
		
        return "project/toAdd";
    } 
	
	@RequestMapping("/findAllProjects/{optype}")
    public String findAllProjects(Model model,@PathVariable("optype") String optype){   
		logger.info("------Current path:/project/findAllProjects");
		List<Project> projects=projectService.findAllByRankDesc();
		model.addAttribute("projects",projects);
		if(StringUtils.isNotEmpty(optype)){
			model.addAttribute("optype", optype);
		}
		model.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
		
        return "project/findAllProjects";
    }  
	
	@RequestMapping("/projects/{projectId}/{optype}") 
    public String toEdit(Model model,@PathVariable("projectId") String projectId,@PathVariable("optype") String optype){   
		logger.info("------Current path:/project/projects"+projectId);
	
		if(StringUtils.isNotEmpty(optype)){
			model.addAttribute("optype", optype);
		}
		
		Project project=projectService.getProjectById(Long.valueOf(projectId));
		model.addAttribute("project", ConversionUtil.Con2Project(project));
		
		model=setModelCommon(model);
		model=setCommonServerInfoAsync(model);
		
		
		
		logger.debug("------Current path:/project/toAddProject:"+project);
		if(StringUtils.isNotEmpty(optype)&&optype.equalsIgnoreCase("end")){
			 return "project/toEnd";
		}
        return "project/toEdit";
	}

	/**
	 * 加载慢，需要优化
	 * @param model
	 * @return
	 */
	@RequestMapping("/toImport") 
    public String toImport(Model model){   
		logger.info("------Current path:/project/toImport");
		model=setModelCommon(model);
		model=setCommonServerInfoAsync(model);

        return "project/toImport";
	} 
	
	@RequestMapping(value="/toAddProjectTest",method = RequestMethod.POST)
	public String toAddProjectTest(Model model,@ModelAttribute(value = "project") Project project) throws Exception{
		projectService.createProject(project);
		return "forward:/";
	}
	
    @RequestMapping(value = "/toViewProject/{projectId}", method = RequestMethod.GET)
    public String toViewProject(Model model, @PathVariable("projectId") String projectId) {
        System.out.println("toViewProject:" + projectId);
        Project project = new Project();
        try {
        	project = projectService.getProjectById(Long.valueOf(projectId)); 
        } catch (Exception e) {
        	e.printStackTrace();
        }
        model.addAttribute("project",project);

        return "project/toView";
    }

	
	@RequestMapping(value="/toEditProject",method = RequestMethod.POST)
	@ResponseBody
	public CommonHttpResponseBody  toEditProject(Model model, @RequestBody ProjectInputParam projectInputParam, RedirectAttributes redirectAttributes){
		 System.out.println("input" +JSON.toJSONString(projectInputParam));
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
    	 System.out.println("input" +JSON.toJSONString(projectInputParam));
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
        System.out.println("toEndProject:" + projectId);
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        String ret="success";
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
        System.out.println("input" +JSON.toJSONString(projectInputParam));
        Project project;
        try {
            project = projectService.createProject(projectInputParam); // 调远程服务创建
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
	
    @RequestMapping(value="/toEditProjectX",method = RequestMethod.POST)
    @ResponseBody
    public CommonHttpResponseBody toEditProjectX(Model model, @RequestBody ProjectInputParam projectInputParam, RedirectAttributes redirectAttributes){
        CommonHttpResponseBody response = CommonHttpResponseBody.packSuccess();
        System.out.println("input" +JSON.toJSONString(projectInputParam));
        try {
            Project project = projectService.updateProject(projectInputParam); // 调远程服务创建
        } catch (Exception e) {
            System.out.println(e);
            response.setResponseCode(CommonHttpResponseBody.FAIL_CODE);
            response.setResponseMsg(e.getMessage());
            return response;
        }

        redirectAttributes.addFlashAttribute("message", "操作成功");
        logger.info("------Current path:/project/toAddProject:操作成功");
        return response;
    }
    
    
    //通过配合@ResponseBody来将内容或者对象作为HTTP响应正文返回（适合做即时校验）；  
    @RequestMapping(value = "/valid", method = RequestMethod.GET)  
    @ResponseBody  
    public String valid(@RequestParam(value = "userId", required = false) Integer userId,  
            @RequestParam(value = "name") String name) {  
        return String.valueOf(true);  
    }  

    private Model setModelCommon(Model model){
    	Model m=model;
    	m.addAttribute("repoTypes", EnumSet.allOf(RepoType.class));
		m.addAttribute("protocols", EnumSet.allOf(RepoProtocol.class));
		m.addAttribute("aritifactTypes", EnumSet.allOf(AritifactType.class));
		m.addAttribute("projectMemberRoles", EnumSet.allOf(ProjectMemberRole.class));
		m.addAttribute("inputActionTypes", EnumSet.allOf(InputActionType.class));
		m.addAttribute("projectStatus", EnumSet.allOf(ProjectStatus.class));
		m.addAttribute("serverIPs",  serverRepository.findAll());
		return m;
    }
    
    public Model setCommonServerInfo(Model model){   	
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

    /*
     * 上述代码的线程池并行改造
     * */
    public Model setCommonServerInfoAsync(Model model){      
        try {
            List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA); // 先从db中获取jira的所有Server
            Future<List<JiraProjectInputParam>> existJiras = asyncTaskService.fetchAllJiras(jiraServers); // 再轮询外部jira，过滤         
            List<Server> confServers = serverRepository.findByServerType(ServerType.CONFLUENCE); // 先从db中获取confluence的所有Server
            Future<List<ConfluenceSpaceInputParam>> existConfs = asyncTaskService.fetchAllConfluences(confServers); // 再轮询外部confluence，过滤         
            List<Server> jenkinsServers = serverRepository.findByServerType(ServerType.JENKINS); // 先从db中获取jenkins的所有Server
            Future<List<JenkinsInputParam>> existJenkinses = asyncTaskService.fetchAllJenkinses(jenkinsServers); // 再轮询外部jenkins，过滤        
            List<Server> svnServers = serverRepository.findByServerType(ServerType.SVN); // 先从db获取svn的所有Server
            Future<List<ScmRepoInputParam>> existSvns = asyncTaskService.fetchAllSvnRepos(svnServers); // 轮询外部svn，过滤         
            Future<List<ScmRepoInputParam>> svnTemplates = asyncTaskService.fetchAllSvnTemplates(svnServers); // template似乎要去重
            
            model.addAttribute("existJiras", existJiras.get());
            model.addAttribute("existConfs", existConfs.get());
            model.addAttribute("existJenkins", existJenkinses.get());
            model.addAttribute("existSvns", existSvns.get());
            model.addAttribute("svnTemplates", svnTemplates.get());
        } catch (InterruptedException e) {
            return model;
        } catch (ExecutionException e) {
            return model;
        } 
            return model;
    }
    
    public void fetchAllJiras(Model model) {
        List<Server> jiraServers = serverRepository.findByServerType(ServerType.JIRA);
        List<JiraProjectInputParam> existJiras = new ArrayList<>();
    	if (jiraServers != null && jiraServers.size() > 0) {
    	    for (Server jiraServer : jiraServers) {
    	        jiraService.setServer(jiraServer);
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
    
    public void fetchAllJenkinses(Model model) {
        List<Server> jenkinsServers = serverRepository.findByServerType(ServerType.JENKINS);
        List<JenkinsInputParam> existJenkinses = new ArrayList<>();
        if (jenkinsServers != null && jenkinsServers.size() > 0) {
            for (Server jenkinsServer : jenkinsServers) {
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
