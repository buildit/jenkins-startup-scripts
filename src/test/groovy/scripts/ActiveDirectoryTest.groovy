package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class ActiveDirectoryTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(ActiveDirectoryTest.class, ["scripts/activedirectory.groovy"])
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
