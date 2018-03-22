package scripts

import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class MailExtTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/mailext.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(MailExtTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["email-ext-2.58.hpi", "mailer-1.20.hpi", "matrix-project-1.12.hpi", "script-security-1.40.hpi",
            "token-macro-2.3.hpi", "junit-1.23.hpi", "script-security-1.40.hpi", "workflow-step-api-2.14.hpi",
            "workflow-api-2.26.hpi", "scm-api-2.2.6.hpi", "structs-1.10.hpi", "display-url-api-2.2.0.hpi", "workflow-job-2.16.hpi",
            "workflow-support-2.16.hpi"])
    void shouldConfigureMailFromConfig() {
        def settings = Jenkins.instance.getDescriptor("hudson.plugins.emailext.ExtendedEmailPublisher")
        assertNotNull(settings)
        assertThat(settings.smtpServer as String, equalTo("159.34.192.34"))
        assertThat(settings.defaultSuffix as String, equalTo("@somewhere.com"))
    }
}