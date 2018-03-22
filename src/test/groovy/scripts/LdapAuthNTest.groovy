package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class LdapAuthNTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/ldapauthn.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(LdapAuthNTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldSetPermissions() {
        def securityRealm = jenkinsRule.instance.getSecurityRealm()
        println(securityRealm)
        assertThat(securityRealm.managerDN as String, equalTo("uid=binduser,cn=sysaccounts,cn=etc,dc=mavel,dc=com"))
        assertThat(securityRealm.managerPasswordSecret as String, equalTo("12345678910"))
    }
}
