package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.junit.Assert.assertTrue

class RoleStrategyTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(RoleStrategyTest.class, ["scripts/rolestrategy.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["matrix-auth-2.3.hpi", "icon-shim-2.0.3.hpi", "role-strategy-2.3.2.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureRoleStrategyFromConfig() {
        def plugin = jenkinsRule.jenkins.getPlugin('role-strategy')
        plugin.load()

        def authorizationStrategy = jenkinsRule.jenkins.getAuthorizationStrategy()

        assertTrue(authorizationStrategy.groups.contains('john.doe'))
        assertTrue(authorizationStrategy.groups.contains('david.doe'))
    }
}
