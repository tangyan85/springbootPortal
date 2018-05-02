package com.wanda.portal.dao.remote.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.wanda.portal.config.biz.ConfluenceConfig;
import com.wanda.portal.config.biz.JenkinsConfig;
import com.wanda.portal.config.biz.JiraConfig;
import com.wanda.portal.config.biz.SvnConfig;
import com.wanda.portal.constants.AritifactType;
import com.wanda.portal.constants.InputActionType.RESULT;
import com.wanda.portal.constants.ProjectStatus;
import com.wanda.portal.dao.jpa.ArtifactsRepository;
import com.wanda.portal.dao.jpa.ConfluenceSpaceRepository;
import com.wanda.portal.dao.jpa.JenkinsProjectRepository;
import com.wanda.portal.dao.jpa.JiraProjectRepository;
import com.wanda.portal.dao.jpa.ProjectMemberRepository;
import com.wanda.portal.dao.jpa.ProjectRepository;
import com.wanda.portal.dao.jpa.SCMRepoRepository;
import com.wanda.portal.dao.jpa.ServerRepository;
import com.wanda.portal.dao.remote.ConfluenceService;
import com.wanda.portal.dao.remote.JenkinsService;
import com.wanda.portal.dao.remote.JiraService;
import com.wanda.portal.dao.remote.ProjectService;
import com.wanda.portal.dao.remote.RepoService;
import com.wanda.portal.dto.confluence.CreateConfluenceSpaceParamDTO;
import com.wanda.portal.dto.svn.SvnTemplateDTO;
import com.wanda.portal.dto.svn.SvnTemplateWrapperDTO;
import com.wanda.portal.entity.Artifact;
import com.wanda.portal.entity.ConfluenceSpace;
import com.wanda.portal.entity.JenkinsProject;
import com.wanda.portal.entity.JiraProject;
import com.wanda.portal.entity.Project;
import com.wanda.portal.entity.ProjectMember;
import com.wanda.portal.entity.SCMRepo;
import com.wanda.portal.entity.Server;
import com.wanda.portal.facade.model.input.ArtifactInputParam;
import com.wanda.portal.facade.model.input.ConfluenceSpaceInputParam;
import com.wanda.portal.facade.model.input.JenkinsInputParam;
import com.wanda.portal.facade.model.input.JiraProjectInputParam;
import com.wanda.portal.facade.model.input.ProjectInputParam;
import com.wanda.portal.facade.model.input.ProjectMemberInputParam;
import com.wanda.portal.facade.model.input.ScmRepoInputParam;
import com.wanda.portal.utils.ValidationUtils;

