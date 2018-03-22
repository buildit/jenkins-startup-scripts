package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.containsString
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class ScriptSecurityTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/scriptsecurity.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(ScriptSecurityTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["script-security-1.40.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldAddSignaturesToApprovedList() {
        def plugin = jenkinsRule.jenkins.getPlugin('script-security')
        plugin.load()

        def scriptApprovalConfig = new File(jenkinsRule.jenkins.root.getAbsoluteFile(), "scriptApproval.xml").text
        assertThat(scriptApprovalConfig, containsString("field hudson.ProxyConfiguration name"))
        assertThat(scriptApprovalConfig, containsString("field hudson.ProxyConfiguration port"))
        assertThat(scriptApprovalConfig, containsString("35f0f8ad5c16ff2873f66efe9bf556aba0512bef"))

    }
}
