            $(function () {
                //使用的是url进行iframe src替换
                var defaultData = [{
                        text: '项目管理',
                        href: '#parent1',
                        iconClass: "fa fa-dashboard",
                        nodes: [
                            /*{
                                text: '新建项目',
                                url: 'project/toAdd',
                                iconClass: "fa fa-circle-thin"
                            },*/{
                                text: '新建项目',
                                url: 'project/toAdd2',
                                iconClass: "fa fa-circle-thin"
                            },
                            /*{
                                text: '维护项目',
                                url: 'project/findAllProjects/view',
                                iconClass: "fa fa-circle-thin"
                            },*/
                            {
                                text: '查看/维护项目',
                                url: 'project/toList',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查看项目进展',
                                url: jiraUrlPrefix + '/secure/BrowseProjects.jspa?selectedCategory=all&selectedProjectType=all',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '结项申请',
                                url: 'project/findAllProjects/end',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },
                    {
                        text: '项目成员管理',
                        href: '#parent2',
                        iconClass: "fa fa-user",
                        nodes: [
                            {
                                text: '新建项目成员',
                                url: 'member/toAdd/false',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '项目成员查询',
                                url: 'member/toList',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },
                    {
                        text: '任务跟踪',
                        href: '#parent2',
                        iconClass: "fa fa-sitemap",
                        nodes: [
                            {
                                text: '新建Jira任务',
                                url: jiraUrlPrefix + '/secure/CreateIssue.jspa',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查询任务',
                                url: jiraUrlPrefix + '/issues/?jql=',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '新建Jira项目',
                                url: 'task/toAdd/false',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查询/维护Jira项目',
                                url: '/task/toList',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },
                    {
                        text: '文档管理',
                        href: '#parent3',
                        iconClass: "fa fa-gears",
                        nodes: [
                            {
                                text: '新建Confluence页面',
                                url: confUrlContext + '/pages/resumedraft.action?draftId=12582916&draftShareId=9e1062ec-fbeb-49e7-a798-74d9fa4e336d',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查询文档',
                                url: confUrlContext + '/spacedirectory/view.action',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '新建Confluence空间',
                                url: 'doc/toAdd/false',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查看/维护Confluence空间',
                                url: 'doc/toList',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },
                    {
                        text: '持续集成',
                        href: '#parent4',
                        iconClass: "fa fa-copy",
                        nodes: [
                            {
                                text: '新建Jenkins Job',
                                url: 'ci/toAdd/false',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查看/维护Jenkins Job',
                                url: 'ci/toList',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },
                    {
                        text: '源代码管理',
                        iconClass: "fa fa-eye",
                        href: '#parent5',
                        nodes: [
                            /*{
                                text: '创建代码库',
                                url: svnLoginUrl + '/repo/index',
                                iconClass: "fa fa-circle-thin"
                            },*/
                            {
                                text: '代码库查询',
                                url: svnLoginUrl + '#browse/browse',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '新建代码库',
                                url: 'code/toAdd/false',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查看/维护代码库',
                                url: 'code/toList',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查看代码',
                                url: 'project/findAllProjects/svnView',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '申请codeReview',
                                url: 'pages/blank',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '修改访问规则',
                                url: svnLoginUrl + '/repo/editAuthorization',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },
                    {
                        text: '质量管理',
                        href: '#parent4',
                        iconClass: "fa fa-wrench",
                        nodes: [{
                            text: '静态分析结果',
                            url: sonarUrlContext + "/projects",
                            iconClass: "fa fa-circle-thin"
                        }, {
                            text: '测试管理',
                            url: testlinkUrlPrefix,
                            iconClass: "fa fa-circle-thin"
                        }]
                    },
                    {
                        text: '资源申请',
                        href: '#parent5',
                        iconClass: "fa fa-globe",
                        nodes: [{
                                text: '申请DEV环境',
                                url: 'pages/blank',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '申请UAT环境',
                                url: 'pages/blank',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '申请PROD环境',
                                url: 'pages/blank',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },
                    {
                        text: '发布申请',
                        iconClass: "fa fa-edit",
                        href: '#parent5',
                        nodes: [{
                                text: '发布日构建',
                                url: 'pages/blank',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: 'UAT构建发布',
                                url: 'pages/blank',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '申请生产变更',
                                url: 'pages/blank',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },

                    {
                        text: '软件仓库',
                        iconClass: "fa fa-database",
                        href: '#parent5',
                        nodes: [
                            {
                                text: '新建项目产出物',
                                url: 'artifact/toAdd/false',
                                iconClass: "fa fa-circle-thin"
                            },
                            {
                                text: '查看/维护项目产出物',
                                url: 'artifact/toList',
                                iconClass: "fa fa-circle-thin"
                            },
                            /*{
                                text: '查看产出物',
                                url: 'project/findAllProjects/artifactView',
                                iconClass: "fa fa-circle-thin"
                            },*/
                            {
                                text: '查询软件',
                                url: 'pages/blank',
                                iconClass: "fa fa-circle-thin"
                            }
                        ]
                    },
                    {
                        text: '变更管理',
                        href: '#parent5',
                        iconClass: "fa fa-exchange"
                    },
                    {
                        text: '运维管理',
                        href: '#parent5',
                        iconClass: "fa fa-calendar"
                    },
                    {
                        text: '监控管理',
                        href: '#parent5',
                        iconClass: "fa fa-bar-chart-o"
                    }
                ];

                /**
                 * 拼凑menu字符串
                 *         <li class="treeview">
          <a href="#">
            <i class="fa fa-files-o"></i>
            <span>Layout Options</span>
            <span class="pull-right-container">
              <span class="label label-primary pull-right">4</span>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="pages/layout/top-nav.html"><i class="fa fa-circle-o"></i> Top Navigation</a></li>
            <li><a href="pages/layout/boxed.html"><i class="fa fa-circle-o"></i> Boxed</a></li>
            <li><a href="pages/layout/fixed.html"><i class="fa fa-circle-o"></i> Fixed</a></li>
            <li><a href="pages/layout/collapsed-sidebar.html"><i class="fa fa-circle-o"></i> Collapsed Sidebar</a></li>
          </ul>
        </li>
                */
                var ul = $(".sidebar-menu");
                var li = "";
                for (var i = 0; i < defaultData.length; i++) {
                    var curr = defaultData[i]; //获取当前li
                    li += '<li class="treeview">';
                    li += '<a href="#">';

                    if (typeof (curr.iconClass) != "undefined" && curr.iconClass != null) {
                        li += '<i class="' + curr.iconClass + '"></i>';
                    }
                    if (typeof (curr.text) != "undefined" && curr.text != null) {
                        li += '<span>' + curr.text + '</span>';
                    }
                    if (typeof (curr.nodes) != "undefined" && curr.nodes != null) {
                        li += '<span class="pull-right-container"><span class="label label-primary pull-right">' +
                            curr.nodes.length + '</span></span></span>';
                    }
                    li += '</a>';

                    //nodes
                    var nodes = "";
                    if (typeof (curr.nodes) != "undefined" && curr.nodes != null) {
                        nodes += '<ul class="treeview-menu">';
                        for (var j = 0; j < curr.nodes.length; j++) {
                            var node = curr.nodes[j];

                            nodes += '<li>';
                            nodes += '<a href="#" ';
                            if (typeof (node.url) != "undefined" && node.url != null) {
                                nodes += ' data-url="' + node.url + '"';
                            }
                            nodes += '> <i ';
                            if (typeof (node.iconClass) != "undefined" && node.iconClass != null) {
                                nodes += ' class="' + node.iconClass + '"></i>';
                            }
                            if (typeof (node.text) != "undefined" && node.text != null) {
                                nodes += node.text;
                            }
                            nodes += '</a></li>';
                        }
                        nodes += '</ul>';
                    }
                    li += nodes;
                    li += '</li>';
                }
                ul.append(li);
            })