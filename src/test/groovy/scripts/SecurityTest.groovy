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
        assertThat(users).extracting("id").containsExactly('jimbo', 'timbo')

        assertThat(securityRealm).isExactlyInstanceOf(HudsonPrivateSecurityRealm.class)
        assertThat(securityRealm.allowsSignup()).isFalse()

        assertThat(authStrategy.getClass().getName()).isEqualTo('hudson.security.GlobalMatrixAuthorizationStrategy')

        assertThat(authStrategy.grantedPermissions.get((Permission.fromId('hudson.model.Hudson.Administer')))).containsExactly('jimbo')
        assertThat(authStrategy.grantedPermissions.get((Permission.fromId('hudson.model.Hudson.Read')))).containsExactly('timbo')
        assertThat(authStrategy.grantedPermissions.get((Permission.fromId('hudson.model.Computer.Build')))).containsExactly('timbo')
        assertThat(authStrategy.grantedPermissions.get((Permission.fromId('hudson.model.Computer.Configure')))).containsExactly('timbo')
        assertThat(authStrategy.grantedPermissions.get((Permission.fromId('hudson.model.Computer.Connect')))).containsExactly('timbo')
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
