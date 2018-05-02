package com.wanda.portal.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import com.wanda.portal.RootApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class TestCase1 {
	@Autowired
	private TestRestTemplate testRestTemplate;

	/*
	 * 测试createProject接口
	 */
//	@Test
//	public void tryPostForCreateProject() throws Exception {
//		Map<String, Object> multiValueMap = new HashMap<>();
//		// multiValueMap.put("project_id", 1);// id一般要禁止，如果db存在此id，则会暴力更新
//		multiValueMap.put("projectKey", "MKKs");
//		multiValueMap.put("projectName", "杀111地区的去偶尔");
//		multiValueMap.put("description", "达到");
//		multiValueMap.put("status", "LEGAL");
//		multiValueMap.put("rank", 6);
//		multiValueMap.put("remark", "强无读取多");
//		multiValueMap.put("createTime", new Date());
//		Object[] x = {null};
//
//		String result = testRestTemplate.postForObject("/createProject",
//				multiValueMap, String.class, x);
//		Assert.assertEquals(result, "success");
//	}

	// /*
	// * 测试createJenkins接口
	// */
	// @Test
	// public void tryPostForCreateJenkinsProject() throws Exception {
	// Map<String, Object> multiValueMap = new HashMap<>();
	// multiValueMap.put("jenkinsProjId", 1);// id一般要禁止，如果db存在此id，则会暴力更新
	// multiValueMap.put("jenkinsProjKey", "jenkins-job-no1");
	// multiValueMap.put("serverIp", "10.210.74.70");
	// multiValueMap.put("createTime", new Date());
	// multiValueMap.put("projectId", 1);
	// multiValueMap.put("remark", "my jenkins");
	// Object[] x = {null};
	//
	// String result = testRestTemplate.postForObject("/createJenkins",
	// multiValueMap, String.class, x);
	// Assert.assertEquals(result, "success");
	// }
	//
	// /*
	// * 测试createJira接口
	// */
	// @Test
	// public void tryPostForCreateJiraProject() throws Exception {
	// Map<String, Object> multiValueMap = new HashMap<>();
	// multiValueMap.put("jiraProjectId", 1);// id一般要禁止，如果db存在此id，则会暴力更新
	// multiValueMap.put("jiraProjectKey", "BBDD");
	// multiValueMap.put("jiraProjectName", "JIRA项目demo1");
	// multiValueMap.put("jiraProjectDescription", "这个项目非常重要");
	// multiValueMap.put("serverIP", "22.33.212.22");
	// multiValueMap.put("createTime", new Date());
	// multiValueMap.put("projectId", 1);
	// multiValueMap.put("remark", "my jira");
	// Object[] x = {null};
	//
	// String result = testRestTemplate.postForObject("/createJira",
	// multiValueMap, String.class, x);
	// Assert.assertEquals(result, "success");
	// }
	//
	// /*
	// * 测试createScmRepo接口
	// */
	// @Test
	// public void tryPostForCreateScmRepo() throws Exception {
	// Map<String, Object> multiValueMap = new HashMap<>();
	// multiValueMap.put("repoId", 2);// id一般要禁止，如果db存在此id，则会暴力更新
	// multiValueMap.put("repoName", "wxxs");
	// multiValueMap.put("serverIP", "44.53.212.22");
	// multiValueMap.put("serverRoot", "root");
	// multiValueMap.put("browsingServerIP", "144.153.212.22");
	// multiValueMap.put("browsingServerRoot", "browseroot");
	// multiValueMap.put("remark", "my svn");
	// multiValueMap.put("createTime", new Date());
	// multiValueMap.put("protocol", RepoProtocol.SVN);
	// multiValueMap.put("repoStyle", RepoStyle.BRANCHED);
	// multiValueMap.put("repoType", RepoType.SVN);
	// multiValueMap.put("projectId", 1);
	// Object[] x = {null};
	//
	// String result = testRestTemplate.postForObject("/createScmRepo",
	// multiValueMap, String.class, x);
	// Assert.assertEquals(result, "success");
	// }
	//
	// /*
	// * 测试createConfluenceSpace接口
	// */
	// @Test
	// public void tryPostForCreateConfluenceSpace() throws Exception {
	// Map<String, Object> multiValueMap = new HashMap<>();
	// multiValueMap.put("spaceId", 2);// id一般要禁止，如果db存在此id，则会暴力更新
	// multiValueMap.put("spaceKey", "confXXX");
	// multiValueMap.put("serverIP", "44.53.212.22");
	// multiValueMap.put("spaceName", "secret_room");
	// multiValueMap.put("spaceDescription", "秘密空间");
	// multiValueMap.put("pageId", "2899382");
	//
	// multiValueMap.put("projectId", 1);
	// Object[] x = {null};
	//
	// String result = testRestTemplate.postForObject("/createConfluenceSpace",
	// multiValueMap, String.class, x);
	// Assert.assertEquals(result, "success");
	// }
	//
	// /*
	// * 测试createArtifact接口
	// */
	// @Test
	// public void tryPostForCreateArtifact() throws Exception {
	// Map<String, Object> multiValueMap = new HashMap<>();
	// multiValueMap.put("artifactId", 1);// id一般要禁止，如果db存在此id，则会暴力更新
	// multiValueMap.put("rootPath", "ssadkl");
	// multiValueMap.put("serverIP", "144.153.212.122");
	// Project p = new Project();
	// p.setProjectId(1l);
	// multiValueMap.put("project", p);
	// multiValueMap.put("aritifactType", AritifactType.MAVEN);
	// Object[] x = {null};
	//
	// String result = testRestTemplate.postForObject("/createArtifact",
	// multiValueMap, String.class, x);
	// Assert.assertEquals(result, "success");
	// }
//
//	@Autowired
//	ProjectRepository projectRepository;
//	@Autowired
//	JiraProjectRepository jiraProjectRepository;
//
//	@Test
//	public void saveProjectComplex() {
//		Project p = new Project();
//		p.setCreateTime(new Date());
//		p.setProjectKey("haha");
//		p.setDescription("qdqd1");
//		List<Artifact> l = new ArrayList<>();
//		Artifact aa = new Artifact();
//		aa.setAritifactType(AritifactType.MAVEN);
//		aa.setRootPath("vv");
//		l.add(aa);
//		// p.setArtifacts(l);
//		projectRepository.save(p);
//	}
//
//	@Test
//	public void modifyJiraOnAProject() {
//		Optional<JiraProject> p = jiraProjectRepository.findById(1l);
//		p.get().setRemark("CHANGE IT!");
//		jiraProjectRepository.save(p.get());
//		Optional<Project> pro = projectRepository.findById(1l);
//		pro.get().setConfluenceSpaces(new ArrayList<>());
//		pro.get().setArtifacts(new ArrayList<>());
//		pro.get().setJenkinsProjects(new ArrayList<>());
//		pro.get().setScmRepositories(new ArrayList<>());
//		pro.get().setProjectMembers(new ArrayList<>());
//		try {
//			System.out.println(JSONObject.toJSONString(pro.get()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void appendOneJiraOnAProject() {
//		Optional<Project> p = projectRepository.findById(1l);
//		Project pp = p.get();
//		JiraProject jira = new JiraProject();
//		jira.setCreateTime(new Date());
//		jira.setJiraProjectDescription("在拉大的确定");
//		jira.setJiraProjectKey("WDJR-123123");
//		jira.setJiraProjectName("wqewqewqe");
//		jira.setRemark("气象万千");
//		jira.setServerIP("215.234.53.2");
//		jira.setProject(pp);
//		List<JiraProject> jiras = new ArrayList<>();
//		jiras.addAll(pp.getJiraProjects());
//		jiras.add(jira);
//		pp.setJiraProjects(jiras);
//		projectRepository.save(pp);
//	}
//
//	/*
//	 * 测试createArtifact接口
//	 */
//	@Test
//	public void tryPostForQueryAllProject() throws Exception {
//		Map<String, Object> multiValueMap = new HashMap<>();
//		Object[] x = {null};
//
//		String result = testRestTemplate.postForObject("/queryAllProject",
//				multiValueMap, String.class, x);
//		// Assert.assertEquals(result, "success");
//		System.out.println(result);
//	}
//
//	@Autowired
//	RestTemplate restTemplate;
//	@Autowired
//	JiraConfig jiraConfig;
//	@Test
//	public void testRest() {
//		HttpEntity<String> request = new HttpEntity<String>(
//				RestUtils.packBasicAuthHeader(jiraConfig.getUsername(),
//						jiraConfig.getPassword()));
//		ResponseEntity<String> response = restTemplate.exchange(
//				jiraConfig.getUrlHead() + "search?jql=key=ADLM-19",
//				HttpMethod.GET, request, String.class);
//		String account = response.getBody();
//		System.out.println(account);
//
//	}

	@Test 
	public void fake() {
	    
	}
}
