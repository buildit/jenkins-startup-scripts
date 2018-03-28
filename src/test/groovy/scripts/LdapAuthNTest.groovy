package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
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
    void shouldSetPermissions() {
        def securityRealm = jenkinsRule.instance.getSecurityRealm()
        println(securityRealm)
        assertThat(securityRealm.managerDN as String, equalTo("uid=binduser,cn=sysaccounts,cn=etc,dc=mavel,dc=com"))
        assertThat(securityRealm.managerPasswordSecret as String, equalTo("12345678910"))
    }
}
