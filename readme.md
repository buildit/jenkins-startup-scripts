
# Jenkins Startup Scripts

[![Build Status](https://travis-ci.org/buildit/jenkins-startup-scripts.svg?branch=master)](https://travis-ci.org/buildit/jenkins-startup-scripts)
[ ![Download](https://api.bintray.com/packages/buildit/maven/jenkins-startup-scripts/images/download.svg) ](https://bintray.com/buildit/maven/jenkins-startup-scripts/_latestVersion)

A collection of groovy scripts that can be used to simplify the configuration of Jenkins.

## Using the Scripts

Though it is possible to run them without it, these scripts have been wrtten to work in conjunction with the [Jenkins Startup Scripts Runner](https://github.com/buildit/jenkins-startup-scripts-runner) project. Please see that project for details on how to confgure and invoke these scripts.

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

<sub>Tested Plugin Versions: anchore-container-scanner-1.0.12.hpi, structs-1.14.hpi</sub>

AquaSec

```groovy
aquasec {
    version = '3.x'
    aquaScannerImage = 'aquasec/scanner-cli:3.2'
    apiUrl = 'https://aquasec-server-url.com'
    user = 'scanner'
    password = 'scanner123'
    //in seconds
    timeout =  0
    runOptions = 'test'
    caCertificates= false
}
```

<sub>Tested Plugin Versions: aqua-security-scanner-3.0.12.hpi, structs-1.14.hpi, jdk-tool-1.1.hpi</sub>

AzureVmAgents

```groovy
/**

  Notes:

  - Supports multiple clouds, but only 1-1-1 mapping between cloud -> template -> image.  The plugin
    supports multiple templates per cloud however, and this could be implemented later.  The plugin
    only supports a 1-1 mapping between template -> image
  - Retention strategy not currently configurable.  It will use the default IDLE strategy
    with a rentention time of 60 minutes.
  - Commented-out values indicate mutually exclusive settings (e.g. new or existing resource group, etc)
  - This example config also requires credentials like so:

  credentials {
    my-admin-credential-id=['username':'admin', 'password':'ENC(AAAADKGPigC2vDGp7Btx8Z+KyEmJUp8DobiaJ9QoaoxS0nWk7feTvo0O)', 'description':'SSH credentials']
    azure-sp=['id': 'azure-sp', 'description': 'azure SP', 'subscriptionId':'ENC(AAAADKJo6t6KaVbHUf97+bW1VLTlzOdsWS+HRuAd/gCxOmWX/V7pffZx+dAO+6E41g==)', 'clientId': 'ENC(AAAADJEPBS43p1UhJ7tlU4uOXcBKlGTMOKfGuSmprEiK/wxt0t7xzbfPLJBKWkU/hA==)', 'clientSecret': 'ENC(AAAADLBZMqQnYTb7wQmW4ghcuB5KF0ZYzte/T0atslRqNHcsMYtFbv52Lv4OQQ==)', 'tenantId': 'ENC(AAAADCdXyebPUb7kYsKy4lvPy6QSQf3OnkzPnPlUZVSZkdU8c0WPBs5kO1AhpawrSg==)', 'azureEnvironment': "Azure", 'type': 'AzureServicePrincipal']
  }

*/


clouds {
    azure {
        builtInImageCloud {
            [
                cloudName = "ubuntu-builtin-image-cloud-name",
                credentialsId = "azure-sp",
                maxVmLimit = "2",
                deploymentTimeout = "1200",
                // existingResourceGroupName = "existing-resource-group",
                newResourceGroupName = "new-resource-group",
                template = [
                    name: "ubuntu-template-name",
                    description: "ubuntu built-in image",
                    agentWorkspace: "/opt/jenkins/workspace",
                    labels: "ubuntu-label",
                    location: "UK West",
                    vmSize: "Standard_DS2_v2",
                    storageAccountType: "Standard_LRS",
                    // existingStorageAccountName: "existing-storage-account",
                    newStorageAccountName: "new-storage-account",
                    diskType: "managed",
                    shutdownOnIdle: false,
                    usageMode: "Use this node as much as possible",
                    vmAdminCredentialsId: "my-admin-credential-id",
                    builtInImage: [
                        imageName: "Ubuntu 16.04 LTS",
                        installGit: true,
                        installMaven: false,
                        installDocker: true
                    ]
                ]
            ]
        }
        advancedImageCloud {
            [
                cloudName = "ubuntu-advanced-image-cloud-name",
                credentialsId = "azure-sp",
                maxVmLimit = "10",
                deploymentTimeout = "600",
                existingResourceGroupName = "existing-resource-group",
                //newResourceGroupName = "new-resource-group",
                template = [
                    name: "advanced-template-name",
                    description: "advanced image",
                    agentWorkspace: "/var/jenkins/workspace",
                    labels: "advanced-label",
                    location: "UK South",
                    vmSize: "Standard_D4s_v3",
                    storageAccountType: "Standard_LRS",
                    existingStorageAccountName: "existing-storage-account",
                    //newStorageAccountName: "new-storage-account",
                    diskType: "managed",
                    shutdownOnIdle: true,
                    usageMode: "Only build jobs with label expressions matching this node",
                    vmAdminCredentialsId: "my-admin-credential-id",
                    advancedImage: [
                        customManagedImageId: "/subscriptions/UUID/resourceGroups/resource-group-name/providers/Microsoft.Compute/images/jenkins-agent-20180712181349",
                        osType: 'Linux',
                        launchMethod: 'SSH',
                        initScript: 'apt-get update && apt-get upgrade -y',
                        runScriptAsRoot: true,
                        doNotUseMachineIfInitFails: true,
                        virtualNetworkName: 'my-vnet',
                        virtualNetworkResourceGroupName: 'my-vnet-rg',
                        subnetName: 'my-subnet',
                        usePrivateIP: true,
                        numberOfExecutors: '4',
                        jvmOptions: '-Xms4G -Xmx4G',
                        preInstallSsh: false
                    ]
                ]
            ]
        }
    }
}
```

<sub>Tested Plugin Versions: azure-vm-agents-0.7.1.hpi, credentials-2.1.16.hpi, plain-credentials-1.4.hpi, azure-commons-0.2.6.hpi, cloud-stats-0.18.hpi, azure-credentials-1.6.0.hpi, workflow-step-api-2.14.hpi, ssh-credentials-1.13.hpi, structs-1.14.hpi, credentials-binding-1.16.hpi</sub>

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

<sub>Tested Plugin Versions: config-file-provider-2.15.7.hpi, credentials-2.1.16.hpi, ssh-credentials-1.13.hpi, structs-1.14.hpi, token-macro-2.3.hpi, workflow-job-2.21.hpi, workflow-step-api-2.14.hpi, workflow-api-2.27.hpi, workflow-support-2.18.hpi, scm-api-2.2.6.hpi, script-security-1.44.hpi</sub>

Confluence

```groovy
tools {
  confluence = [
                  [
                    url: 'http://confluence.sandbox.extranet.group/',
                    username: 'arvind.kumar',
                    password: 'ENC(AAAADMlEpxykoqaB2T3ilF48y9tU0RuTh08QXuYdA9fy359sMJc506xz)'
                  ]
              ]
}
```

<sub>Tested Plugin Versions: confluence-publisher-1.8.hpi</sub>

Credentials

```groovy
credentials {
    repository=['username':'repository', 'password':'ENC(AAAADKGPigC2vDGp7Btx8Z+KyEmJUp8DobiaJ9QoaoxS0nWk7feTvo0O)', 'description':'repository credentials']
    cloud=['username':'cloud', 'password':'ENC(AAAADBG5Hw+6UzWIbJQogBA7PjSUDZAF4TGqfwzkuL/CbbTn8w==)', 'description':'cloud credentials']
    somewhereCool=[id: 'somewhere-cool', 'username':'somewhere', 'password':'ENC(AAAADKGPigC2vDGp7Btx8Z+KyEmJUp8DobiaJ9QoaoxS0nWk7feTvo0O)', 'description':'somewhere-cool credentials']
    ssh=['username':'ssh', 'password':'ENC(AAAADDp9istmVU5kLc8CDFArqEAWel5iQmveVw/ro3bgwY1QLQ==)', 'description':'node credentials', 'type': 'SSH', 'privateKeyFile':'.ssh/id_rsa']
    vault=['description': 'vault credentials', 'key':'super/secret', 'usernameKey': "username", 'passwordKey': "password", 'type': 'HashicorpVault']
    saucelabs=['description': 'SauceLabs credentials', 'username': 'slUser', 'apiKey': 'slApiKey', 'type': 'SauceLabs']
    gitlab=['description': 'Gitlab credentials', 'token': 'ENC(AAAADKDFECe0WbaVge4XD0Nia2fnfWhZ5Ee9CLfhixui/3ak128pV4fw)', 'type': 'GitLabApiToken']
    string=['description': 'auth token', 'token': 'ENC(AAAADMoiy5CDV7xwFgER+pLGnPNRsYKtWstLzDgJYOiBn72y7KLu6/Di)', 'type': 'StringCredential']
    azureServicePrincipal=['id': 'azure-sp-id', 'description': 'azure SP', 'subscriptionId':'ENC(AAAADKJo6t6KaVbHUf97+bW1VLTlzOdsWS+HRuAd/gCxOmWX/V7pffZx+dAO+6E41g==)', 'clientId': 'ENC(AAAADJEPBS43p1UhJ7tlU4uOXcBKlGTMOKfGuSmprEiK/wxt0t7xzbfPLJBKWkU/hA==)', 'clientSecret': 'ENC(AAAADLBZMqQnYTb7wQmW4ghcuB5KF0ZYzte/T0atslRqNHcsMYtFbv52Lv4OQQ==)', 'tenantId': 'ENC(AAAADCdXyebPUb7kYsKy4lvPy6QSQf3OnkzPnPlUZVSZkdU8c0WPBs5kO1AhpawrSg==)', 'azureEnvironment': "Azure", 'type': 'AzureServicePrincipal']
    azureSecretString=['id': 'secret-id', 'description': 'secret-description', 'servicePrincipalId': 'azure-sp-id', 'secretIdentifier': 'ENC(AAAADDKoWcVCrE1bCld3s48MECqA8nQITPw6Go3kP+nVWqx5gb07iy4LwD4X+jhW)', 'type': 'SecretStringCredentials']
    azureStorageAccount=['id':'az-storage-creds-id', 'description':'az storage credentials', 'storageAccountName': 'testsa', 'storageKey':'ENC(AAAADMcRT6s4SNoNvUDCmfpiO9CI7TMpUrBIAoxs/u9cxh18xO61CECSIwfc5g==)', 'endpointUrl':'https://testsa.blob.core.windows.net', 'type':'AzureStorageAccount']
}
```

<sub>Tested Plugin Versions: azure-credentials-1.6.0.hpi, hashicorp-vault-plugin-2.1.0.hpi, hashicorp-vault-credentials-plugin-0.0.9.hpi, credentials-binding-1.16.hpi, credentials-2.1.16.hpi, workflow-api-2.27.hpi, workflow-step-api-2.14.hpi, structs-1.14.hpi, plain-credentials-1.4.hpi, ssh-credentials-1.13.hpi, sauce-ondemand-1.164.hpi, maven-plugin-3.1.2.hpi, matrix-project-1.12.hpi, workflow-basic-steps-2.5.hpi, run-condition-1.0.hpi, workflow-cps-2.53.hpi, junit-1.23.hpi, workflow-job-2.21.hpi, script-security-1.44.hpi, javadoc-1.1.hpi, token-macro-2.3.hpi, workflow-scm-step-2.6.hpi, workflow-support-2.18.hpi, ace-editor-1.0.1.hpi, jquery-detached-1.2.1.hpi, scm-api-2.2.6.hpi, gitlab-plugin-1.4.8.hpi, git-3.7.0.hpi, git-client-2.7.0.hpi, cloudbees-folder-6.4.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, display-url-api-2.2.0.hpi, mailer-1.20.hpi, windows-azure-storage-0.3.9.hpi, azure-commons-0.2.6.hpi, copyartifact-1.40.hpi, blueocean-rest-1.5.0.hpi, blueocean-commons-1.5.0.hpi</sub>

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

Files

```groovy
files {
    vaultUrl='http://localhost:51234' // default vaultUrl for any values with source 'HashicorpVault'
    vaultToken='f119a093-c4b1-4422-1c8c-47688d14fe9e' // default vaultToken for any values with source 'HashicorpVault'
    privateKey=[
            path: '${jenkinsHome}/.ssh/id_rsa',
            mode:'600',

            // This private key is not in the project.  If you need to re-encrypt, you need to generate a new key
            contents:'''ENC(AAAADA97iygXfher9FBaFsAhlyl2SczDJi6DH5B9P4aMF8M77u/7tk0MKw3jdNm6ud9WbmjQhxdLfGDGRYCHA4YS+VXLTICMDB+3gPcMiCun1svE1VAlq5bVpVu1Tn8YIL6tUV0lX7VAB9WzarwCPJdErEPirERWl9x9tQKJ+kBRSY2MZtRk+EU3eMUdKSRpCJ/6ldcD3nt1XOgjDAPbWStA5Gq+7fvfTAoYobss/pXdTU8GdGf0DLGBfuGXzzyrciSjkGrBpTyK9Suv77in6Bc4Vzgfa9NG2UM855p+X5FxjouK7Hpkl35oG6mwOFiB1BsgFtF6DRYyP5+IorFMC4aZtCZJDeX2Li/bJsxb52jvSWKsYVSEPO4SRGl/xvlTIw7pApmE5Mtcm2Xs0AwpgSE2JUoBf9+GUYjdT6kqHtYA9nURD8JSOFN/vCLLLg5rdcgoRQwfPtpUEbz8IQZH0F3YhgsmtYfPjtZ+/IcSZEXV5cxbyzRGgWwj3aSFbnSAXNnD1DMl2YJGQrJRkN5AckhqeusX/QxZd8PdYZx1YV86ymDC+Vtdy7kV3ktjs/MqnwGu1CaGOLupCukA0hurQrf11IEG5tYH2MgwgUij1bTocIXfwu4tHR/u0howxiilXuxtXh7ajbDurFhi5jjLpAM9X0iNfX06mXkqF5nCHT5uYo+yg2EhCuhIUkHDpg6i2Wh/bP7ebaU2v9LEmAML7iIlB9NAVRRXDQZGezd3W8U9S5pPf5p9O3uU39hTcC9QDOyE0nxFxg+EZT3ya3MS3rpSZ6KHW6LxJTRbNXEVgI2EHBJqPL1GPgqEklT2XsYE+n63N/t+UvPLEIcapGHBH1LG2ZDHz9dI9QO/GtGamGh+6dj4QIfz4ecrIvxkQagE9FxHxaQwpIFofGsdVQ5FJrzv2AnCxnGg9RaVOYFn0ovXyxZLNKy11Y6WEmHn95fB+Nb7jNeIdndHOAxo7ymlNvktPK5wBwpXNxsoZLPuL89QqtMa/cJMdkJPUcLJxmiRMykNN3CMHH6yi1cVzcoGj1tw2Yn7q3H25KvkNS9M79x5H9qf9aRWHMnsE5P9NwUIFr+rZsGN1xBrkBtpwkf1bD5fDM5R8HmmnOFsc+qgc7Rj/HFHIMMF3MoVhM1mJYa1F+eP7ZiU1QNSDcWFjO/AEHsefQm0sazkhBbm/yyCdnBsMobbOLoJEOIAxvhbCP8zPFYFPHplFqOBb684dUC42CykEJyX4ozkyS2+Z77a61pxxn/rUR3rxQpx0tyA2Pdqdj41dlVfnKVthUF9x4pZ+us/jZjw7LPi6hDozEHGela9gRsuooQO3rfgxNSFkf7dYSzVrpY7RMZ/StwwXMxpVe7Vu1tdTa1dcWRC60UzPXgcib83OV8AFDrRmk/7EFXzsXUrCvhtldGWzFlTnocgI/kW0QdxZ+T2YkzJ3VizWPTNJmdBGBiezCZBvC7LfUpBcMz+w+8hlgvk1/gceSHJnPbr5jZHxkpGdzC3UHPNEhFSgOSGhu/hwOxUYDe5orJtMS5ggXr1Ai0QKZcQoYvaXXVm/wtJxWLXTDHymPfXmMaoJJz1gGEnCsMinfMKxe+ntqMRC2R4jmXk56M5rtKtqzsSAUF48RtOiW/2gWOyVyoqeQxjyGS5mt4dxM4Cwt8dRg94Lp4rqxVIHBRWuszxVIyIslBFmBP+1sdKzB2eXNMmjiVLPOHgBBm13ZjUHFw9+h8TJH95FafnOK5A4g4/Sa9bHNwJO+vDDSks6V5G61ocAtswKITUtoFvK+HlRGgtbNky/ab4ofpFnpllVd+lvFEq60gUc5FWt0qWNBpqA0XC2qLxQw+GVKRO9J3GpUh0QD+zJRG6+f5+duggXMdAQwV3RhNzej91sYRDDiybwFsrye257ilxH+Ie+6MB43itOrZ5r/uc8epQmMYjAaUH9l92CJNzRmM2+TYV/Gom5zNB0Hso10jrcCiGMaAun5VmM4SaJlTaxsyDLJaJ25u6zNKiP0PSBv9z6aiSyWCWTV4KMP0kMSSsxs0dneX4Q3GHEGu5prLrY8cbm/KBmvt9Rkmk72L30hK/cmmJXOeoTNZF9BZO3Fyw7xwJCbzgtFdM05B3fg9vVYlTwUXnagagQUiyQozWqBKbhRiVzG451pcQMELWghIx/kM4aKYPmaLeVJuWaUATP/KyXC0Ab0Mr3GgjOGTYlRRITQhoH0MrYOtjB1PGmC9kmJ5Ijyx6rDqOeUO1vrOuGVGd0P2yoP5LCQNwHoS4U9ekYbhI)'''
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

<sub>Tested Plugin Versions: gerrit-trigger-2.27.5.hpi, structs-1.14.hpi</sub>

GitHub

```groovy
credentials {
    github=['username': 'jenkins', 'password': 'ENC(AAAADBCsrisG4HKN89K4oEKwHbHlA2wKZqR1n275eeadIXrVUTctvseX/jb9V6qv2w==)', 'description': 'GitHub Credentials']
    mycompany=['username': 'jenkins', 'password': 'ENC(AAAADBCsrisG4HKN89K4oEKwHbHlA2wKZqR1n275eeadIXrVUTctvseX/jb9V6qv2w==)', 'description': 'MyCompany GitHub Credentials']
    acme=['username': 'jenkins', 'password': 'ENC(AAAADBCsrisG4HKN89K4oEKwHbHlA2wKZqR1n275eeadIXrVUTctvseX/jb9V6qv2w==)', 'description': 'Acme GitHub Credentials']
}

github {
    githubEnterpriseEndpoints=[
        ['name': 'MyCompany','url': 'https://api.github.mycompany.com'],
        ['name': 'Acme','url': 'https://api.github.acme.com']
    ]
    organisations=[
            [
                    name:"mycompany",
                    displayName:"MyCompany",
                    description:"MyCompany Github Enterprise Organisation",
                    project:[
                            apiEndpoint:"MyCompany",
                            owner:"mycompany",
                            credentialsId:"mycompany",
                            repositoryNamePattern:"*"
                    ],

                    // explicit script path.  If left out, 'Jenkinsfile' will be the default
                    jenkinsfiles: ['Jenkinsfile.prod', 'Jenkinsfile.test']
            ],
            [
                    name:"acme",
                    displayName:"Acme",
                    description:"Acme Github Enterprise Organisation",
                    project:[
                            apiEndpoint:"Acme",
                            owner:"acme",
                            credentialsId:"acme",
                            repositoryNamePattern:"*"
                    ],

                    // explicit script path.  If left out, 'Jenkinsfile' will be the default
                    jenkinsfiles: ['Jenkinsfile.acme']
            ],
            [
                    // Github.com
                    name:"buildit-name",
                    displayName:"Buildit Display Name",
                    description:"Buildit Github Organisation Description",
                    project:[
                            apiEndpoint:"GitHub",  // default endpoint present in Jenkins for github.com
                            owner:"buildit-owner",
                            credentialsId:"github",
                            repositoryNamePattern:"*"
                    ]
             ],
             [
                                 // Github.com
                                 name:"buildit-specific-branches",
                                 displayName:"Buildit Specific Branches Display Name",
                                 description:"Buildit Github Organisation Description",
                                 branchesToBuildAutomatically: " ", // Don't build any branches automatically, empty string isn't enough
                                 project:[
                                         owner:"buildit-owner",
                                         credentialsId:"github",
                                         repositoryNamePattern:"*",
                                         branchesToInclude:"master"
                                 ]
                          ]
    ]
}
```

<sub>Tested Plugin Versions: github-branch-source-2.3.4.hpi, github-1.28.1.hpi, credentials-2.1.16.hpi, display-url-api-2.2.0.hpi, git-3.7.0.hpi, github-api-1.90.hpi, scm-api-2.2.6.hpi, structs-1.14.hpi, git-client-2.7.0.hpi, mailer-1.20.hpi, matrix-project-1.12.hpi, ssh-credentials-1.13.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, junit-1.23.hpi, script-security-1.44.hpi, workflow-api-2.27.hpi, workflow-step-api-2.14.hpi, workflow-scm-step-2.6.hpi, jackson2-api-2.8.10.1.hpi, plain-credentials-1.4.hpi, token-macro-2.3.hpi, workflow-job-2.21.hpi, workflow-support-2.18.hpi, branch-api-2.0.19.hpi, cloudbees-folder-6.4.hpi, workflow-multibranch-2.19.hpi, workflow-cps-2.53.hpi, ace-editor-1.0.1.hpi, jquery-detached-1.2.1.hpi</sub>

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
    gitlabCredentials = ['description': 'Gitlab credentials', 'token': 'ENC(AAAADBCsrisG4HKN89K4oEKwHbHlA2wKZqR1n275eeadIXrVUTctvseX/jb9V6qv2w==)', 'type': 'GitLabApiToken']
}
```

<sub>Tested Plugin Versions: gitlab-plugin-1.4.8.hpi, credentials-2.1.16.hpi, plain-credentials-1.4.hpi, git-3.7.0.hpi, git-client-2.7.0.hpi, matrix-project-1.12.hpi, workflow-step-api-2.14.hpi, ssh-credentials-1.13.hpi, junit-1.23.hpi, script-security-1.44.hpi, mailer-1.20.hpi, scm-api-2.2.6.hpi, workflow-scm-step-2.6.hpi, structs-1.14.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, display-url-api-2.2.0.hpi, workflow-api-2.27.hpi</sub>

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

<sub>Tested Plugin Versions: hashicorp-vault-plugin-2.1.0.hpi, workflow-scm-step-2.6.hpi, cloudbees-folder-6.4.hpi, credentials-2.1.16.hpi, workflow-step-api-2.14.hpi, structs-1.14.hpi, ssh-credentials-1.13.hpi</sub>

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

<sub>Tested Plugin Versions: hipchat-2.1.1.hpi, workflow-step-api-2.14.hpi, credentials-2.1.16.hpi, display-url-api-2.2.0.hpi, junit-1.23.hpi, matrix-project-1.12.hpi, plain-credentials-1.4.hpi, token-macro-2.3.hpi, structs-1.14.hpi, script-security-1.44.hpi, ssh-credentials-1.13.hpi, workflow-job-2.21.hpi, workflow-api-2.27.hpi, scm-api-2.2.6.hpi, workflow-support-2.18.hpi</sub>

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

<sub>Tested Plugin Versions: jira-2.2.1.hpi, matrix-project-1.12.hpi, mailer-1.20.hpi, junit-1.23.hpi, script-security-1.44.hpi, structs-1.14.hpi, workflow-step-api-2.14.hpi, workflow-api-2.27.hpi, junit-1.23.hpi, scm-api-2.2.6.hpi, display-url-api-2.2.0.hpi, mailer-1.20.hpi</sub>

JobDSL

```groovy
jobdsl {
    scriptSecurityEnabled = false // scriptSecurityEnabled defaults to false
    jobdsl=[url:"http://localhost:6666", targets:"jobs/**/*.groovy", branch:"*/master", additionalClasspath:"src/main/groovy"]
    jobdslWithLabel=[url:"http://localhost:6666", targets:"jobs/**/*.groovy", branch:"*/master", label:"foo"]
    jobdslWithCredentials=[url:"http://localhost:6666", targets:"jobs/**/*.groovy", branch:"*/master", credentialsId:"git"]
}

credentials {
    git=['username':'test', 'password':'p@ssword', 'description':'git credentials']
}
```

<sub>Tested Plugin Versions: ace-editor-1.0.1.hpi, jquery-detached-1.2.1.hpi, workflow-cps-global-lib-2.9.hpi, workflow-cps-2.53.hpi, workflow-scm-step-2.6.hpi, cloudbees-folder-6.4.hpi, git-client-2.7.0.hpi, git-server-1.7.hpi, scm-api-2.2.6.hpi, structs-1.14.hpi, ssh-credentials-1.13.hpi, credentials-2.1.16.hpi, workflow-step-api-2.14.hpi, workflow-api-2.27.hpi, workflow-support-2.18.hpi, ace-editor-1.0.1.hpi, script-security-1.44.hpi, git-3.7.0.hpi, matrix-project-1.12.hpi, mailer-1.20.hpi, junit-1.23.hpi, job-dsl-1.70.hpi, credentials-binding-1.16.hpi, credentials-2.1.16.hpi, plain-credentials-1.4.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, display-url-api-2.2.0.hpi, git-client-2.7.0.hpi</sub>

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

<sub>Tested Plugin Versions: kubernetes-1.5.2.hpi, workflow-step-api-2.14.hpi, credentials-2.1.16.hpi, durable-task-1.16.hpi, variant-1.1.hpi, structs-1.14.hpi, kubernetes-credentials-0.3.0.hpi, plain-credentials-1.4.hpi</sub>

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

<sub>Tested Plugin Versions: ldap-1.15.hpi, mailer-1.20.hpi, display-url-api-2.2.0.hpi, matrix-auth-2.3.hpi, icon-shim-2.0.3.hpi</sub>

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

<sub>Tested Plugin Versions: ldap-1.15.hpi, mailer-1.20.hpi, display-url-api-2.2.0.hpi, icon-shim-2.0.3.hpi, matrix-auth-2.3.hpi</sub>

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

<sub>Tested Plugin Versions: ace-editor-1.0.1.hpi, jquery-detached-1.2.1.hpi, workflow-cps-global-lib-2.9.hpi, workflow-cps-2.53.hpi, workflow-scm-step-2.6.hpi, cloudbees-folder-6.4.hpi, git-client-2.7.0.hpi, git-server-1.7.hpi, scm-api-2.2.6.hpi, structs-1.14.hpi, ssh-credentials-1.13.hpi, credentials-2.1.16.hpi, workflow-step-api-2.14.hpi, workflow-api-2.27.hpi, workflow-support-2.18.hpi, ace-editor-1.0.1.hpi, script-security-1.44.hpi, git-3.7.0.hpi, matrix-project-1.12.hpi, mailer-1.20.hpi, junit-1.23.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, display-url-api-2.2.0.hpi, mailer-1.20.hpi</sub>

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

<sub>Tested Plugin Versions: logstash-1.3.0.hpi, mask-passwords-2.10.1.hpi, structs-1.14.hpi, junit-1.23.hpi, script-security-1.44.hpi, workflow-step-api-2.14.hpi, workflow-api-2.27.hpi, scm-api-2.2.6.hpi</sub>

MailExt

```groovy
mail {
  host = "159.34.192.34"
  defaultSuffix = "@somewhere.com"
}
```

<sub>Tested Plugin Versions: email-ext-2.58.hpi, mailer-1.20.hpi, matrix-project-1.12.hpi, script-security-1.44.hpi, token-macro-2.3.hpi, junit-1.23.hpi, script-security-1.44.hpi, workflow-step-api-2.14.hpi, workflow-api-2.27.hpi, scm-api-2.2.6.hpi, structs-1.14.hpi, display-url-api-2.2.0.hpi, workflow-job-2.21.hpi, workflow-support-2.18.hpi</sub>

Mail

```groovy
mail {
  host = "159.34.192.34"
  defaultSuffix = "@somewhere.com"
}
```

<sub>Tested Plugin Versions: mailer-1.20.hpi, display-url-api-2.2.0.hpi</sub>

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

<sub>Tested Plugin Versions: mesos-0.14.1.hpi, credentials-2.1.16.hpi, metrics-3.1.2.10.hpi, jackson2-api-2.8.10.1.hpi, structs-1.14.hpi</sub>

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

<sub>Tested Plugin Versions: mesos-0.14.1.hpi, credentials-2.1.16.hpi, metrics-3.1.2.10.hpi, jackson2-api-2.8.10.1.hpi, structs-1.14.hpi, kubernetes-1.5.2.hpi, workflow-step-api-2.14.hpi, credentials-2.1.16.hpi, durable-task-1.16.hpi, variant-1.1.hpi, kubernetes-credentials-0.3.0.hpi, plain-credentials-1.4.hpi</sub>

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
    master {
        labels = ['label1', 'label2']
        executors = 3
        mode = 'EXCLUSIVE'
    }
    slaves {
        node01 = ['name': 'node 01', 'description': 'First node', remoteFS: '/tmp', numExecutors: '1', launchType: 'JNLP', jnlpTunnel: ':443', jnlpVmArgs: '-', env: ['android_home': '/android', 'java_home': '/java']]
        node02 = ['name': 'node 02', 'description': 'Second node', remoteFS: '/var', mode: 'EXCLUSIVE']
        node03 = ['name': 'node 03', 'description': 'Third node', remoteFS: '/var', mode: 'EXCLUSIVE', launchType: 'SSH', host: 'sshHost', port: 1234, credentialsId: 'sshCredentials', javaPath: 'sshJavaPath']
        node04 = ['name': 'node 04', 'description': 'Fourth node', remoteFS: '/var', numExecutors: '15', launchType: 'SSH', host: 'sshHost', credentialsId: 'sshCredentials']
    }
}
```

<sub>Tested Plugin Versions: ssh-slaves-1.26.hpi, credentials-2.1.16.hpi, structs-1.14.hpi, ssh-credentials-1.13.hpi, jdk-tool-1.1.hpi</sub>

Passwords

```groovy
env {
    passwords {
        repository='ENC(AAAADF6IulRzxGoLgc3QerNDHPRdARvB3eJ3y1nLPk2PhxNt4VdE6ibH)'
    }
}
```

<sub>Tested Plugin Versions: envinject-1.91.3.hpi, maven-plugin-3.1.2.hpi, mailer-1.20.hpi, javadoc-1.1.hpi, junit-1.23.hpi, display-url-api-2.2.0.hpi, workflow-step-api-2.14.hpi, script-security-1.44.hpi, structs-1.14.hpi, workflow-api-2.27.hpi, scm-api-2.2.6.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, ssh-credentials-1.13.hpi, credentials-2.1.16.hpi</sub>

Plugins

```groovy
plugins=[
    // default artifactPlugin = "http://updates.jenkins-ci.org/download/plugins/[module]/[revision]/[module].[ext]"
    first: [
            artifactPattern: 'http://localhost:4567/[module]-[revision].[ext]',
            pluginArtifacts: ['ace-editor:1.0.1', 'maven-plugin:3.1.2']
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

<sub>Tested Plugin Versions: matrix-auth-2.3.hpi, icon-shim-2.0.3.hpi, role-strategy-2.3.2.hpi</sub>

Saml

```groovy
/**
  SAML Plugin Configuration:

    - currently only supports the attributes seen below
    - Does not set authorization strategy - do that in a separate security{} block
*/

saml {
    idpMetadataUrl = 'https://nexus.microsoftonline-p.com/federationmetadata/saml20/federationmetadata.xml'
    refreshPeriod  = '15'
    maximumAuthLifetime = 1209600
    displayNameAttribute = 'http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name'
    groupAttribute = 'http://schemas.microsoft.com/ws/2008/06/identity/claims/groups'
    usernameAttribute = 'http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name'
    emailAttribute = 'email.attribute'
    usernameCaseConversion = 'None'
    dataBindingMethod = 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect'
    logoutUrl = 'https://logout.com'
    idpMetadataXml = '''\
&lt;EntityDescriptor xmlns=&quot;urn:oasis:names:tc:SAML:2.0:metadata&quot; xmlns:alg=&quot;urn:oasis:names:tc:SAML:metadata:algsupport&quot; ID=&quot;_0c0d1ca7-7292-4bc6-801c-f880f6098f4e&quot; entityID=&quot;urn:federation:MicrosoftOnline&quot;&gt;
&lt;Signature xmlns=&quot;http://www.w3.org/2000/09/xmldsig#&quot;&gt;
&lt;SignedInfo&gt;
&lt;CanonicalizationMethod Algorithm=&quot;http://www.w3.org/2001/10/xml-exc-c14n#&quot;/&gt;
&lt;SignatureMethod Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#rsa-sha1&quot;/&gt;
&lt;Reference URI=&quot;#_0c0d1ca7-7292-4bc6-801c-f880f6098f4e&quot;&gt;
&lt;Transforms&gt;
&lt;Transform Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#enveloped-signature&quot;/&gt;
&lt;Transform Algorithm=&quot;http://www.w3.org/2001/10/xml-exc-c14n#&quot;/&gt;
&lt;/Transforms&gt;
&lt;DigestMethod Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#sha1&quot;/&gt;
&lt;DigestValue&gt;7XES82rGE5v/Hdsytuvg9wp8iBE=&lt;/DigestValue&gt;
&lt;/Reference&gt;
&lt;/SignedInfo&gt;
&lt;SignatureValue&gt;
OOSrXuQqx/V/XjkOnEMuSVaGsb5a25e5kT5ntSDn6TwVjQR35l1o918OoEzF0+KNe+MZhfGdzzntFsG96Ifq8lM8hcVzwG6/xH9Ar+jdwSe7cemd3j7d48H1WIIx6M60rzHlkHnegbrXbUwWZLGAGyQu/P2z9Z8lIjzzjbZi0THFx5aZR5goI91N6eLYBP3ms75QDTb3749InVb+Yc//klycFkFfa5kViDqkNJULw8d+S6BGPsfzd+V72ih16YLegHLZAOhv0Te07QiVgFvNbArcJcyUbXZf+VHW4XyG2YYCL1F1cwT5GFDdemq7Fc+TacEPqq5SxsAq7Nu9pM78Sg==
&lt;/SignatureValue&gt;
&lt;KeyInfo&gt;
&lt;X509Data&gt;
&lt;X509Certificate&gt;
MIIDYDCCAkigAwIBAgIJALLJPAyvf2sjMA0GCSqGSIb3DQEBBQUAMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleTAeFw0xNDA3MTgxOTUzNDBaFw0xOTA3MTcxOTUzNDBaMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANYDKgByFZdqtTnnpF4IfIp4i2XLg2rLIo+mu4DmW9gRLlBJCNc7YESUxpKzuFYaANd8fWsDigJZTXbhOQApSpw4xXFnor2vJ1zm94LtqjcVEXTjUml5gAIS4pwuOU3ZfO/0eTG0gDYp4a0L/mzzTRsnwe/8WMPIE75Bq2zAyAZ9aePvl3QX7cXYLPfeK4QTgK3B5lwe1wWu3y5oQidjcSok8Frf80xzuCYuOa+ZUK3JibpLLCrT4uwiqf+KREDSdc4bPPlq0PWI4sQr1tha8yypRSvOH+/MxcfSRSnl6Uc+gm8nVEEWWIu4hhu6NIfG91mMUqJuzkgLCi6Gov6JS8UCAwEAAaOBijCBhzAdBgNVHQ4EFgQUnQoq7sI3R8rde4sQs6nGEbJm3LcwWQYDVR0jBFIwUIAUnQoq7sI3R8rde4sQs6nGEbJm3LehLaQrMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleYIJALLJPAyvf2sjMAsGA1UdDwQEAwIBxjANBgkqhkiG9w0BAQUFAAOCAQEAf4jaNhKzRG3k+52WoM9nnISP7rlWIeWwH6EQGUlF6ozSP/03gYMAdqpdhww5zNwKzi7TQVbDC0pgq/tqzHv6JEI0R4B6h7/TJ1pYPxdvIFQrE27RHESltH/m+5UkVnayLqRD3/fi4zf4aEpxSDZ73MCR5LanPGqvlAMz29AL3g1ynj+eu7xMfFsM/8+qJaCXuxT5/30eeLEe+PYikA/PhEwp+qkDQWPvdAwEghuUaFvtKAgDZierjpGzHZnYkXTTDTHVe1iP7tsAJH5qK3qdcv3UGPyZrjC/lietJcAcnwVoZQ93v2ieGfcKKN+PFN9M59/BkPo62HPoGNNx2ZDQaQ==
&lt;/X509Certificate&gt;
&lt;/X509Data&gt;
&lt;/KeyInfo&gt;
&lt;/Signature&gt;
&lt;Extensions&gt;
&lt;alg:DigestMethod Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#sha1&quot;/&gt;
&lt;alg:SigningMethod Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#rsa-sha1&quot;/&gt;
&lt;/Extensions&gt;
&lt;SPSSODescriptor protocolSupportEnumeration=&quot;urn:oasis:names:tc:SAML:2.0:protocol&quot; WantAssertionsSigned=&quot;true&quot;&gt;
&lt;KeyDescriptor use=&quot;signing&quot;&gt;
&lt;ds:KeyInfo xmlns:ds=&quot;http://www.w3.org/2000/09/xmldsig#&quot;&gt;
&lt;ds:X509Data&gt;
&lt;ds:X509Certificate&gt;
MIIDYDCCAkigAwIBAgIJALLJPAyvf2sjMA0GCSqGSIb3DQEBBQUAMCkxJzAlBgNV BAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleTAeFw0xNDA3MTgxOTUz NDBaFw0xOTA3MTcxOTUzNDBaMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25p bmcgUHVibGljIEtleTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANYD KgByFZdqtTnnpF4IfIp4i2XLg2rLIo+mu4DmW9gRLlBJCNc7YESUxpKzuFYaANd8 fWsDigJZTXbhOQApSpw4xXFnor2vJ1zm94LtqjcVEXTjUml5gAIS4pwuOU3ZfO/0 eTG0gDYp4a0L/mzzTRsnwe/8WMPIE75Bq2zAyAZ9aePvl3QX7cXYLPfeK4QTgK3B 5lwe1wWu3y5oQidjcSok8Frf80xzuCYuOa+ZUK3JibpLLCrT4uwiqf+KREDSdc4b PPlq0PWI4sQr1tha8yypRSvOH+/MxcfSRSnl6Uc+gm8nVEEWWIu4hhu6NIfG91mM UqJuzkgLCi6Gov6JS8UCAwEAAaOBijCBhzAdBgNVHQ4EFgQUnQoq7sI3R8rde4sQ s6nGEbJm3LcwWQYDVR0jBFIwUIAUnQoq7sI3R8rde4sQs6nGEbJm3LehLaQrMCkx JzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleYIJALLJPAyv f2sjMAsGA1UdDwQEAwIBxjANBgkqhkiG9w0BAQUFAAOCAQEAf4jaNhKzRG3k+52W oM9nnISP7rlWIeWwH6EQGUlF6ozSP/03gYMAdqpdhww5zNwKzi7TQVbDC0pgq/tq zHv6JEI0R4B6h7/TJ1pYPxdvIFQrE27RHESltH/m+5UkVnayLqRD3/fi4zf4aEpx SDZ73MCR5LanPGqvlAMz29AL3g1ynj+eu7xMfFsM/8+qJaCXuxT5/30eeLEe+PYi kA/PhEwp+qkDQWPvdAwEghuUaFvtKAgDZierjpGzHZnYkXTTDTHVe1iP7tsAJH5q K3qdcv3UGPyZrjC/lietJcAcnwVoZQ93v2ieGfcKKN+PFN9M59/BkPo62HPoGNNx 2ZDQaQ==
&lt;/ds:X509Certificate&gt;
&lt;/ds:X509Data&gt;
&lt;/ds:KeyInfo&gt;
&lt;/KeyDescriptor&gt;
&lt;KeyDescriptor use=&quot;signing&quot;&gt;
&lt;ds:KeyInfo xmlns:ds=&quot;http://www.w3.org/2000/09/xmldsig#&quot;&gt;
&lt;ds:X509Data&gt;
&lt;ds:X509Certificate&gt;
MIIDYDCCAkigAwIBAgIJAMkTRrgd+CoIMA0GCSqGSIb3DQEBCwUAMCkxJzAlBgNV BAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleTAeFw0xNjEyMDYyMjA2 MjlaFw0yMTEyMDUyMjA2MjlaMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25p bmcgUHVibGljIEtleTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAN4S BAyO92gibTP+qXxrc/+OVG/kznAlMf4VW+jCakTjHYh3m+WzWa0HI0ZmQX1O7roZ MTWGjLQFpSA8BaTnHPxYgdK48wkdVIMeCPDeqvgZfZsuPIP57MjJb/v0X6PlHU6X 2L/SWfNCGVjWwbL+LdwWIPdVN9jhCuh99UHGHZ0Be5sZLqg2sBildMUwqKnj5V+G v9w4ZqSaF5jz7SzSm5d+a9A9CrX70q1ynZSMsgtsLJ1+spscCJD+hfMVTL8hqDUQ BxoPS5AvnPC9iSkUYDsyOzhm1pIpTgb6SrZ59IF/MKsGINM0hHRaRs7gxg+V0Sy4 VA6gVLJlSM7JIKCHG/sCAwEAAaOBijCBhzAdBgNVHQ4EFgQUzSeR6iod/rh1A50s /s8c91qBYtYwWQYDVR0jBFIwUIAUzSeR6iod/rh1A50s/s8c91qBYtahLaQrMCkx JzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleYIJAMkTRrgd +CoIMAsGA1UdDwQEAwIBxjANBgkqhkiG9w0BAQsFAAOCAQEA0xjNKxnEDYiMoiUO qm1i3fjBEW348eTpkRbHmJizHrnnaLEQn+8JOqSmMntDRd9iuk+/GOR4ALJEJ6Lu KvRUK5/cc79nN2Muq0cnGPHu6OhVsBLRG/UnPnrwtuLTwuxso5d9oZ3QckRE+yKl c8fYFhOWe43lGKb8PTlveZcZ7y0eJ5gFOMhM5AuwjWFvR/GiiQ3JTEvqM/MoCHE/ +4+VvE6AYg5+AI/LF0kYFP8f9aazqoBjljDj40+IMkj6LGkdoSKqx2SnT0F9VgPX 9E/ffrIOhyj+jwkA4M7sXaFoVs8cLdLurTVSjMuEvquO3lHbPunOXBPv029oTSEs V4ArsQ==
&lt;/ds:X509Certificate&gt;
&lt;/ds:X509Data&gt;
&lt;/ds:KeyInfo&gt;
&lt;/KeyDescriptor&gt;
&lt;SingleLogoutService Binding=&quot;urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST&quot; Location=&quot;https://login.microsoftonline.com/login.srf&quot;/&gt;
&lt;NameIDFormat&gt;
urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress
&lt;/NameIDFormat&gt;
&lt;NameIDFormat&gt;urn:mace:shibboleth:1.0:nameIdentifier&lt;/NameIDFormat&gt;
&lt;NameIDFormat&gt;
urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified
&lt;/NameIDFormat&gt;
&lt;NameIDFormat&gt;
urn:oasis:names:tc:SAML:2.0:nameid-format:transient
&lt;/NameIDFormat&gt;
&lt;NameIDFormat&gt;
urn:oasis:names:tc:SAML:2.0:nameid-format:persistent
&lt;/NameIDFormat&gt;
&lt;AssertionConsumerService isDefault=&quot;true&quot; index=&quot;0&quot; Binding=&quot;urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST&quot; Location=&quot;https://login.microsoftonline.com/login.srf&quot;/&gt;
&lt;AssertionConsumerService index=&quot;1&quot; Binding=&quot;urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign&quot; Location=&quot;https://login.microsoftonline.com/login.srf&quot;/&gt;
 &lt;!--
 PAOS functionality is NOT supported by this service. The binding is only included to ease setup and integration with Shibboleth ECP
--&gt;
&lt;AssertionConsumerService index=&quot;2&quot; Binding=&quot;urn:oasis:names:tc:SAML:2.0:bindings:PAOS&quot; Location=&quot;https://login.microsoftonline.com/login.srf&quot;/&gt;
&lt;/SPSSODescriptor&gt;
&lt;/EntityDescriptor&gt;
'''
}
```

<sub>Tested Plugin Versions: matrix-auth-2.3.hpi, saml-1.0.7.hpi, icon-shim-2.0.3.hpi, bouncycastle-api-2.16.1.hpi, mailer-1.20.hpi, display-url-api-2.2.0.hpi</sub>

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

<sub>Tested Plugin Versions: script-security-1.44.hpi</sub>

Security

```groovy
users {
    accounts=[
        'jimbo': 'userpassword1',
        'timbo': 'ENC(AAAADIG5XwfoURydvwWm/MRnLy6v20hU3fyCpXx9Wu7BZwErfD94wxz0EmDG)',
        'bob': 'bobspassword',
        'doug': 'dougspassword'
    ]
}

// Can also use variables
def adminGroup       = '840491fe-167d-4eaa-b137-9877712a9b3f'
def readOnlyGroup    = '9a966d41-b9bb-4951-909f-2ea5f3f43310'
def developmentGroup = 'ef369fdf-6307-4e43-a4aa-28233fde0071'

security {

    authStrategy = 'Matrix-based security'

    matrix=[
        'hudson.model.Hudson.Administer':  ['jimbo', 'timbo', adminGroup],
        'hudson.model.Hudson.Read':        [readOnlyGroup, developmentGroup],
        'hudson.model.Computer.Build':     ['bob', 'doug', developmentGroup],

        //  Can also do stuff like:
        // 'hudson.model.Hudson.Administer': 'authenticated',
        // 'hudson.model.Hudson.Read': 'anonymous'
    ]
}

/**
  Also possible:

  security {

      authStrategy = 'Logged-in users can do anything'

      allowAnonymousRead = true

  }

*/
```

<sub>Tested Plugin Versions: matrix-auth-2.3.hpi, icon-shim-2.0.3.hpi, matrix-auth-2.3.hpi, icon-shim-2.0.3.hpi</sub>

Shell

```groovy
tools {
    shell = [path: '/bin/bash']
}
```

<sub>Tested Plugin Versions: NA</sub>

Slack

```groovy
credentials {
    authToken=['type': 'StringCredential', 'token': 'ENC(AAAADBCsrisG4HKN89K4oEKwHbHlA2wKZqR1n275eeadIXrVUTctvseX/jb9V6qv2w==)', 'description': 'Slack token']
}

/**
  Other options that can be used:
  baseUrl,
  botUser - string, 'true' or 'false'
  buildServerUrl,
  room,
  sendAs
*/
slack {
  teamDomain = 'buildit'
  tokenCredentialId = 'authToken'
}
```

<sub>Tested Plugin Versions: credentials-2.1.16.hpi, display-url-api-2.2.0.hpi, junit-1.23.hpi, plain-credentials-1.4.hpi, scm-api-2.2.6.hpi, script-security-1.44.hpi, slack-2.3.hpi, structs-1.14.hpi, workflow-api-2.27.hpi, workflow-step-api-2.14.hpi</sub>

SonarQube

```groovy
tools {
    sonar {
        qube =
        [
                [
                        name            : "SONAR6",
                        serverUrl       : "http://10.113.140.170:9000/sonar",
                        serverAuthenticationToken: "someToken"
                ],
                [
                        name            : "SONAR6-2",
                        serverUrl       : "http://10.113.140.171:9000/sonar",
                        serverAuthenticationToken: "someToken1",
                        additionalCommandLineArguments: "-X",
                        additionalAnalysisProperties: "sonar.organization=buildit"
                ]
        ]
    }
}
```

<sub>Tested Plugin Versions: sonar-2.8.hpi, maven-plugin-3.1.2.hpi, mailer-1.20.hpi, javadoc-1.1.hpi, junit-1.23.hpi, jquery-1.11.2-0.hpi, display-url-api-2.2.0.hpi, workflow-step-api-2.14.hpi, workflow-api-2.27.hpi, script-security-1.44.hpi, structs-1.14.hpi, scm-api-2.2.6.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, ssh-credentials-1.13.hpi, credentials-2.1.16.hpi</sub>

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

<sub>Tested Plugin Versions: sonar-2.8.hpi, maven-plugin-3.1.2.hpi, mailer-1.20.hpi, javadoc-1.1.hpi, junit-1.23.hpi, jquery-1.11.2-0.hpi, display-url-api-2.2.0.hpi, workflow-step-api-2.14.hpi, workflow-api-2.27.hpi, script-security-1.44.hpi, structs-1.14.hpi, scm-api-2.2.6.hpi, apache-httpcomponents-client-4-api-4.5.3-2.1.hpi, jsch-0.1.54.1.hpi, ssh-credentials-1.13.hpi, credentials-2.1.16.hpi</sub>

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

<sub>Tested Plugin Versions: splunk-devops-1.6.0.hpi, script-security-1.44.hpi</sub>

Ssh

```groovy
tools {
    ssh =
            [name             : 'dev-p1-app-01',
             hostname         : 'dev-p1-app-01',
             username         : 'user.name',
             encryptedPassword: 'ENC(AAAADDi67hVsOnOklqPJMGDW/kYaYGtvwEBGYx6POU72V8IjL3durqHPZ8s=)',
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

<sub>Tested Plugin Versions: antisamy-markup-formatter-1.5.hpi</sub>

SystemProperties

```groovy
systemProperties = [
    "hudson.model.DirectoryBrowserSupport.CSP":""
]
```

<sub>Tested Plugin Versions: NA</sub>

Users

```groovy
/**

  Creates users using the local Jenkins user database.  Really only intended
  to provide quick minimal security for public cloud development environments.

*/

security {
    authStrategy = 'Logged-in users can do anything'
}

users {
    accounts=[
     // 'username': 'password'
        'jimbo': 'userpassword1',
        'timbo': 'ENC(AAAADIG5XwfoURydvwWm/MRnLy6v20hU3fyCpXx9Wu7BZwErfD94wxz0EmDG)'
    ]
}
```

<sub>Tested Plugin Versions: matrix-auth-2.3.hpi, icon-shim-2.0.3.hpi, matrix-auth-2.3.hpi, icon-shim-2.0.3.hpi</sub>



Note. This file is generated using docs.gradle

```
gradle -b docs.gradle generate
```

Whatever you do, don't go editing the readme.md file directly :-)

