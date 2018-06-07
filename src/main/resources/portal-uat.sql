-- 导出 portal 的数据库结构
DROP DATABASE IF EXISTS `portal`;
CREATE DATABASE IF NOT EXISTS `portal` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `portal`;

-- 导出  表 portal.confluence_space 结构
DROP TABLE IF EXISTS `confluence_space`;
CREATE TABLE IF NOT EXISTS `confluence_space` (
  `space_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `page_id` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `server_ip` varchar(255) DEFAULT NULL,
  `space_description` varchar(255) DEFAULT NULL,
  `space_key` varchar(255) DEFAULT NULL,
  `space_name` varchar(255) DEFAULT NULL,
  `web_ui` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `server_id` bigint(20) DEFAULT NULL,
  `input_action_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`space_id`),
  UNIQUE KEY `UK_gac78vthjfhsre0bgvvyhuntg` (`space_key`),
  KEY `FK9m3ccub605rd13433e3qt5ja` (`project_id`),
  KEY `FK2eluhp2y9805fwn6ftrj3xpmr` (`server_id`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- 导出  表 portal.jenkins_project 结构
DROP TABLE IF EXISTS `jenkins_project`;
CREATE TABLE IF NOT EXISTS `jenkins_project` (
  `jenkins_proj_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `jenkins_proj_key` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `server_ip` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `server_id` bigint(20) DEFAULT NULL,
  `input_action_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`jenkins_proj_id`),
  UNIQUE KEY `UK_o7l9ukx8yf8aisf0taaw2gkka` (`jenkins_proj_key`),
  KEY `FKpckcxqdcwyt7s234t1wo4hmv7` (`project_id`),
  KEY `FK1em3mlqxeqiu1styffrrg0ald` (`server_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- 导出  表 portal.jira_project 结构
DROP TABLE IF EXISTS `jira_project`;
CREATE TABLE IF NOT EXISTS `jira_project` (
  `jira_proj_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `jira_proj_description` varchar(255) DEFAULT NULL,
  `jira_proj_key` varchar(255) DEFAULT NULL,
  `jira_proj_name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `server_id` bigint(20) DEFAULT NULL,
  `server_ip` varchar(255) DEFAULT NULL,
  `input_action_type` int(11) DEFAULT NULL,
  `refer_jira_id` bigint(20) DEFAULT NULL,
  `team_leader` varchar(255) DEFAULT NULL,
  `web_ui` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`jira_proj_id`),
  UNIQUE KEY `UK_15s110e3065tq0klur7uoyb2n` (`jira_proj_key`),
  KEY `FK3slmejtg6y3097i41xgpuwuw9` (`project_id`),
  KEY `FK65thwbgvn6g2ty6wwmr3by0w5` (`server_id`)
) ENGINE=MyISAM AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

-- 导出  表 portal.nexus_artifacts 结构
DROP TABLE IF EXISTS `nexus_artifacts`;
CREATE TABLE IF NOT EXISTS `nexus_artifacts` (
  `artifact_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `artifact_type` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `root_path` varchar(255) DEFAULT NULL,
  `server_ip` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `server_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`artifact_id`),
  KEY `FKyj4pg3v03c9n99y35vlnjv03` (`project_id`),
  KEY `FKesd24xgxj2iso27klq9iragbk` (`server_id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- 导出  表 portal.project 结构
DROP TABLE IF EXISTS `project`;
CREATE TABLE IF NOT EXISTS `project` (
  `project_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `project_key` varchar(255) DEFAULT NULL,
  `project_name` varchar(255) DEFAULT NULL,
  `rank` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `UK_6nmhlci6jh2k2fv7ipcfv1drm` (`project_key`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- 导出  表 portal.project_member 结构
DROP TABLE IF EXISTS `project_member`;
CREATE TABLE IF NOT EXISTS `project_member` (
  `project_member_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`project_member_id`),
  KEY `FK103dwxad12nbaxtmnwus4eft2` (`project_id`)
) ENGINE=MyISAM AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

-- 导出  表 portal.project_scm_repositories 结构
DROP TABLE IF EXISTS `project_scm_repositories`;
CREATE TABLE IF NOT EXISTS `project_scm_repositories` (
  `project_project_id` bigint(20) NOT NULL,
  `scm_repositories_repo_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_37ujosjv9glgaug7micc7bxyc` (`scm_repositories_repo_id`),
  KEY `FKqdhs138vj5j5gsdw21nxjplba` (`project_project_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 导出  表 portal.scm_repository 结构
DROP TABLE IF EXISTS `scm_repository`;
CREATE TABLE IF NOT EXISTS `scm_repository` (
  `repo_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `browsing_server_ip` varchar(255) DEFAULT NULL,
  `browsing_server_root` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `repo_protocol` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `repo_name` varchar(255) DEFAULT NULL,
  `repo_style` varchar(255) DEFAULT NULL,
  `repo_type` varchar(255) DEFAULT NULL,
  `server_ip` varchar(255) DEFAULT NULL,
  `server_root` varchar(255) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `server_id` bigint(20) DEFAULT NULL,
  `input_action_type` int(11) DEFAULT NULL,
  `repo_remote_id` bigint(20) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  `web_ui` varchar(100) DEFAULT NULL,
  `check_out` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`repo_id`),
  KEY `FKmixtabyicf1122us8wlqo4hlr` (`project_id`),
  KEY `FKsyn5yjxljjat3g3twhaj8076d` (`server_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- 导出  表 portal.server 结构
DROP TABLE IF EXISTS `server`;
CREATE TABLE IF NOT EXISTS `server` (
  `server_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `inner_server_ip` varchar(40) DEFAULT NULL,
  `outer_server_ip` varchar(40) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `server_type` varchar(255) DEFAULT NULL,
  `protocol` varchar(20) DEFAULT NULL,
  `reserved` varchar(255) DEFAULT NULL,
  `server_root` varchar(40) DEFAULT NULL,
  `login_suffix` varchar(100) DEFAULT NULL,
  `domain` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`server_id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- 正在导出表  portal2.server 的数据：10 rows
/*!40000 ALTER TABLE `server` DISABLE KEYS */;
INSERT INTO `server` (`server_id`, `inner_server_ip`, `outer_server_ip`, `remark`, `server_type`, `protocol`, `reserved`, `server_root`, `login_suffix`, `domain`) VALUES
	(1, '192.168.0.23', '10.215.4.112', 'jira的url', 'JIRA', 'http', NULL, NULL, '', 'jira-uat.wdjr.local'),
	(7, '', '', 'sonarqube的url', 'SONAR', 'http', NULL, NULL, '/sessions/new', 'sonar-uat.wdjr.local'),
	(3, '192.168.0.33', '10.215.4.123', 'jenkins master的url', 'JENKINS', 'http', NULL, NULL, '', '	ci-uat.wdjr.local'),
	(4, '192.168.0.30', '10.215.4.118', 'confluence的url', 'CONFLUENCE', 'http', NULL, NULL, '/dashboard.action#all-updates', 'conf-uat.wdjr.local	\r\n'),
	(5, '192.168.0.31', '', 'svn的url,对外展现端口是80', 'SVN', 'https', NULL, '', '/csvn', 'svn-uat.wdjr.local:4434'),
	(6, '192.168.0.34', '10.215.4.122', 'nexus的url', 'ARTIFACT', 'http', NULL, NULL, NULL, 'nexus-uat.wdjr.local'),
	(8, '192.168.0.35', '10.215.4.63', 'git的url', 'GIT', 'https', 'not applied', NULL, NULL, 'git-uat.wdjr.local'),
	(9, '192.168.0.29', '10.215.4.111', 'portal的url', 'PORTAL', 'http', NULL, '', '/index', 'adlml-uat.wdjr.local'),
	(10, '', '', 'testlink的url', 'TESTLINK', 'http', NULL, 'testlink', '/login.php', NULL),
	(11, '192.168.0.31', '', 'viewvc的url', 'VIEWVC', 'https', NULL, '', '', 'svn-uat.wdjr.local');

-- 导出  表 portal.stat_task_record 结构
DROP TABLE IF EXISTS `stat_task_record`;
CREATE TABLE IF NOT EXISTS `stat_task_record` (
  `task_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `record` varchar(255) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `finTime` datetime DEFAULT NULL,
  `interval` time DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=457 DEFAULT CHARSET=utf8;

-- 导出  表 portal.user 结构
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `raw_password` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