@Primary
@Service("ProjectServiceImpl")
public class ProjectServiceImpl implements ProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    JiraProjectRepository jiraProjectRepository;
    @Autowired
    SCMRepoRepository scmRepoRepository;
    @Autowired
    ConfluenceSpaceRepository confluenceSpaceRepository;
    @Autowired
    JenkinsProjectRepository jenkinsProjectRepository;
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    ProjectMemberRepository projectMemberRepository;
    @Autowired
    ArtifactsRepository artifactsRepository;
    
    @Autowired
    JiraService jiraService;
    @Autowired
    ConfluenceService confluenceService;
    @Autowired
    JenkinsService jenkinsService;
    @Autowired
    RepoService repoService;

    @Autowired
    JiraConfig jiraConfig;
    @Autowired
    ConfluenceConfig confluenceConfig;
    @Autowired
    JenkinsConfig jenkinsConfig;
    @Autowired
    SvnConfig svnConfig;
    
    @Override
    public Project getProjectByKey(String projectKey) {
        return null;
    }

    @Override
    public Project getProjectById(Long projectId) {
        List<Long> queryId = new ArrayList<Long>();
        queryId.add(projectId);

        Project project = projectRepository.findById(projectId).get();
        return project;
    }

    @Deprecated
    @Override
    public void createProject(Project project) throws Exception {
        projectRepository.save(project);
    }
    
    @Deprecated
    @Override
    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void addExistingProjectPortal(Project project) {

    }

    @Override
    public List<Project> findAllByRankDesc() {
        List<String> sorts = new ArrayList<>();
        sorts.add("rank"); // 先rank
        sorts.add("projectKey"); // 再key
        Sort sort = new Sort(Sort.Direction.DESC, sorts) ;
        return (List<Project>) projectRepository.findAll(sort);
    }


    @Override
    public Project createProject(ProjectInputParam projectInputParam) throws Exception {
        projectInputParam.validateCreate();
        Project proj = persistProjectOnly(projectInputParam); // 因为是创建Project，需要先持久化
        executeDetailedTasks(projectInputParam, proj);
        return proj;
    }

    @Override
    public Project updateProject(ProjectInputParam projectInputParam) throws Exception {
        projectInputParam.validateModify();
        Project proj = projectRepository.findById(projectInputParam.getProjectId()).get(); // 因为是edit，需要查找之后再执行操作
        executeDetailedTasks(projectInputParam, proj);
        prepareUpdateProject(projectInputParam, proj);
        projectRepository.saveAndFlush(proj);
        return proj;
    }
    /*
     * project本身的修改
     * 注意此时不允许修改ProjectKey和ProjectName
     * */
    private static void prepareUpdateProject(ProjectInputParam projectInputParam, Project proj) {
        proj.setDescription(projectInputParam.getDescription());
        proj.setStatus(ProjectStatus.find(projectInputParam.getStatus()));
        proj.setRank(projectInputParam.getRank());
        proj.setRemark(projectInputParam.getRemark());
    }

    @Override
    public String  endProject(Long projectId) {
    	Project old=projectRepository.findById(projectId).get();
    	old.setStatus(ProjectStatus.END);
    	projectRepository.save(old);
        return "success";
    }
    
    private void executeDetailedTasks(ProjectInputParam projectInputParam, Project proj)
            throws Exception {
        if (proj == null) {
            throw new Exception("null project selected or persisted");
        }
        packScm(projectInputParam.getScmRepositories(), proj);       
        packJira(projectInputParam.getJiraProjects(), proj);
        packConfluence(projectInputParam.getConfluenceSpaces(), proj);
        packJenkins(projectInputParam.getJenkinsProjects(), proj);
        packProjectMembers(projectInputParam.getProjectMembers(), proj);
        packArtifact(projectInputParam.getArtifacts(), proj);
    }

    // 先纯落地Project
    private Project persistProjectOnly(ProjectInputParam projectInputParam) {
        Project proj = new Project();
        proj.setCreateTime(new Date());
        proj.setDescription(projectInputParam.getDescription());
        proj.setProjectKey(projectInputParam.getProjectKey());
        proj.setProjectName(projectInputParam.getProjectName());
        proj.setRank(projectInputParam.getRank());
        proj.setRemark(projectInputParam.getRemark());
        proj.setStatus(ProjectStatus.find(projectInputParam.getStatus()));
        proj = projectRepository.saveAndFlush(proj);
        return proj;
    }

    private void packArtifact(List<ArtifactInputParam> list, Project proj) {
        if (list == null || list.size() < 1) {
            return;
        }
        for (ArtifactInputParam artifact : list) {
            if (artifact.getArtifactId() == null) { // 表明为新建
                LOGGER.info("开始新建artifact，serverIp= " + artifact.getServerIP() + ", rootPath" + artifact.getRootPath());
                Artifact art = prepareArtifact(proj, artifact);               
                persistArtifactOnly(artifact, art);
            } else {
                LOGGER.info("开始更新artifact，ID=" + artifact.getArtifactId() + ", serverIp= " + artifact.getServerIP()
                        + ", rootPath" + artifact.getRootPath());
                Artifact art = prepareArtifact(proj, artifact);
                persistArtifactOnly(artifact, art);
            }
        }
    }

    private void persistArtifactOnly(ArtifactInputParam artifact, Artifact art) {
        Server server = serverRepository.findById(artifact.getServerId()).get();
        art.setServer(server);    
        artifactsRepository.saveAndFlush(art);
    }

    private static Artifact prepareArtifact(Project proj, ArtifactInputParam artifact) {
        Artifact art = new Artifact();
        art.setProject(proj);
        art.setAritifactType(artifact.getArtifactType());
        art.setArtifactId(artifact.getArtifactId());
        art.setCreateTime(ValidationUtils.validateDate(artifact.getCreateTime()));
        art.setRootPath(artifact.getRootPath());
        art.setServerIP(artifact.getServerIP());
        return art;
    }
    
    private void packProjectMembers(List<ProjectMemberInputParam> list, Project proj) {
        if (list == null || list.size() < 1) {
            return;
        }  
        for (ProjectMemberInputParam member : list) {
            if (member.getProjectMemberId() == null) { // 表明为新建
                LOGGER.info("开始创建member, 用户名为: " + member.getUsername() + ", 角色为: " + member.getRole());
                ProjectMember pm = prepareProjectMember(proj, member);
                projectMemberRepository.saveAndFlush(pm);
            } else { // 表明为修改
                LOGGER.info("开始修改memberId=" + member.getProjectMemberId() + ", 用户名为: " + member.getUsername()
                        + ", 角色为: " + member.getRole());
                ProjectMember pm = prepareProjectMember(proj, member);
                projectMemberRepository.saveAndFlush(pm);
            }
        }
    }

    private static ProjectMember prepareProjectMember(Project proj, ProjectMemberInputParam member) {
        ProjectMember pm = new ProjectMember();
        pm.setProjectMemberId(member.getProjectMemberId());
        pm.setCreateTime(ValidationUtils.validateDate(member.getCreateTime()));
        pm.setProject(proj);
        pm.setRole(member.getRole());
        pm.setUsername(member.getUsername());
        return pm;
    }
    
    private void packScm(List<ScmRepoInputParam> list, Project project) throws Exception {
        if (list == null || list.size() < 1) {
            return;
        }                
        for (ScmRepoInputParam repo : list) {
            Server server = serverRepository.findById(repo.getServerId()).get(); // 查询出对应的Server
            repoService.setServer(server); // 设置server，非常重要！
            SvnTemplateWrapperDTO tmps = repoService.findSubversionTemplates();
            Map<Long, String> tmpMap = new HashMap<>();
            for (SvnTemplateDTO tmp : tmps.getTemplates()) {
                tmpMap.put(tmp.getId(), tmp.getName());
            }
            RESULT checkRes = repo.getInputActionType().checkWithMainId(repo.getRepoId());           
            if (checkRes.equals(RESULT.VALID_REMOTE_CREATE)) { // 远程创建
                LOGGER.info("创建");  
                repoService.createSvnRepo(repo.getRepoName(), repo.getTemplateId(), true);
                SCMRepo scmRepo = prepareScmRepo(repo);
                Long tmpId = repo.getTemplateId();
                String repoStyle = tmpId == null ? "Empty repository"
                        : (tmpMap.get(tmpId) == null ? "Empty repository" : tmpMap.get(tmpId));
                scmRepo.setRepoStyle(repoStyle);         
                persistScmOnly(project, scmRepo, server);               
            } else if (checkRes.equals(RESULT.VALID_ATTACH_OLD)) { // 添加已有
                LOGGER.info("添加已有");
                SCMRepo scmRepo = prepareScmRepo(repo);
                persistScmOnly(project, scmRepo, server);         
            } else if (checkRes.equals(RESULT.VALID_UPDATE_OR_NOTHING)) { // 更新已有              
                LOGGER.info("更新已有");
                SCMRepo scmRepo = prepareScmRepo(repo);
                persistScmOnly(project, scmRepo, server);              
            } else {
                LOGGER.warn("illegal input action type and id!");
            }          
        }
    }

    private void persistScmOnly(Project project, SCMRepo scmRepo, Server server) {
        scmRepo.setServer(server);
        scmRepo.setProject(project);
        scmRepoRepository.saveAndFlush(scmRepo);
    }

    private static SCMRepo prepareScmRepo(ScmRepoInputParam repo) {
        SCMRepo scmRepo = new SCMRepo();
        scmRepo.setRepoId(repo.getRepoId());
//        scmRepo.setBrowsingServerIP(repo.getBrowsingServerIP());
//        scmRepo.setBrowsingServerRoot(repo.getBrowsingServerRoot());
        scmRepo.setCreateTime(repo.getCreateTime());
//        scmRepo.setProtocol(repo.getProtocol());
        scmRepo.setRemark(repo.getRemark());
        scmRepo.setRepoName(repo.getRepoName());
        scmRepo.setRepoStyle(repo.getRepoStyle());
        scmRepo.setServerIP(repo.getServerIP());
        scmRepo.setRepoType(repo.getRepoType());
//        scmRepo.setServerRoot(repo.getServerRoot());
        return scmRepo;
    }

    private void packJenkins(List<JenkinsInputParam> list, Project proj) throws Exception {
        if (list == null || list.size() < 1) {
            return;
        }
        for (JenkinsInputParam jenkins : list) {       
            Server server = serverRepository.findById(jenkins.getServerId()).get(); // 查询出对应的Server
            jenkinsService.setServer(server); // 设置server，非常重要！
            
            RESULT checkRes = jenkins.getInputActionType().checkWithMainId(jenkins.getJenkinsProjectId());            
            if (checkRes.equals(RESULT.VALID_REMOTE_CREATE)) { // 远程创建
                LOGGER.info("创建");
                jenkinsService.createJenkinsUsingCopy(jenkins.getJenkinsProjKey(), jenkins.getReferProj());
                JenkinsProject jenk = prepareJenkins(jenkins);
                persistJenkins(proj, jenk, server);
            } else if (checkRes.equals(RESULT.VALID_ATTACH_OLD)) { // 添加已有
                LOGGER.info("添加已有");
                JenkinsProject jenk = prepareJenkins(jenkins);
                persistJenkins(proj, jenk, server);
            } else if (checkRes.equals(RESULT.VALID_UPDATE_OR_NOTHING)) { // 更新已有           
                JenkinsProject jenk = prepareJenkins(jenkins);
                persistJenkins(proj, jenk, server);
            } else {
                LOGGER.warn("illegal input action type and id!");
            }            
            
        }
    }

    private void persistJenkins(Project proj, JenkinsProject jenk, Server server) {
        jenk.setServer(server);
        jenk.setProject(proj);
        jenkinsProjectRepository.saveAndFlush(jenk);
    }

    private static JenkinsProject prepareJenkins(JenkinsInputParam jenkins) {
        JenkinsProject jenk = new JenkinsProject();
        jenk.setJenkinsProjectId(jenkins.getJenkinsProjectId());
        jenk.setCreateTime(jenkins.getCreateTime());
        jenk.setJenkinsProjKey(jenkins.getJenkinsProjKey());
        jenk.setRemark(jenkins.getRemark());
        jenk.setServerIP(jenkins.getServerIP());
        return jenk;
    }

    private void packConfluence(List<ConfluenceSpaceInputParam> list, Project proj)
            throws Exception {
        if (list == null || list.size() < 1) {
            return;
        }
        for (ConfluenceSpaceInputParam conf : list) {   
            Server server = serverRepository.findById(conf.getServerId()).get(); // 查询出对应的Server
            confluenceService.setServer(server); // 设置server，非常重要！            
            RESULT checkRes = conf.getInputActionType().checkWithMainId(conf.getSpaceId());
            
            if (checkRes.equals(RESULT.VALID_REMOTE_CREATE)) { // 远程创建
                LOGGER.info("创建");
                CreateConfluenceSpaceParamDTO param = new CreateConfluenceSpaceParamDTO();
                param.setKey(conf.getSpaceKey());
                param.setName(conf.getSpaceName());
                param.setRepresentation(conf.getSpaceDescription());
                param.setDescription(conf.getSpaceDescription());
                String wb = confluenceService.createSpace(param);
                ConfluenceSpace confluenceSpace = prepareConfluence(conf);
                confluenceSpace.setWebui(wb);
                persistConfluenceOnly(proj, confluenceSpace, server);                
            } else if (checkRes.equals(RESULT.VALID_ATTACH_OLD)) { // 添加已有
                LOGGER.info("添加已有");
                ConfluenceSpace confluenceSpace = prepareConfluence(conf);
                persistConfluenceOnly(proj, confluenceSpace, server);           
            } else if (checkRes.equals(RESULT.VALID_UPDATE_OR_NOTHING)) { // 更新已有         
                LOGGER.info("更新已有");
                ConfluenceSpace confluenceSpace = prepareConfluence(conf);
                persistConfluenceOnly(proj, confluenceSpace, server);               
            } else {
                LOGGER.warn("illegal input action type and id!");
            }                    
        }
    }

    private void persistConfluenceOnly(Project proj, ConfluenceSpace confluenceSpace, Server server) {
        confluenceSpace.setServer(server);
        confluenceSpace.setProject(proj);
        confluenceSpaceRepository.saveAndFlush(confluenceSpace);
    }

    private static ConfluenceSpace prepareConfluence(ConfluenceSpaceInputParam conf) {
        ConfluenceSpace confluenceSpace = new ConfluenceSpace();
        confluenceSpace.setPageId(conf.getPageId());
        confluenceSpace.setSpaceId(conf.getSpaceId());
        confluenceSpace.setCreateTime(conf.getCreateTime());
        confluenceSpace.setServerIP(conf.getServerIP());
        confluenceSpace.setSpaceDescription(conf.getSpaceDescription());
        confluenceSpace.setSpaceKey(conf.getSpaceKey());
        confluenceSpace.setSpaceName(conf.getSpaceName());
        //confluenceSpace.setWebui("");
        return confluenceSpace;
    }

    private void packJira(List<JiraProjectInputParam> list, Project proj) throws Exception {
        if (list == null || list.size() < 1) {
            return;
        }
        for (JiraProjectInputParam jira : list) {
            Server server = serverRepository.findById(jira.getServerId()).get(); // 查询出对应的Server
            jiraService.setServer(server); // 设置server，非常重要！
            RESULT checkRes = jira.getInputActionType().checkWithMainId(jira.getJiraProjectId());
            
            if (checkRes.equals(RESULT.VALID_REMOTE_CREATE)) { // 远程创建
                LOGGER.info("创建jira");
                jiraService.createJiraProjectUsingExistingProject(jira.getReferJiraId(), jira.getJiraProjectKey(),
                       jira.getTeamleader(), jira.getJiraProjectName(), true);
                JiraProject jiraProject = prepareJira(jira);
                persistJiraOnly(proj, jiraProject, server);
            } else if (checkRes.equals(RESULT.VALID_ATTACH_OLD)) { // 添加已有
                LOGGER.info("添加已有");
                JiraProject jiraProject = prepareJira(jira);
                persistJiraOnly(proj, jiraProject, server);            
            } else if (checkRes.equals(RESULT.VALID_UPDATE_OR_NOTHING)) { // 更新已有              
                LOGGER.info("更新已有");
                JiraProject jiraProject = prepareJira(jira);
                persistJiraOnly(proj, jiraProject, server);               
            } else {
                LOGGER.warn("illegal input action type and id!");
            }                
        }
    }

    private void persistJiraOnly(Project proj, JiraProject jiraProject, Server server) {
        jiraProject.setServer(server);
        jiraProject.setProject(proj);
        jiraProjectRepository.saveAndFlush(jiraProject);
    }

    private static JiraProject prepareJira(JiraProjectInputParam jira) {
        JiraProject jiraProject = new JiraProject();
        jiraProject.setTeamleader(jira.getTeamleader()); // 补上teamleader
        jiraProject.setJiraProjectId(jira.getJiraProjectId());
        jiraProject.setCreateTime(jira.getCreateTime());
        jiraProject.setJiraProjectKey(jira.getJiraProjectKey());
        jiraProject.setJiraProjectName(jira.getJiraProjectName());
        jiraProject.setJiraProjectDescription(jira.getJiraProjectDescription());
        jiraProject.setRemark(jira.getRemark());       
        return jiraProject;
    }

}
