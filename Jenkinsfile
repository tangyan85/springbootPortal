// jenkins pipeline build脚本，采用scripted pipeline
node {
    def mvnHome
    stage('Preparation') {
        mvnHome = tool 'MAVEN-3.5'
        checkout([
            $class: 'SubversionSCM',
            additionalCredentials: [],
            excludedCommitMessages: '',
            excludedRegions: '',
            excludedRevprop: '',
            excludedUsers: '',
            filterChangelog: false,
            ignoreDirPropChanges: false,
            includedRegions: '',
            locations: [
                [cancelProcessOnExternalsFail: true,
                    credentialsId: 'newSvnSync',
                    depthOption: 'infinity',
                    ignoreExternalsOption: true,
                    local: '.',
                    remote: 'http://10.215.4.165/svn/wdjr_adlm/springbootPortal/trunk@HEAD']
            ],
            quietOperation: true,
            workspaceUpdater: [$class: 'UpdateUpdater']
        ])
    }
    stage('Build') { sh "'${mvnHome}/bin/mvn' -B -U clean package findbugs:findbugs" }
    stage('Results') {
        //junit '**/target/surefire-reports/TEST-*.xml'
        parallel( // 单元测试和findbugs静态分析可并行
                unittest : {
                    step([$class: 'JUnitResultArchiver', testResults: '**/TEST-*.xml'])
                },
                jacoco : {
                    jacoco()
                },
                findbugs : {
                    // findbugs分析和报告生成也是串行
                    findbugs canComputeNew: false,
                    defaultEncoding: '',
                    excludePattern: '',
                    healthy: '',
                    includePattern: '',
                    pattern: '**/findbugsXml.xml',
                    unHealthy: ''
                    step([
                        $class: 'AnalysisPublisher',
                        canComputeNew: false,
                        defaultEncoding: '',
                        healthy: '',
                        pmdActivated: false,
                        unHealthy: ''
                    ])
                }
                )
        emailext body: '$DEFAULT_CONTENT', recipientProviders: [
            [$class: 'DevelopersRecipientProvider'],
            [$class: 'RequesterRecipientProvider']
        ],
        subject: '$DEFAULT_SUBJECT',
        to: '$DEFAULT_RECIPIENTS'
    }
}