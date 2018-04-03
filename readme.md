
# Jenkins Startup Scripts

[![Build Status](https://travis-ci.org/buildit/jenkins-startup-scripts.svg?branch=master)](https://travis-ci.org/buildit/jenkins-startup-scripts)
[ ![Download](https://api.bintray.com/packages/buildit/maven/jenkins-startup-scripts/images/download.svg) ](https://bintray.com/buildit/maven/jenkins-startup-scripts/_latestVersion)

A collection of groovy scripts that can be used to simplify the configuration of Jenkins.

## Using the Scripts

Though it is possible to run them without it, these scripts have been wrtten to work in conjunction with the jenkins-startup-scripts-runner project. Please see that project for details on how to confgure and invoke these scripts.

## Adding Scripts

To add another script, simply place the file inside the scripts directory and add the details to the "config/scripts.config" file. The following snippet, for example, would configure a script called "newscript.groovy" in the scripts directory, that would only be evaluated if the configuration file contains a non null value for config.tools.newtool.

```
newscript {
    script = "scripts/newscript.groovy"
    configPath = "config?.tools?.newtool"
}
```

The scripts themselves are pretty easy to write, take a look at the existing scripts - you should find something that is similar to the problem you are trying to solve.

## Testing

There are plenty of examples of how to write tests in this codebase already. Most of the existing tests start up an instance of Jenkins and test the state after a certain script has been executed. Much of the hard work has been done already so try not to reinvent the wheel.

Note. As an added bonus, when you add a test config file to the src/test/resources/scripts directory it will be automatically added to these docs when they are auto-generated. See below for instructions.

## Configuration Samples

ActiveDirectory

```groovy
activeDirectory {
    domain = 'host.local'
    server = 'server.example.com:3268'
    site = 'ABC'
    bindName = 'dn'
    bindPassword = 'pwd'
}
```

<sub>Tested Plugin Versions: active-directory-2.0.hpi, mailer-1.20.hpi, display-url-api-2.2.0.hpi</sub>

Anchore

```groovy
anchore {
    debug = false
    enabled = true
    useSudo = false
    engineMode = 'anchoreengine'

    // Engine mode parameters
    engineUrl = 'https://example.com/anchore'
    engineUser = 'admin'
    enginePass = 'foobar'
    engineVerifySSL = false

    // Local mode parameters
    containerImageId = 'anchore/jenkins:latest'
    containerId = 'jenkins_anchore'
}
```

<sub>Tested Plugin Versions: anchore-container-scanner-1.0.12.hpi, structs-1.10.hpi</sub>

Commands

```groovy
commands {
    spiderman=[
            // remember to add '> dev null' to prevent logging sensitive information
            command: "echo 'Peter Parker' > ${jenkinsHome}/spiderman",
            description:'Write to the Spiderman file',
    ]
}
```

<sub>Tested Plugin Versions: NA</sub>

ConfigFiles

```groovy
configfiles {
	loansGlobal = [
		id: 'id1',
		name: 'name1',
		comment: 'comment1',
		content: '<settings>1</settings>',
		replaceAll: 'true',
		servers: [
		 	[serverId: 'server1', credentialsId: 'credentialsId1'],
		 	[serverId: 'server2', credentialsId: 'credentialsId2']
		]
	]
}
```

<sub>Tested Plugin Versions: config-file-provider-2.15.7.hpi, credentials-2.1.16.hpi, ssh-credentials-1.13.hpi, structs-1.10.hpi, token-macro-2.3.hpi, workflow-job-2.16.hpi, workflow-step-api-2.14.hpi, workflow-api-2.26.hpi, workflow-support-2.16.hpi, scm-api-2.2.6.hpi, script-security-1.40.hpi</sub>

Confluence

```groovy
tools {
  confluence = [
                  [
                    url: 'http://confluence.sandbox.extranet.group/',
                    username: 'arvind.kumar',
                    password: 'ENC(Y/a5t0YI5fmWqx1NEEYoKQ==)'
                  ]
              ]
}
```

<sub>Tested Plugin Versions: confluence-publisher-1.8.hpi</sub>

Credentials

```groovy
credentials {
    repository=['username':'repository', 'password':'ENC(Y/a5t0YI5fmWqx1NEEYoKQ==)', 'description':'repository credentials']
    cloud=['username':'cloud', 'password':'ENC(FtBGXIAp6Ks=)', 'description':'cloud credentials']
    ssh=['username':'ssh', 'password':'ENC(FtBGXIAp6Ks=)', 'description':'node credentials', 'type': 'SSH', 'privateKeyFile':'.ssh/id_rsa']
    vault=['description': 'vault credentials', 'key':'super/secret', 'usernameKey': "username", 'passwordKey': "password", 'type': 'HashicorpVault']
    saucelabs=['description': 'SauceLabs credentials', 'username': 'slUser', 'apiKey': 'slApiKey', 'type': 'SauceLabs']
    gitlab=['description': 'Gitlab credentials', 'token': 'ENC(Y/a5t0YI5fmWqx1NEEYoKQ==)', 'type': 'GitLabApiToken']
    string=['description': 'auth token', 'token': 'ENC(RUa6u7bp0SpkbxktC6E8GQ==)', 'type': 'StringCredential']
}
```

<sub>Tested Plugin Versions: hashicorp-vault-plugin-2.1.0.hpi, hashicorp-vault-credentials-plugin-0.0.9.hpi, credentials-binding-1.10.hpi, credentials-2.1.16.hpi, workflow-api-2.26.hpi, workflow-step-api-2.14.hpi, structs-1.10.hpi, plain-credentials-1.4.hpi, ssh-credentials-1.13.hpi, sauce-ondemand-1.164.hpi, maven-plugin-2.7.1.hpi, matrix-project-1.12.hpi, workflow-basic-steps-2.5.hpi, run-condition-1.0.hpi, workflow-cps-2.23.hpi, junit-1.23.hpi, workflow-job-2.16.hpi, script-security-1.40.hpi, javadoc-1.1.hpi, token-macro-2.3.hpi, workflow-scm-step-2.6.hpi, workflow-support-2.16.hpi, ace-editor-1.0.1.hpi, jquery-detached-1.2.1.hpi, scm-api-2.2.6.hpi, workflow-cps-2.23.hpi, gitlab-plugin-1.4.8.hpi, git-3.7.0.hpi, git-client-2.7.0.hpi, cloudbees-folder-6.1.0.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, display-url-api-2.2.0.hpi, mailer-1.20.hpi</sub>

CustomTool

```groovy
tools {
    custom = [[name: "cf", url: "http://someserver.com/binaries/cf-cli/cf-cli_6.23.1_linux_x86-64.tgz", subdir: ""]]
}
```

<sub>Tested Plugin Versions: custom-tools-plugin-0.4.4.hpi, extended-choice-parameter-0.28.hpi</sub>

Env

```groovy
env {
    variables{
        url='http://www.bbc.co.uk'
    }
}
```

<sub>Tested Plugin Versions: NA</sub>

Executors

```groovy
executors {
	active = 3
}
```

<sub>Tested Plugin Versions: NA</sub>

Files

```groovy
files {
    vaultUrl='http://localhost:51234' // default vaultUrl for any values with source 'HashicorpVault'
    vaultToken='f119a093-c4b1-4422-1c8c-47688d14fe9e' // default vaultToken for any values with source 'HashicorpVault'
    privateKey=[
            path: '${jenkinsHome}/.ssh/id_rsa',
            mode:'600',
            contents:'''ENC(LWk/qSmbM9xKKRX0o906g0GW1VSnFmeJpLx97XRwpS2GiIV0w/4eptWUb+/PvjG3rMotH+ZgwXAZH6W7yhFiJfe89INTAFheohNW
                            wOJasq078wVzN2dYWYanNFIj/d+ZL6CU0X77rEXMsCSBpln64jWcUpkF0WwfJamePohldKa9FsIq/ZtCt772Lwm1cZspBShrGVvW
                            ilcX0JE2IsuOOMUk0oyKrEbs4XhSLhz7cYfAO99O/QL4k9xb22Mr9IyqkTxm1ae33FbOgZ0Ih+aTlevRPLTWrju3SgFfHsqitJqy
                            PLL2urcQli7qs3N818Fmn2WrL3zPQ1Czj76AfW2P2ZBH5mzCa9BY8iDZ715R+0X2zrzeip35uhXvcgxB1las2ChG34GHu6HqGQVT
                            lzm42fHs4vPaSbI2XHciQExRtWLmx3m2HwLIzGrlDKpUVUXIlN0p2WjSf1yUblJkAmzkMbsz0oeIffmbTTASjgnVEpJ3fKTQw6J8
                            yr59Y/7FguchfSbKo4u0240HM5lI3XyzyL90uU0jIb8ycLdC17vJutMhq3lJvfAHXBf6lB1QyHML8Q7pij/cknJCp+QYScKCg5bE
                            zsY/2xYpKYwgSUvmvWh6ZRzlrMNPyDJq00mO5mbWT6VHyL5YgLLPOmBgQeKQO7RQRwGpMgNU6f6tzezgwX6il+7CvpAEj6/bpjo6
                            TLpntJDuqOpGku1gvpYE5C/4FHM2Hrr94YxnR1XyispAhNIe6J/QOozFRH8uJxMhjIPI/+I0y3NjWVManctSfW6ZyCpnaBT6+w1X
                            REX7jPF+XyETR0QnSSX0x1ByXB0ffHFrKr3mEK/5BfIILMK7O2K5rYd63pgRkaPBj7MlEXJafh7jyaVYG/zTUxSD6uV8tmCrdAjM
                            CEAHfgp9PYWOk55A8uIC+kYrBMhIcj9p7QaCBqx/MN1DNAF6X3tuww4uxB4r4i1fUScDe52G5Y5QjM1f9NKfsIddUpQCfs7m2y95
                            eY3w7q+4GBhl2KIkToNQ0pUkAPhCH+S9+EoLhvbVQUy7FC/U+zD7DGQFaYSBHLBP8ckY6IQHJQW4sGEezMWlRMiuACzNuMT9rGni
                            C5yJ7I5cz0nJM5zOLA5c/BFOJkTK9Oy9p/YjteEFFhMfMbQsUWGR3hFJSqSijkBUeMnzm9thUbW7C6rvAahxmzaokAl5WuPZZylo
                            IoZ/w+4QEPllMzh1W/nwV3Re0liPA5svqm1OHX2ckBzEXkN+KRz9LxyjJM0R0CYScxYlTNjB5aOJHao8ECVdH+RQQKcE3kW4DOSI
                            KorQjm7vhNlpzfg2YGoYJxNkjeneBLjnH2w9Ibydxhcv0S2TK932DA1UAqUDHYyzS385toYlaExIL4Vu2xV+LapNKBc0ObWmP0hM
                            jx3sYM2dZdsFlgmeHcFfLA4NYfJ/P5xV9Q4J4ndhL85T4xuevVrS0p21IZL36mbbcozrPqmnt2z3/0W8yYjes8dPQ85QDq+vHSU/
                            zU/OVlwgjFb3vqj50hqqvPziB6T4ro5eZIpxNi0PHnXYo/L/mw0t58ewS7GG4TARywJaoYkc8q/lhLalm2VU9P+Q5WG9EOt697Y5
                            i/7ly2MmkfVV4KeccqEbCXWDRVPMJVgFUPxoVjuqDndp4FXfSPBVl111bX+OgbtKBujUzO8dtWnAU5a2AApqhlokDMFaEcHIQXjx
                            c1iYrTPy+62gVakWA+uFB4qOVz4kGuvFaWaRWLnkYIKzqQnYA+d5kaun12aozZXAR0LnIGchZ9su4gVP8Ljti/dFEyns+fC7Sx3M
                            uwfqSWhdwnIwajwm73HPaU5FC9QsaRoj2p3hO5Of9TxeBa0UEZurRfBcgbSJDBOcWeNY7zgB2Yer2yt34FRl8OHW/xZnuHFJumSe
                            5Q+KRqn/+lrFCfi8y7Egx/tXNroHyEeNw+AVSdXx0NpaDomMs/uLhzCbtinOchl4Y7Xr2HhWoL8CQPgkvbHSRrDtVYrbvz9njgGC
                            MXDcSU77OYs+oEXtWF2N7DZvYj4y8lzKTM1m0pYuTFTmNBGiZOjOM+8kvk/jOf4rKmxfQ0BRgq8JmcSMFBL/yvN4J2SZis89wlpx
                            dAPxxbjX+NZXS/01ZdpYPvGtJTlzmBq6DcL0ImErp5Cyp5LDw5coniOsXy5EUMzw30eNeLSd6FRiBZlFYq499qfxjHerJmPGCj4S
                            0r/Ne3+qJ7S5eqWOMTB6o8mZHNBnukUq1mA8DqaClDjvbn9tNqH9VgDMPFJpTdfuSmT5RuiNfhNA/+/o2oYIE6LFK3MTirWdDycn
                            uWlFRqM9UKGk9513lhiVd7Gi33NPOp6DPLzZ9mMKTHQNymZ5Fv3tkT+nTw==)'''
    ]
    settingsXml=[
            path: 'settings.xml',
            mode:'600',
            contents:'''
                        <?xml version="1.0" encoding="UTF-8"?>
                        <settings>
                        <profiles>
                              <profile>
                                 <id>myprofile</id>
                                 <properties>
                                     <sonar.host.url>
                                         http://localhost:9000
                                     </sonar.host.url>
                                 </properties>
                                 <repositories>
                                   <repository>
                                     <name>Jenkins</name>
                                      <id>repo.jenkins-ci.org</id>
                                      <url>http://repo.jenkins-ci.org/public/</url>
                                  </repository>
                                    <repository>
                                       <name>Spring Snapshots</name>
                                       <id>spring-snapshots</id>
                                       <url>http://repo.spring.io/snapshot</url>
                                       <snapshots>
                                          <enabled>true</enabled>
                                       </snapshots>
                                    </repository>
                                    <repository>
                                       <name>Spring Milestones</name>
                                       <id>spring-milestones</id>
                                       <url>http://repo.spring.io/milestone</url>
                                    </repository>
                                 </repositories>
                                 <pluginRepositories>
                                    <pluginRepository>
                                       <id>spring-snapshots</id>
                                       <url>http://repo.spring.io/snapshot</url>
                                    </pluginRepository>
                                    <pluginRepository>
                                       <id>spring-milestones</id>
                                       <url>http://repo.spring.io/milestone</url>
                                    </pluginRepository>
                                    <pluginRepository>
                                      <name>Jenkins</name>
                                       <id>repo.jenkins-ci.org</id>
                                       <url>http://repo.jenkins-ci.org/public/</url>
                                   </pluginRepository>
                                 </pluginRepositories>
                              </profile>
                           </profiles>
                           <activeProfiles>
                              <activeProfile>myprofile</activeProfile>
                           </activeProfiles>
                        </settings>
'''
    ]
    empty=[
            path: 'empty.txt',
            mode:'777',
            contents:''
    ]
    id_rsa_vault=[
            path:'${jenkinsHome}/.ssh/id_rsa_vault',
            mode:'600',
            source: 'HashicorpVault',
            key: 'secret/id_rsa',
            url:'http://localhost:51235',
            token: 'f119a093-c4b1-4422-1c8c-47688d14fe9e',
            contentKey: "contents", // defaults to 'contents'
            base64Encoded: true // defaults to false
    ]
    passcode_from_default_vault_config=[
            path:'${jenkinsHome}/.ssh/passcode',
            mode:'600',
            source: 'HashicorpVault',
            key: 'secret/passcode',
    ]
    password_not_encoded=[
            path:'${jenkinsHome}/.ssh/password',
            mode:'600',
            source: 'HashicorpVault',
            key: 'secret/password',
            url:'http://localhost:51235',
            token: 'f119a093-c4b1-4422-1c8c-47688d14fe9e',
            contentKey: "password",
            base64Encoded: false
    ]
}
```

<sub>Tested Plugin Versions: NA</sub>

Gerrit

```groovy
tools {
  gerrit =
          [
                  [
                        name: 'Fake Gerrit',
                        gerritHostName: 'gerrit.sandbox.local',
                        gerritFrontEndUrl: 'http://gerrit.sandbox/',
                        gerritSshPort: 29418,
                        gerritUserName: 'jenkins',
                        gerritEMail: 'sandbox@example.com',
                        gerritAuthKeyFile: '${jenkinsHome}/gerrit_rsa',
                        gerritAuthKeyFilePassword: 'password',
                        gerritBuildStartedVerifiedValue: 0,
                        gerritBuildStartedCodeReviewValue: 0,
                        gerritBuildSuccessfulVerifiedValue: 1,
                        gerritBuildSuccessfulCodeReviewValue: 0,
                        gerritBuildFailedVerifiedValue: -1,
                        gerritBuildFailedCodeReviewValue: 0,
                        gerritBuildUnstableVerifiedValue: -1,
                        gerritBuildUnstableCodeReviewValue: 0,
                        gerritBuildNotBuiltVerifiedValue: 0,
                        gerritBuildNotBuiltCodeReviewValue: 0,
                        enableManualTrigger: true,
                        enablePluginMessages: true,
                        buildScheduleDelay: 3,
                        dynamicConfigRefreshInterval: 30,
                        enableProjectAutoCompletion: true,
                        projectListFetchDelay: 0,
                        projectListRefreshInterval: 3600,
                        watchdogTimeoutMinutes: 1,
                        verdictCategories: [
                                [ 'verdictValue':'CRVW', 'verdictDescription':'Code Review'],
                                [ 'verdictValue':'VRIF', 'verdictDescription':'Verified']
                        ] as LinkedList,
                        buildCurrentPatchesOnly: [
                            enabled: true,
                            abortNewPatchsets: false,
                            abortManualPatchsets: false
                        ]
                  ]
          ]
}
```

<sub>Tested Plugin Versions: gerrit-trigger-2.17.2.hpi</sub>

GitHub

```groovy
github {
  apiUrl = "https://github.com/"
  name = "GitHub"
}
```

<sub>Tested Plugin Versions: github-branch-source-2.3.2.hpi, github-1.28.1.hpi, credentials-2.1.16.hpi, display-url-api-2.2.0.hpi, git-3.7.0.hpi, github-api-1.90.hpi, scm-api-2.2.6.hpi, structs-1.10.hpi, git-client-2.7.0.hpi, mailer-1.20.hpi, matrix-project-1.12.hpi, ssh-credentials-1.13.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, junit-1.23.hpi, script-security-1.40.hpi, workflow-api-2.26.hpi, workflow-step-api-2.14.hpi, workflow-scm-step-2.6.hpi, jackson2-api-2.8.10.1.hpi, plain-credentials-1.4.hpi, token-macro-2.3.hpi, workflow-job-2.16.hpi, workflow-support-2.16.hpi</sub>

Gitlab

```groovy
gitlab=[
    gitlab: [
            url:"http://gitlab.platform.com/",
            apiTokenId:"gitlabCredentials",
            ignoreCertificateErrors:false,
            connectionTimeout:15,
            readTimeout:15
    ],
    gitlabUsingDefaults: [
            url:"http://gitlab.platform.com/",
            apiTokenId:"gitlabCredentials"
    ]
]

credentials {
    gitlabCredentials = ['description': 'Gitlab credentials', 'token': 'ENC(Y/a5t0YI5fmWqx1NEEYoKQ==)', 'type': 'GitLabApiToken']
}
```

<sub>Tested Plugin Versions: gitlab-plugin-1.4.8.hpi, credentials-2.1.16.hpi, plain-credentials-1.4.hpi, git-3.7.0.hpi, git-client-2.7.0.hpi, matrix-project-1.12.hpi, workflow-step-api-2.14.hpi, ssh-credentials-1.13.hpi, junit-1.23.hpi, script-security-1.40.hpi, mailer-1.20.hpi, scm-api-2.2.6.hpi, workflow-scm-step-2.6.hpi, structs-1.10.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, display-url-api-2.2.0.hpi, workflow-api-2.26.hpi</sub>

Gradle

```groovy
tools {
    gradle =
            [
                    [name: "gradle-2", url: "https://services.gradle.org/distributions/gradle-2.14-bin.zip"],
                    [name: "gradle-3", url: "https://services.gradle.org/distributions/gradle-3.0-bin.zip"],
                    [name: "gradle-2-1", version: "2.1"]
            ]
}
```

<sub>Tested Plugin Versions: gradle-1.25.hpi</sub>

HashicorpVault

```groovy
hashicorpvault{
    url= 'http://vault.server.url'
    credentialsId= 'vault'
}

credentials {
    vault=['token':'somesortoftoken', 'description':'vault credentials', type:"HashicorpVaultTokenCredential"]
}
```

<sub>Tested Plugin Versions: hashicorp-vault-plugin-2.1.0.hpi, workflow-scm-step-2.6.hpi, cloudbees-folder-6.1.0.hpi, credentials-2.1.16.hpi, workflow-step-api-2.14.hpi, structs-1.10.hpi, ssh-credentials-1.13.hpi</sub>

HipChat

```groovy
hipchat {
  server = "hipchat.com"
  v2Enabled = true
  room = "Default Room"
  sendAs = "Jenkins"
  credentialsId = "hipchatToken"
}

credentials {
    hipchatToken=['description': 'HipChat Token', 'token': 'helloworld', 'type': 'StringCredential']
}
```

<sub>Tested Plugin Versions: hipchat-2.1.1.hpi, workflow-step-api-2.14.hpi, credentials-2.1.16.hpi, display-url-api-2.2.0.hpi, junit-1.23.hpi, matrix-project-1.12.hpi, plain-credentials-1.4.hpi, token-macro-2.3.hpi, structs-1.10.hpi, script-security-1.40.hpi, ssh-credentials-1.13.hpi, workflow-job-2.16.hpi, workflow-api-2.26.hpi, scm-api-2.2.6.hpi, workflow-support-2.16.hpi</sub>

Java

```groovy
tools {
    java =
            [
                    [name: "jdk8", url: "http://localhost/jdk-8u102-oth-JPR"],
                    [name: "jdk7", url: "http://localhost/jdk-7u80-oth-JPR"]
            ]
}
```

<sub>Tested Plugin Versions: NA</sub>

JenkinsHost

```groovy
jenkins {
    host {
        url = 'http://example.com/jenkins'
        adminEmail = 'admin@example.com'
    }
}
```

<sub>Tested Plugin Versions: NA</sub>

Jira

```groovy
jira {
    sites {
        foo {
            url = "https://jira.foo.com/"
            alternativeUrl = "https://jira.foo.com/"
            userName = "admin"
            password = "secret"
            supportsWikiStyleComment = false
            recordScmChanges = false
            updateJiraIssueForAllStatus = false
            userPattern = null
            useHTTPAuth = false
            groupVisibility = null
            roleVisibility = null
        }
        bar {
            url = "http://jira.bar.com"
            alternativeUrl = "http://jira.bar.com"
            userName = "user"
            password = "p@ss"
            supportsWikiStyleComment = true
            recordScmChanges = true
            updateJiraIssueForAllStatus = true
            userPattern = "5"
            useHTTPAuth = true
            groupVisibility = "6"
            roleVisibility = "7"
        }
    }
}
```

<sub>Tested Plugin Versions: jira-2.2.1.hpi, matrix-project-1.12.hpi, mailer-1.20.hpi, junit-1.23.hpi, script-security-1.40.hpi, structs-1.10.hpi, workflow-step-api-2.14.hpi, workflow-api-2.26.hpi, junit-1.23.hpi, scm-api-2.2.6.hpi, display-url-api-2.2.0.hpi, mailer-1.20.hpi</sub>

JobDSL

```groovy
jobdsl {
    scriptSecurityEnabled = false // scriptSecurityEnabled defaults to false
    jobdsl=[url:"https://github.com/buildit/sample-pipelines", targets:"jobs/**/*.groovy", branch:"*/master", additionalClasspath:"src/main/groovy"]
    //jobdslWithLabel=[url:"https://github.com/buildit/sample-pipelines", targets:"jobs/**/*.groovy", branch:"*/master", label:"foo"]
    jobdslWithCredentials=[url:"https://github.com/buildit/sample-pipelines", targets:"jobs/**/*.groovy", branch:"*/master", credentialsId:"git"]
}

credentials {
    git=['username':'test', 'password':'p@ssword', 'description':'git credentials']
}
```

<sub>Tested Plugin Versions: ace-editor-1.0.1.hpi, jquery-detached-1.2.1.hpi, workflow-cps-global-lib-2.5.hpi, workflow-cps-2.23.hpi, workflow-scm-step-2.6.hpi, cloudbees-folder-6.1.0.hpi, git-client-2.7.0.hpi, git-server-1.7.hpi, scm-api-2.2.6.hpi, structs-1.10.hpi, ssh-credentials-1.13.hpi, credentials-2.1.16.hpi, workflow-step-api-2.14.hpi, workflow-api-2.26.hpi, workflow-support-2.16.hpi, ace-editor-1.0.1.hpi, script-security-1.40.hpi, git-3.7.0.hpi, matrix-project-1.12.hpi, mailer-1.20.hpi, junit-1.23.hpi, job-dsl-1.64.hpi, credentials-binding-1.10.hpi, credentials-2.1.16.hpi, plain-credentials-1.4.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, display-url-api-2.2.0.hpi, git-client-2.7.0.hpi</sub>

Kubernetes

```groovy
clouds {
    kubernetes {
        sandbox {
            [
                cloudName = "My Cloud",
                serverUrl = 'https://rancher.com/r/projects/1a120948/kubernetes:6443',
                namespace = 'default',
                jenkinsUrl = 'http://jenkins-k8s.platform.com/',
                serverCertificate = 'QmFzaWMgT0VFMk1UWkZSRVZHTlVKQk9UUkROVGM0TjBNNldtZFRjRlZ5TkVRM19HOXFRMVkzWkVWQldHbzVOMU5YZEdoRk5GcEJaVTVSWWpGS1lsVkJUQT08',
                skipTlsVerify = true,
                credentialsId = 'credentialsId',
                jenkinsTunnel = 'jenkinsTunnel',
                containerCapStr = '100',
                connectTimeout = 300,
                readTimeout = 300,
                retentionTimeout = 300,
                podTemplates = [
                    [
                        inheritFrom: 'base',
                        name: 'myPod',
                        namespace: 'default',
                        image : 'ci-base',
                        command : 'cat',
                        args : '',
                        remoteFs : '',
                        label : '',
                        serviceAccount : '',
                        nodeSelector : '',
                        resourceRequestCpu : '',
                        resourceRequestMemory : '',
                        resourceLimitCpu : '',
                        resourceLimitMemory : '',
                        privileged : true,
                        alwaysPullImage : false,
                        instanceCap : Integer.MAX_VALUE,
                        slaveConnectTimeout : 100,
                        idleMinutes : 1,
                        customWorkspaceVolumeEnabled : false,
                        envVars : [
                                [type: "SecretEnvVar", key:'', secretName:'', secretKey:''],
                                [type: "KeyValueEnvVar", key:'', value:'']
                        ],
                        workspaceVolume : [type: "PersistentVolumeClaimWorkspaceVolume", claimName:'', readOnly: true],
                        podVolumes : [
                                [type: "ConfigMapVolume", mountPath:'', configMapName:''],
                                [type: "EmptyDirVolume", mountPath:'', memory: true],
                                [type: "HostPathVolume", hostPath:'', mountPath:''],
                                [type: "NfsVolume", serverAddress:'', serverPath:'', readOnly: true, mountPath:''],
                                [type: "PersistentVolumeClaim", mountPath:'', claimName:'', readOnly: true],
                                [type: "SecretVolume", mountPath:'', configMapName:'']
                        ],
                        nodeUsageMode : [type: "NORMAL" /* type: "EXCLUSIVE" */],
                        annotations : [
                                [key:'', value:''],
                        ],
                        imagePullSecrets : [
                                [name:''],
                        ],
                        containerTemplates :
                                [
                                    [
                                    inheritFrom: '',
                                    name: '',
                                    namespace: '',
                                    image : 'imageName',
                                    command : '',
                                    args : '',
                                    remoteFs : '',
                                    label : '',
                                    serviceAccount : '',
                                    nodeSelector : '',
                                    resourceRequestCpu : '',
                                    resourceRequestMemory : '',
                                    resourceLimitCpu : '',
                                    resourceLimitMemory : '',
                                    privileged : false,
                                    alwaysPullImage : false,
                                    instanceCap : Integer.MAX_VALUE,
                                    slaveConnectTimeout : 100,
                                    idleMinutes : 1,
                                    customWorkspaceVolumeEnabled : false,
                                    envVars : [
                                            [type: "SecretEnvVar", key:'', secretName:'', secretKey:''],
                                            [type: "SecretEnvVar", key:'', secretName:'', secretKey:'']
                                    ],
                                    ports : [
                                            [name:'', containerPort:8000, hostPort:8000],
                                    ],
                                    livenessProbe : [execArgs: "", timeoutSeconds: 1, initialDelaySeconds: 1, failureThreshold: 1, periodSeconds: 1, successThreshold: 1],
                                    ]
                        ]
                    ]
                ]
            ]
        }
    }
}

/*
Minimum config as follows

clouds {
    kubernetes {
        sandbox {
            [
                    cloudName = "My Cloud",
                    serverUrl = 'https://rancher.com/r/projects/1a120948/kubernetes:6443',
                    namespace = 'default',
                    jenkinsUrl = 'http://jenkins-k8s.platform.com/',
                    serverCertificate = 'QmFzaWMgT0VFMk1UWkZSRVZHTlVKQk9UUkROVGM0TjBNNldtZFRjRlZ5TkVRM19HOXFRMVkzWkVWQldHbzVOMU5YZEdoRk5GcEJaVTVSWWpGS1lsVkJUQT08',
                    skipTlsVerify = true,
                    credentialsId = 'credentialsId',
                    jenkinsTunnel = 'jenkinsTunnel',
                    containerCapStr = '100',
                    connectTimeout = 300,
                    readTimeout = 300,
                    retentionTimeout = 300
            ]
        }
    }
}
 */
```

<sub>Tested Plugin Versions: kubernetes-1.1.4.hpi, workflow-step-api-2.14.hpi, credentials-2.1.16.hpi, durable-task-1.15.hpi, variant-1.1.hpi, structs-1.10.hpi</sub>

LdapAuthN

```groovy
auth {
    ldap {
        server = "someserver.com" // no default value
        rootDN = 'dc=com' // no default value
        bindDN = "uid=binduser,cn=sysaccounts,cn=etc,dc=mavel,dc=com" // no default value
        bindPass = "12345678910" // no default value
        userSearchBase = 'cn=users,cn=accounts' // defaults to this value if missing
        userSearch = 'uid={0}' // defaults to this value if missing
        groupSearchBase = 'cn=groups,cn=accounts' // defaults to this value if missing
        groupSearchFilter = '' // defaults to this value if missing
        groupMembershipFilter = 'memberOf' // defaults to this value if missing
        inhibitInferRootDN = false // defaults to this value if missing
        disableMailAddressResolver = false // defaults to this value if missing
        displayNameAttributeName = 'displayname' // defaults to this value if missing
        mailAddressAttributeName = 'mail' // defaults to this value if missing
    }
}
```

<sub>Tested Plugin Versions: NA</sub>

LdapAuthZ

```groovy
auth {
    ldap {
        adminGroup = "my-jenkins-administrators"
        adminRoles = ["hudson.model.Hudson.Administer"] // remove if you want defaults below
        developerGroup = "my-jenkins-developers"
        developerRoles = ["hudson.model.Computer.Disconnect"] // remove if you want defaults below
        viewersGroup = "my-jenkins-viewers"
        viewerRoles = ["hudson.model.Computer.Build"] // remove if you want defaults below
        webhookGroup = "my-jenkins-webhook"
        webhookRoles = ["hudson.model.Item.Build"] // remove if you want defaults below
        anonymousRoles = ["hudson.model.View.Read"]
    }
}

/**

 Defaults are as follows

 def administrators = [

 name: "jenkins-administrators",
 roles: [
 "hudson.model.Computer.Build",
 "hudson.model.Computer.Configure",
 "hudson.model.Computer.Connect",
 "hudson.model.Computer.Create",
 "hudson.model.Computer.Delete",
 "hudson.model.Computer.Disconnect",
 "hudson.model.Computer.Provision",
 "com.cloudbees.plugins.credentials.CredentialsProvider.Create",
 "com.cloudbees.plugins.credentials.CredentialsProvider.Delete",
 "com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains",
 "com.cloudbees.plugins.credentials.CredentialsProvider.Update",
 "com.cloudbees.plugins.credentials.CredentialsProvider.View",
 "hudson.model.Hudson.Administer",
 "hudson.model.Hudson.ConfigureUpdateCenter",
 "hudson.model.Hudson.Read",
 "hudson.model.Hudson.RunScripts",
 "hudson.model.Hudson.UploadPlugins",
 "hudson.model.Item.Build",
 "hudson.model.Item.Cancel",
 "hudson.model.Item.Configure",
 "hudson.model.Item.Create",
 "hudson.model.Item.Delete",
 "hudson.model.Item.Discover",
 "hudson.model.Item.Move",
 "hudson.model.Item.Read",
 "hudson.model.Item.Workspace",
 "hudson.model.Run.Delete",
 "hudson.model.Run.Replay",
 "hudson.model.Run.Update",
 "hudson.model.View.Configure",
 "hudson.model.View.Create",
 "hudson.model.View.Delete",
 "hudson.model.View.Read",
 "hudson.scm.SCM.Tag",
 "hudson.security.HealthCheck",
 "hudson.security.ThreadDump",
 "hudson.security.View"
 ]
 ]

 def developers = [
 name: "jenkins-developers",
 roles: [
 "hudson.model.Computer.Build",
 "hudson.model.Computer.Configure",
 "hudson.model.Computer.Connect",
 "hudson.model.Computer.Create",
 "hudson.model.Computer.Delete",
 "hudson.model.Computer.Disconnect",
 "hudson.model.Computer.Provision",
 "com.cloudbees.plugins.credentials.CredentialsProvider.Create",
 "com.cloudbees.plugins.credentials.CredentialsProvider.Delete",
 "com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains",
 "com.cloudbees.plugins.credentials.CredentialsProvider.Update",
 "com.cloudbees.plugins.credentials.CredentialsProvider.View",
 "hudson.model.Hudson.Read",
 "hudson.model.Item.Build",
 "hudson.model.Item.Cancel",
 "hudson.model.Item.Configure",
 "hudson.model.Item.Create",
 "hudson.model.Item.Delete",
 "hudson.model.Item.Discover",
 "hudson.model.Item.Move",
 "hudson.model.Item.Read",
 "hudson.model.Item.Workspace",
 "hudson.model.Run.Delete",
 "hudson.model.Run.Replay",
 "hudson.model.Run.Update",
 "hudson.model.View.Configure",
 "hudson.model.View.Create",
 "hudson.model.View.Delete",
 "hudson.model.View.Read",
 "hudson.scm.SCM.Tag",
 "hudson.security.HealthCheck",
 "hudson.security.ThreadDump",
 "hudson.security.View"
 ]
 ]

 def viewers = [
 name: "jenkins-viewer",
 roles: [
 "hudson.model.Hudson.Read",
 "hudson.model.Item.Read",
 "hudson.model.View.Configure",
 "hudson.model.View.Create",
 "hudson.model.View.Delete",
 "hudson.model.View.Read",
 "hudson.security.HealthCheck",
 "hudson.security.View"
 ]
 ]

 def anonymous = [
 name: "anonymous",
 roles: []
 ]

 def webhook = [
 name: "jenkins-webhook",
 roles: [
 "hudson.model.Item.Build"
 ]
 ]
 **/
```

<sub>Tested Plugin Versions: NA</sub>

Libraries

```groovy
libraries = [
    foo : [
            defaultVersion : 'master',
            implicit : true,
            allowVersionOverride : false,
            scm : [
                    url : 'https://git.example.com/foo.git',
                    credentialsId : "git"
            ]
    ],
    bar : [
            defaultVersion : 'master',
            implicit : false,
            allowVersionOverride : true,
            scm : [
                    url : 'https://git.example.com/bar.git'
            ]
    ],
    baz : [
            defaultVersion : 'master',
            implicit : false,
            allowVersionOverride : true,
            scm : [
                    url : 'https://git.example.com/baz.git',
                    branch : 'baz_test'
            ]
    ]
]

credentials {
    git=['username':'test', 'password':'p@ssword', 'description':'git credentials']
}
```

<sub>Tested Plugin Versions: ace-editor-1.0.1.hpi, jquery-detached-1.2.1.hpi, workflow-cps-global-lib-2.5.hpi, workflow-cps-2.23.hpi, workflow-scm-step-2.6.hpi, cloudbees-folder-6.1.0.hpi, git-client-2.7.0.hpi, git-server-1.7.hpi, scm-api-2.2.6.hpi, structs-1.10.hpi, ssh-credentials-1.13.hpi, credentials-2.1.16.hpi, workflow-step-api-2.14.hpi, workflow-api-2.26.hpi, workflow-support-2.16.hpi, ace-editor-1.0.1.hpi, script-security-1.40.hpi, git-3.7.0.hpi, matrix-project-1.12.hpi, mailer-1.20.hpi, junit-1.23.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, display-url-api-2.2.0.hpi, mailer-1.20.hpi</sub>

Logstash

```groovy
#!/usr/bin/env groovy

logstash {
  type = 'SYSLOG'
  host = '10.113.140.169'
  port = 5122
  key = 'logstash'
}
```

<sub>Tested Plugin Versions: logstash-1.3.0.hpi, mask-passwords-2.10.1.hpi, structs-1.10.hpi, junit-1.23.hpi, script-security-1.40.hpi, workflow-step-api-2.14.hpi, workflow-api-2.26.hpi, scm-api-2.2.6.hpi</sub>

MailExt

```groovy
mail {
  host = "159.34.192.34"
  defaultSuffix = "@somewhere.com"
}
```

<sub>Tested Plugin Versions: email-ext-2.58.hpi, mailer-1.20.hpi, matrix-project-1.12.hpi, script-security-1.40.hpi, token-macro-2.3.hpi, junit-1.23.hpi, script-security-1.40.hpi, workflow-step-api-2.14.hpi, workflow-api-2.26.hpi, scm-api-2.2.6.hpi, structs-1.10.hpi, display-url-api-2.2.0.hpi, workflow-job-2.16.hpi, workflow-support-2.16.hpi</sub>

Mail

```groovy
mail {
  host = "159.34.192.34"
  defaultSuffix = "@somewhere.com"
}
```

<sub>Tested Plugin Versions: NA</sub>

Master

```groovy
nodes {
    master {
        labels = ['label1', 'label2']
    }
}
```

<sub>Tested Plugin Versions: NA</sub>

Maven

```groovy
tools {
    maven =
            [
                    [name: "maven-3.3.3", version: "3.3.3", url: "http://test_url"],  [name: "maven-3.3.9", version: "3.3.9"]
            ]
}
```

<sub>Tested Plugin Versions: NA</sub>

Mesos

```groovy
clouds {
    mesos {
        sandbox {
            [
                    cloudName = "My Cloud",
                    nativeLibraryPath = "/usr/lib/libmesos.so",
                    master = "10.113.140.187:5050",
                    description = "",
                    frameworkName = "jenkins",
                    role = "*",
                    slavesUser = "jenkins",
                    credentialsId = "",
                    principal = "jenkins",
                    secret = "",
                    checkpoint = false,
                    onDemandRegistration = true,
                    jenkinsURL = "http://mesos3.platform.com/",
                    declineOfferDuration = "600000",
                    cloudID = "",
                    slaves = [
                            [
                                    labelString           : "label",
                                    slaveCpus             : "0.2",
                                    slaveMem              : "1024",
                                    minExecutors          : "3",
                                    maxExecutors          : "5",
                                    executorCpus          : "0.1",
                                    executorMem           : "128",
                                    remoteFSRoot          : "jenkins",
                                    idleTerminationMinutes: "3",
                                    slaveAttributes       : "",
                                    containerInfo         : [
                                            dockerImage                : "",
                                            dockerPrivilegedMode       : false,
                                            dockerForcePullImage       : false,
                                            dockerImageCustomizable    : false,
                                            useCustomDockerCommandShell: false,
                                            customDockerCommandShell   : "",
                                            networking                 : "BRIDGE"
                                    ],
                                    uris                  : [
                                            [uri: "", executable: true, extract: true]
                                    ],
                                    volumes               : [
                                            [containerPath: "", hostPath: "", readOnly: true]
                                    ],
                                    ports                 : [
                                            [containerPort: 8080, hostPort: 8080, protocol: ""]
                                    ],
                                    networks              : [
                                            [name: "test"]
                                    ],
                                    params                : [key: "value", another: "value"]
                            ]
                    ]
            ]
        }
    }
}
```

<sub>Tested Plugin Versions: mesos-0.14.1.hpi, credentials-2.1.16.hpi, metrics-3.1.2.10.hpi, jackson2-api-2.8.10.1.hpi, structs-1.10.hpi</sub>

MultiCloud

```groovy
clouds {
    kubernetes {
        sandbox {
            [
                    cloudName = "My Cloud",
                    serverUrl = 'https://rancher.com/r/projects/1a120948/kubernetes:6443',
                    namespace = 'default',
                    jenkinsUrl = 'http://jenkins-k8s.platform.com/',
                    serverCertificate = 'QmFzaWMgT0VFMk1UWkZSRVZHTlVKQk9UUkROVGM0TjBNNldtZFRjRlZ5TkVRM19HOXFRMVkzWkVWQldHbzVOMU5YZEdoRk5GcEJaVTVSWWpGS1lsVkJUQT08',
                    skipTlsVerify = true,
                    credentialsId = 'credentialsId',
                    jenkinsTunnel = 'jenkinsTunnel',
                    containerCapStr = '100',
                    connectTimeout = 300,
                    readTimeout = 300,
                    retentionTimeout = 300
            ]
        }
    }
    mesos {
        sandbox {
            [
                    cloudName = "My Cloud",
                    nativeLibraryPath = "/usr/lib/libmesos.so",
                    master = "10.113.140.187:5050",
                    description = "",
                    frameworkName = "jenkins",
                    role = "*",
                    slavesUser = "jenkins",
                    credentialsId = "",
                    principal = "jenkins",
                    secret = "",
                    checkpoint = false,
                    onDemandRegistration = true,
                    jenkinsURL = "http://mesos3.platform.com/",
                    declineOfferDuration = "600000",
                    cloudID = "",
                    slaves = [
                            [
                                    labelString           : "label",
                                    slaveCpus             : "0.2",
                                    slaveMem              : "1024",
                                    minExecutors          : "3",
                                    maxExecutors          : "5",
                                    executorCpus          : "0.1",
                                    executorMem           : "128",
                                    remoteFSRoot          : "jenkins",
                                    idleTerminationMinutes: "3",
                                    slaveAttributes       : "",
                                    containerInfo         : [
                                            dockerImage                : "",
                                            dockerPrivilegedMode       : false,
                                            dockerForcePullImage       : false,
                                            dockerImageCustomizable    : false,
                                            useCustomDockerCommandShell: false,
                                            customDockerCommandShell   : "",
                                            networking                 : "BRIDGE"
                                    ],
                                    uris                  : [
                                            [uri: "", executable: true, extract: true]
                                    ],
                                    volumes               : [
                                            [containerPath: "", hostPath: "", readOnly: true]
                                    ],
                                    ports                 : [
                                            [containerPort: 8080, hostPort: 8080, protocol: ""]
                                    ],
                                    networks              : [
                                            [name: "test"]
                                    ],
                                    params                : [key: "value", another: "value"]
                            ]
                    ]
            ]
        }
    }
}
```

<sub>Tested Plugin Versions: mesos-0.14.1.hpi, credentials-2.1.16.hpi, metrics-3.1.2.10.hpi, jackson2-api-2.8.10.1.hpi, structs-1.10.hpi, kubernetes-1.1.4.hpi, workflow-step-api-2.14.hpi, credentials-2.1.16.hpi, durable-task-1.15.hpi, variant-1.1.hpi</sub>

NodeJs

```groovy
tools {
    node =
            [
                [
                    name: "nodejs-4.2.3",
                    version: "4.2.3"
                ]
            ]
}
```

<sub>Tested Plugin Versions: nodejs-0.2.1.hpi</sub>

Nodes

```groovy
nodes {
    slaves {
        node01 = ['name': 'node 01', 'description': 'First node', remoteFS: '/tmp', numExecutors: '1', launchType: 'JNLP', jnlpTunnel: ':443', jnlpVmArgs: '-', env: ['android_home': '/android', 'java_home': '/java']]
        node02 = ['name': 'node 02', 'description': 'Second node', remoteFS: '/var', mode: 'EXCLUSIVE']
        node03 = ['name': 'node 03', 'description': 'Third node', remoteFS: '/var', mode: 'EXCLUSIVE', launchType: 'SSH', host: 'sshHost', port: 1234, credentialsId: 'sshCredentials', javaPath: 'sshJavaPath']
        node04 = ['name': 'node 04', 'description': 'Fourth node', remoteFS: '/var', numExecutors: '15', launchType: 'SSH', host: 'sshHost', credentialsId: 'sshCredentials']
    }
}
```

<sub>Tested Plugin Versions: NA</sub>

Passwords

```groovy
env {
    passwords {
        repository='ENC(Y/a5t0YI5fmWqx1NEEYoKQ==)'
    }
}
```

<sub>Tested Plugin Versions: envinject-1.91.3.hpi, maven-plugin-2.7.1.hpi, mailer-1.20.hpi, javadoc-1.1.hpi, junit-1.23.hpi, display-url-api-2.2.0.hpi, workflow-step-api-2.14.hpi, script-security-1.40.hpi, structs-1.10.hpi, workflow-api-2.26.hpi, scm-api-2.2.6.hpi</sub>

Plugins

```groovy
plugins=[
    // default artifactPlugin = "http://updates.jenkins-ci.org/download/plugins/[module]/[revision]/[module].[ext]"
    first: [
            artifactPattern: 'http://localhost:4567/[module]-[revision].[ext]',
            pluginArtifacts: ['ace-editor:1.0.1', 'maven-plugin:2.7.1']
    ],
    second: [
            artifactPattern: 'http://localhost:4567/[module]-[revision].[ext]',
            pluginArtifacts: ['active-directory:2.0']
    ]
]
```

<sub>Tested Plugin Versions: NA</sub>

Proxy

```groovy
proxy {
  host="proxy.sandbox.local"
  port="3128"
  userName="spiderman"
  password="p3t3rPark3r"
  noProxyHost="localhost,127.0.0.1"
}
```

<sub>Tested Plugin Versions: NA</sub>

RoleStrategy

```groovy
roleStrategy {
    roles = [
        [
            'name': 'anonymous',
            'permissions': [
                'hudson.model.Item.Read',
                'hudson.model.Item.Build'
            ]
        ],
        [
            'name': 'admin',
            'permissions': [
                'hudson.model.Item.Workspace',
                'hudson.model.Run.Delete'
            ],
            'members': [
                'john.doe'
            ]
        ],
        [
            'name': 'developer',
            'permissions': [
                'hudson.model.Item.Configure'
            ],
            'members': [
                'david.doe'
            ]
        ]
    ] as LinkedList
}
```

<sub>Tested Plugin Versions: matrix-auth-1.4.hpi, icon-shim-2.0.3.hpi, role-strategy-2.3.2.hpi</sub>

ScriptSecurity

```groovy
scriptsecurity {
    approvedSignatures=[
            "field hudson.ProxyConfiguration name",
            "field hudson.ProxyConfiguration port"
    ]
    approvedScriptHashes=[
        "35f0f8ad5c16ff2873f66efe9bf556aba0512bef"
    ]
}
```

<sub>Tested Plugin Versions: script-security-1.40.hpi</sub>

Shell

```groovy
tools {
    shell = [path: '/bin/bash']
}
```

<sub>Tested Plugin Versions: NA</sub>

SonarQube

```groovy
tools {
    sonar {
        qube =
        [
                [
                        name            : "SONAR",
                        serverUrl       : "http://10.113.140.170:9000/sonar",
                        sonarLogin      : "admin", //only required for v5.1-
                        sonarPassword   : "ENC(Y/a5t0YI5fmWqx1NEEYoKQ==)", //only required for v5.1-
                        databaseUrl     : "jdbc:mysql://10.113.140.170:3306/sonarqube?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useConfigs=maxPerformance", //only required for v5.1-
                        databaseLogin   : "sonarqube", //only required for v5.1-
                        databasePassword: "Y/a5t0YI5fmWqx1NEEYoKQ==" //only required for v5.1-
                ],
                [
                        name            : "SONAR6",
                        version         : "5.3 or higher", //defaults to "5.1 or lower" so not necessary with above example
                        serverUrl       : "http://10.113.140.170:9000/sonar",
                        serverAuthenticationToken: "someToken" //only required for v5.3+
                ]
        ]
    }
}
```

<sub>Tested Plugin Versions: sonar-2.6.1.hpi, maven-plugin-2.7.1.hpi, mailer-1.20.hpi, javadoc-1.1.hpi, junit-1.23.hpi, jquery-1.11.2-0.hpi, display-url-api-2.2.0.hpi, workflow-step-api-2.14.hpi, workflow-api-2.26.hpi, script-security-1.40.hpi, structs-1.10.hpi</sub>

SonarRunner

```groovy
tools {
    sonar {
        runner =
        [
                [name: "sonar", label: "master", url: "http://example.com/sonar.zip", subdir: "bin"],
                [name: "sonar14", label: "slave", url: "http://example.com/sonar14.zip", subdir: "bin"]
        ]
    }
}
```

<sub>Tested Plugin Versions: sonar-2.6.1.hpi, maven-plugin-2.7.1.hpi, mailer-1.20.hpi, javadoc-1.1.hpi, junit-1.23.hpi, jquery-1.11.2-0.hpi, display-url-api-2.2.0.hpi, workflow-step-api-2.14.hpi, workflow-api-2.26.hpi, script-security-1.40.hpi, structs-1.10.hpi</sub>

Splunk

```groovy
splunk{
    splunkHostUrl  = 'http://splunk.server.url'
    splunkHostPort = 8088
    token = '666'
    useSSL = true
    jenkinsMasterHostname = 'jenkins.savings.com'
    rawEventsEnabled = true
    eventSource = 'jenkins'
    splunkJenkinsAppUrl = 'http://splunk.savings.com/en-GB/app/splunk_app_jenkins/overview/'
    maxEventsBatchSize = 666666
    retriesOnError = 6
    ignoredJobNamesPattern = 'ignore'
  //  scriptPath = ''
    scriptContent = 'splunkins.sendTestReport(50)\\nsplunkins.sendCoverageReport(50)'
    customMetaData = [
            [dataSource: "BUILD_EVENT", configItem: "index", value: "poc123"]
            // dataSource: BUILD_EVENT | BUILD_REPORT | CONSOLE_LOG | JENKINS_CONFIG | FILE | QUEUE_INFO | SLAVE_INFO | default
            // configItem: index | sourcetype | disabled
    ]
}
```

<sub>Tested Plugin Versions: splunk-devops-1.6.0.hpi, script-security-1.40.hpi</sub>

Ssh

```groovy
tools {
    ssh =
            [name             : 'dev-p1-app-01',
             hostname         : 'dev-p1-app-01',
             username         : 'user.name',
             encryptedPassword: 'ENC(Y/a5t0YI5fmWqx1NEEYoKQ==)',
             remoteRootDir    : '/home/user.name',
             port             : 22,
             timeout          : 300000,
             overrideKey      : false,
             disableExec      : false,
             keyPath          : '',
             key              : """-----BEGIN RSA PRIVATE KEY-----
                MIIEpAIBAAKCAQEA3XHBgsU3+Ngkb0EupoR9UKoyZHlHW585rSyt/f0uw/8QA2I1
                HChRnDms1ws3IkRpK4r4E9ID+0vptKWv/Tj0XS2J/Ikb0NV7VKEKd7+yS4BgngTV
                Sa1PbZACYOXyJvfb0lpfNhuJYqOfA5Nl8FDn5VsNsJhLLm6MxRzG96z+UY0oVTTP
                +QVZqF8VxDkM36S9CuWa+hSdN9xolDiUEFx7GLX8KiRlHNByjVBPwbH8UQYWvlDW
                8N1mpOJ41Ps02y5LPO2qmdjmxMjE6GqswPcDit8z+7wZcMGq9ls9ZR7qK9ZYt4Kv
                d+jeAxvNVru+Yv/DkoGeFVVd7xu+kF48XpwGYQIBIwKCAQEAynaiS6z/+XUooDuY
                Xbr2Ox9EBBccq4pDXICfFBquag3Ff2+7h3VR7euWtfugH1R9aaOg/C26f1tZVLS+
                KVik53oXwlF4hGtpcfJStqCjAzOLiSj9hTC9pgACLLT6p0kDfn6C76Ql1ob3yMFH
                Qh4V2QLKsBY2G9LJ2MnafAvhNJycg0zHcep2GriMmksFp0TI5kw54LtcN4+yLqPB
                gw1ESIBhLXft+dw3Jk2+pqlUF9o+2b8FFtW6+1y0C88XKp/QBMEcFRoZoRAvkffI
                OME1uIJpvCUBiMHLiHdX/htDTSTid6SNHHbppNZHJJ9TkbCQhRuVWSFormgl31+2
                xPf7CwKBgQD9qEwBfJ7d1I2s3Cidj8klml/1BJoA+hl4GtxBS50oclEfyFYfTxDE
                w7KNK89v69bl3WFHYDESm9x3p/KGZ07TWDiyVq1hESJvDSfCV+Xw9jQfcN4BmLao
                WAA3IJ/qayCkj2MG1Zdyswb0Fb6dEUma/wo0XSezAx3BxpgvGAHj4wKBgQDffUzU
                V84KppePH05hIOhbtKJGp62vYP3Fllk3HWjP9xhyGBMZhHbRd7i6D5lYaxh8Isah
                /7KPhhicQB4tHUCsX4HBjBb5q3yh05vnav2/0txw5RWeKncMARh+BpEuJFVcdUOu
                Sy9bw8p7cO7d6l0KgeE+XrEgbTwTXXVdXyrn6wKBgQD2aPlgh7BFNOFmF7JtLJeD
                nUdFyfSwfezbEsdVX2V3v4IBngqE0Hawg5eB0smCqpY+Yga6XXF/ypunb/Lo9qRY
                RxKBW4Paou4//iafiJY6iMTrV7MXfmhLwzNowJtYv9aROtU51s2in0iVVvOuhdJ5
                T4ZBfxCfRNsUAr+4uDxZtwKBgQDMVU2O8TFo0tO2Df6EsF9pybjwJEcGvxPn2esr
                E5MHSFDdV9bywkC/g2cQg0pfd9vfNbzr4ndepnzmoQWllxaO+Dweq/e/srsXnOZP
                7Mq+Ap2pC/aB+vCAAQBzOTRHca0hVUUxo9OHGWirX/BV3ZblCQhzicaEDBmrTiIp
                e5TxSwKBgQCxHRG0SpQzc3oVUd3Xilc4UcqBMJ++W9HgWm7h//6DRuHp50t4UWLs
                STytXGk1cFDmfHYaI/2H5nbepwNMmS46KeJyKPWa1klMAnQkFf14A21SJYacHNKt
                i0hqu/BU+HjQGMJ36Ghnt64lQKkXuhdhssfvfoDdhgCsbjx6YDLwSQ==
                -----END RSA PRIVATE KEY-----"""
            ]
}
```

<sub>Tested Plugin Versions: publish-over-ssh-1.14.hpi</sub>

SystemMessage

```groovy
systemMessage {
    message = "<b>Testing System Message</b>"
}
```

<sub>Tested Plugin Versions: NA</sub>

SystemProperties

```groovy
systemProperties = [
    "hudson.model.DirectoryBrowserSupport.CSP":""
]
```

<sub>Tested Plugin Versions: NA</sub>



Note. This file is generated using docs.gradle

```
gradle -b docs.gradle generate
```

Whatever you do, don't go editing the readme.md file directly :-)

