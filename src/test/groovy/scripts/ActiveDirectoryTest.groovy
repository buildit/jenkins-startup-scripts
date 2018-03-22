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
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class ActiveDirectoryTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/activedirectory.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(ActiveDirectoryTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["active-directory-2.0.hpi", "mailer-1.20.hpi", "display-url-api-2.2.0.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureActiveDirectoryFromConfig() {
        def realm = jenkinsRule.jenkins.getSecurityRealm()
        assertThat(realm.getDomains().size(), equalTo(1))
        assertThat(realm.getDomains().get(0).getName(), equalTo('host.local'))
        assertThat(realm.getDomains().get(0).getServers(), equalTo('server.example.com:3268'))
        assertThat(realm.bindName as String, equalTo('dn'))
        assertThat(realm.bindPassword as String, equalTo('pwd'))
    }
}
