package scripts

import hudson.security.Permission
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class LdapAuthZTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/ldapauthz.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(LdapAuthZTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldSetPermissions() {
        def authorizationStrategy = jenkinsRule.instance.getAuthorizationStrategy()
        assertThat(authorizationStrategy.getGroups().size(), equalTo(5))
        assertThat(authorizationStrategy.grantedPermissions.size() as Integer, equalTo(5))
        assertTrue(authorizationStrategy.grantedPermissions.get(Permission.fromId('hudson.model.Hudson.Administer')).contains('my-jenkins-administrators'))
        assertTrue(authorizationStrategy.grantedPermissions.get(Permission.fromId('hudson.model.Computer.Disconnect')).contains('my-jenkins-developers'))
        assertTrue(authorizationStrategy.grantedPermissions.get(Permission.fromId('hudson.model.Computer.Build')).contains('my-jenkins-viewers'))
        assertTrue(authorizationStrategy.grantedPermissions.get(Permission.fromId('hudson.model.Item.Build')).contains('my-jenkins-webhook'))
        assertTrue(authorizationStrategy.grantedPermissions.get(Permission.fromId('hudson.model.View.Read')).contains('anonymous'))
    }
}
