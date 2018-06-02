package scripts

import hudson.model.FreeStyleProject
import jenkins.model.GlobalConfiguration
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.core.IsEqual.equalTo
import static org.hamcrest.core.IsNot.not
import static org.hamcrest.core.IsNull.notNullValue
import static org.hamcrest.core.StringContains.containsString
import static org.junit.Assert.assertThat

class JobDSLTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(JobDSLTest.class, ["scripts/jobdsl.groovy"])
    }

    @Test(timeout = 30000L)
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["ace-editor-1.0.1.hpi", "jquery-detached-1.2.1.hpi", "workflow-cps-global-lib-2.5.hpi", "workflow-cps-2.23.hpi",
            "workflow-scm-step-2.6.hpi", "cloudbees-folder-6.1.0.hpi", "git-client-2.7.0.hpi", "git-server-1.7.hpi", "scm-api-2.2.6.hpi",
            "structs-1.14.hpi", "ssh-credentials-1.13.hpi", "credentials-2.1.16.hpi", "workflow-step-api-2.14.hpi",
            "workflow-api-2.26.hpi", "workflow-support-2.16.hpi", "ace-editor-1.0.1.hpi", "script-security-1.40.hpi",
            "git-3.7.0.hpi", "matrix-project-1.12.hpi", "mailer-1.20.hpi", "junit-1.23.hpi", "job-dsl-1.64.hpi",
            "credentials-binding-1.16.hpi", "credentials-2.1.16.hpi", "plain-credentials-1.4.hpi", "apache-httpcomponents-client-4-api-4.5.3-2.1.hpi",
            "jsch-0.1.54.1.hpi", "display-url-api-2.2.0.hpi", "git-client-2.7.0.hpi"])
    void shouldCreateAndRunSeedJob() {
        FreeStyleProject seed = jenkinsRule.jenkins.getItem("jobdsl");
        while (jenkinsRule.jenkins.getQueue().getItems().size() > 0) {
            sleep(500)
        }

        assertThat(seed.getBuilds().lastBuild, notNullValue())

        def jobdslXml = new URL("http://localhost:${jenkinsRule.localPort}/jenkins/job/jobdsl/config.xml").getText()
        assertThat(jobdslXml, not(containsString("<credentialsId>")))
        assertThat(jobdslXml, containsString("<additionalClasspath>src/main/groovy</additionalClasspath>"))

        //assertThat(new URL("http://localhost:${jenkinsRule.localPort}/jenkins/job/jobdslWithLabel/config.xml").getText(), containsString("<assignedNode>foo</assignedNode>"))
        assertThat(new URL("http://localhost:${jenkinsRule.localPort}/jenkins/job/jobdslWithCredentials/config.xml").getText(), containsString("<credentialsId>git</credentialsId>"))
        assertThat(getGlobalJobDslSecurityConfiguration().useScriptSecurity as Boolean, equalTo(false))
    }

    def getGlobalJobDslSecurityConfiguration() {
        for (def configuration : GlobalConfiguration.all()) {
            if (configuration.descriptor.displayName == "GlobalJobDslSecurityConfiguration") {
                return configuration
            }
        }
    }
}
