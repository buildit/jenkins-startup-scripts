import jenkins.model.*
import hudson.plugins.git.*
import hudson.triggers.TimerTrigger
import javaposse.jobdsl.plugin.*
import javaposse.jobdsl.plugin.GlobalJobDslSecurityConfiguration
import jenkins.model.GlobalConfiguration

import javax.xml.transform.stream.StreamSource

GlobalConfiguration.all().get(GlobalJobDslSecurityConfiguration.class).useScriptSecurity = false

config.each { jobName, values ->
    if (values instanceof Map) {
        def bindings = [:].withDefault { key ->
            ''
        }
        bindings << values
        bindings.credentialsBlock = bindings.credentialsId ? "<credentialsId>${bindings.credentialsId}</credentialsId>" : ''
        bindings.labelBlock = bindings.label ? "<assignedNode>${bindings.label}</assignedNode>" : ''
        def xml = getXmlFromTemplate(bindings)
        def stream = new ByteArrayInputStream(xml.bytes)
        def project = Jenkins.getInstance().getItem(jobName)
        if (project) {
            println("Updating job with name ${jobName}")
            project.updateByXml(new StreamSource(stream))
            project.save()
        } else {
            println("Creating job with name ${jobName}")
            project = Jenkins.getInstance().createProjectFromXML(jobName, stream)
        }
        def causeAction = new hudson.model.CauseAction(new TimerTrigger.TimerTriggerCause())
        instance.getQueue().schedule(project, 10, causeAction)
    }
}

String getXmlFromTemplate(bindings) {
    def xml = '''
    <?xml version='1.0' encoding='UTF-8'?>
    <project>
        <actions/>
        <description></description>
        <logRotator class="hudson.tasks.LogRotator">
            <daysToKeep>28</daysToKeep>
            <numToKeep>30</numToKeep>
        </logRotator>
        <keepDependencies>false</keepDependencies>
        <scm class="hudson.plugins.git.GitSCM" plugin="git@3.0.0">
            <configVersion>2</configVersion>
            <userRemoteConfigs>
                <hudson.plugins.git.UserRemoteConfig>
                    <url>${url}</url>
                    ${credentialsBlock}
                </hudson.plugins.git.UserRemoteConfig>
            </userRemoteConfigs>
            <branches>
                <hudson.plugins.git.BranchSpec>
                    <name>${branch}</name>
                </hudson.plugins.git.BranchSpec>
            </branches>
            <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>
            <submoduleCfg class="list"/>
            <extensions/>
        </scm>
        ${labelBlock}
        <canRoam>true</canRoam>
        <disabled>false</disabled>
        <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
        <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
        <triggers>
            <hudson.triggers.SCMTrigger>
                <spec>H/5 * * * *</spec>
                <ignorePostCommitHooks>false</ignorePostCommitHooks>
            </hudson.triggers.SCMTrigger>
        </triggers>
        <concurrentBuild>false</concurrentBuild>
        <builders>
            <javaposse.jobdsl.plugin.ExecuteDslScripts plugin="job-dsl@1.51">
                <targets>${targets}</targets>
                <usingScriptText>false</usingScriptText>
                <ignoreExisting>false</ignoreExisting>
                <removedJobAction>DELETE</removedJobAction>
                <removedViewAction>DELETE</removedViewAction>
                <lookupStrategy>JENKINS_ROOT</lookupStrategy>
                <additionalClasspath>${additionalClasspath}</additionalClasspath>
            </javaposse.jobdsl.plugin.ExecuteDslScripts>
        </builders>
        <publishers/>
        <buildWrappers/>
    </project>
    '''
    def engine = new groovy.text.SimpleTemplateEngine()
    def template = engine.createTemplate(xml).make(bindings)
    return template.toString().trim()
}
