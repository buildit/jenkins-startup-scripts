package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.junit.Assert.assertTrue
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class RoleStrategyTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/rolestrategy.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(RoleStrategyTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["matrix-auth-1.4.hpi", "icon-shim-2.0.3.hpi", "role-strategy-2.3.2.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureRoleStrategyFromConfig() {
        def plugin = jenkinsRule.jenkins.getPlugin('role-strategy')
        plugin.load()

        def authorizationStrategy = jenkinsRule.jenkins.getAuthorizationStrategy()

        assertTrue(authorizationStrategy.groups.contains('john.doe'))
        assertTrue(authorizationStrategy.groups.contains('david.doe'))
    }
}
