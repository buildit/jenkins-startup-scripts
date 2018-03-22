package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static junit.framework.TestCase.assertTrue
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class HipChatTest {
    private static
    final List SCRIPTS = ["main.groovy", "scripts/hipchat.groovy", "scripts/credentials.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(HipChatTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["hipchat-2.1.1.hpi", "workflow-step-api-2.14.hpi", "credentials-2.1.16.hpi",
            "display-url-api-2.2.0.hpi", "junit-1.23.hpi", "matrix-project-1.12.hpi", "plain-credentials-1.4.hpi",
            "token-macro-2.3.hpi", "structs-1.10.hpi", "script-security-1.40.hpi", "ssh-credentials-1.13.hpi",
            "workflow-job-2.16.hpi", "workflow-api-2.26.hpi", "scm-api-2.2.6.hpi", "workflow-support-2.16.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureHipChat() {
        def descriptor = jenkinsRule.instance.getDescriptor('jenkins.plugins.hipchat.HipChatNotifier')
        assertThat(descriptor.getServer() as String, equalTo('hipchat.com'))
        assertTrue(descriptor.isV2Enabled() as boolean)
        assertThat(descriptor.getRoom() as String, equalTo('Default Room'))
        assertThat(descriptor.getSendAs() as String, equalTo('Jenkins'))
        assertThat(descriptor.getCredentialId() as String, equalTo('hipchatToken'))
    }
}
