package scripts

import hudson.security.*
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.assertThat

class SecurityTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SecurityTest.class, ["scripts/users.groovy", "scripts/security.groovy"])
        System.metaClass.static.getenv = { String key ->
            return [TEST_CONFIG_FILE: "jenkins.config,loggedinusers.config"].get(key)
        }
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "icon-shim-2.0.3.hpi"])
    void matrix_auth() {

        def securityRealm = jenkinsRule.instance.getSecurityRealm()
        def authStrategy = this.jenkinsRule.getInstance().getAuthorizationStrategy()

        def users = securityRealm.getAllUsers()
        assertThat(users).extracting("id").containsExactlyInAnyOrder('jimbo', 'timbo', 'bob', 'doug')

        assertThat(securityRealm).isExactlyInstanceOf(HudsonPrivateSecurityRealm.class)
        assertThat(securityRealm.allowsSignup()).isFalse()

        assertThat(authStrategy.getClass().getName()).isEqualTo('hudson.security.GlobalMatrixAuthorizationStrategy')

        assertThat(authStrategy.grantedPermissions.get((Permission.fromId('hudson.model.Hudson.Administer')))).containsExactlyInAnyOrder('jimbo', 'timbo', '840491fe-167d-4eaa-b137-9877712a9b3f')
        assertThat(authStrategy.grantedPermissions.get((Permission.fromId('hudson.model.Hudson.Read')))).containsExactly('9a966d41-b9bb-4951-909f-2ea5f3f43310', 'ef369fdf-6307-4e43-a4aa-28233fde0071')
        assertThat(authStrategy.grantedPermissions.get((Permission.fromId('hudson.model.Computer.Build')))).containsExactlyInAnyOrder('bob', 'doug', 'ef369fdf-6307-4e43-a4aa-28233fde0071')
    }


    @Test
    @LocalData
    @ZipTestFiles(files = ["loggedinusers.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "icon-shim-2.0.3.hpi"])
    void logged_in_users_can_do_anything() {

        def securityRealm = jenkinsRule.instance.getSecurityRealm()
        def authStrategy = this.jenkinsRule.getInstance().getAuthorizationStrategy()

        assertThat(securityRealm).isExactlyInstanceOf(HudsonPrivateSecurityRealm.class)
        assertThat(securityRealm.allowsSignup()).isFalse()

        assertThat(authStrategy).isExactlyInstanceOf(FullControlOnceLoggedInAuthorizationStrategy.class)
        assertThat(authStrategy.isAllowAnonymousRead()).isTrue()

    }
}
