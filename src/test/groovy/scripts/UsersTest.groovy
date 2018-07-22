package scripts

import hudson.security.FullControlOnceLoggedInAuthorizationStrategy
import hudson.security.HudsonPrivateSecurityRealm
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.*;

class UsersTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(UsersTest.class, ["scripts/users.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "icon-shim-2.0.3.hpi"])
    void shouldCreateUsers() {

        def securityRealm = jenkinsRule.instance.getSecurityRealm()
        def authStrategy = this.jenkinsRule.getInstance().getAuthorizationStrategy()

        assertThat(securityRealm).isExactlyInstanceOf(HudsonPrivateSecurityRealm.class)
        assertThat(securityRealm.allowsSignup()).isFalse()

        assertThat(authStrategy).isExactlyInstanceOf(FullControlOnceLoggedInAuthorizationStrategy.class)
        assertThat(authStrategy.isAllowAnonymousRead()).isFalse()

        def users = securityRealm.getAllUsers()
        assertThat(users).extracting("id").containsExactly("jimbo", "timbo")

        //Thread.sleep(1000 * 60 * 5)
    }
}