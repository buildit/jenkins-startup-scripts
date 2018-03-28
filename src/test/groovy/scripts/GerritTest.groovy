package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class GerritTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(GerritTest.class, ["scripts/gerrit.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin("gerrit-trigger-2.17.2.hpi")
    @ZipTestFiles(files = ["jenkins.config", "gerrit_rsa"])
    void shouldConfigureGerritServerFromConfig() {
        def plugin = jenkinsRule.jenkins.getPlugin('gerrit-trigger')
        plugin.load()

        assertThat(plugin.getServers().size(), equalTo(1))

        def server = plugin.getServer('Fake Gerrit')
        assertThat(server.getHostName() as String, equalTo('gerrit.sandbox.local'))
        assertThat(server.getFrontEndUrl() as String, equalTo('http://gerrit.sandbox/'))
        assertThat(server.getSshPort() as int, equalTo(29418))
        assertThat(server.getUserName() as String, equalTo('jenkins'))

        def config = server.getConfig()
        assertTrue(config.isGerritBuildCurrentPatchesOnly() as boolean)
        assertThat(config.getGerritBuildStartedVerifiedValue() as int, equalTo(0))
        assertThat((config.getGerritAuthKeyFile() as File).absolutePath, equalTo("${jenkinsRule.jenkins.root}/gerrit_rsa".toString()))
        assertThat(config.getGerritAuthKeyFilePassword() as String, equalTo('password'))
        assertThat(config.getWatchdogTimeoutMinutes() as int, equalTo(1))
    }
}
