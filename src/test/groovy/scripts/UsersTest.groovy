package scripts

import hudson.security.HudsonPrivateSecurityRealm
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.*

class UsersTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(UsersTest.class, ["scripts/users.groovy"])
        System.metaClass.static.getenv = { String key ->
            return [TEST_CONFIG_FILE: "jenkins.config,nosecurityblock.config"].get(key)
        }
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "icon-shim-2.0.3.hpi"])
    void should_create_users() {

        def securityRealm = jenkinsRule.instance.getSecurityRealm()

        assertThat(securityRealm).isExactlyInstanceOf(HudsonPrivateSecurityRealm.class)
        assertThat(securityRealm.allowsSignup()).isFalse()

        def users = securityRealm.getAllUsers()
        assertThat(users).extracting("id").containsExactly("jimbo", "timbo")
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["nosecurityblock.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "icon-shim-2.0.3.hpi"])
    void missing_security_block() {
        def securityRealm = jenkinsRule.instance.getSecurityRealm()

        // can't assert for exception, so just assert that nothing was configured
        assertThat(securityRealm.getClass().getName()).isEqualTo('hudson.security.SecurityRealm$None')
    }
}