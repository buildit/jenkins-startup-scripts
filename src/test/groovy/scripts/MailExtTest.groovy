package scripts

import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat

class MailExtTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(MailExtTest.class, ["scripts/mailext.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["email-ext-2.58.hpi", "mailer-1.20.hpi", "matrix-project-1.12.hpi", "script-security-1.40.hpi",
            "token-macro-2.3.hpi", "junit-1.23.hpi", "script-security-1.40.hpi", "workflow-step-api-2.14.hpi",
            "workflow-api-2.26.hpi", "scm-api-2.2.6.hpi", "structs-1.14.hpi", "display-url-api-2.2.0.hpi", "workflow-job-2.16.hpi",
            "workflow-support-2.16.hpi"])
    void shouldConfigureMailFromConfig() {
        def settings = Jenkins.instance.getDescriptor("hudson.plugins.emailext.ExtendedEmailPublisher")
        assertNotNull(settings)
        assertThat(settings.smtpServer as String, equalTo("159.34.192.34"))
        assertThat(settings.defaultSuffix as String, equalTo("@somewhere.com"))
    }
}