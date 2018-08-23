package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class LdapAuthNTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(LdapAuthNTest.class, ["scripts/ldapauthn.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["ldap-1.15.hpi", "mailer-1.20.hpi", "display-url-api-2.2.0.hpi", "matrix-auth-2.3.hpi", "icon-shim-2.0.3.hpi"])
    void shouldSetPermissions() {
        def securityRealm = jenkinsRule.instance.getSecurityRealm()
        println(securityRealm)
        assertThat(securityRealm.managerDN as String, equalTo("uid=binduser,cn=sysaccounts,cn=etc,dc=mavel,dc=com"))
        assertThat(securityRealm.managerPasswordSecret as String, equalTo("12345678910"))
    }
}
