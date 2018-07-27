package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.assertThat

class SamlTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SamlTest.class, ["scripts/saml.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "saml-1.0.7.hpi", "icon-shim-2.0.3.hpi",
                 "bouncycastle-api-2.16.1.hpi", "mailer-1.20.hpi", "display-url-api-2.2.0.hpi"])
    void shouldConfigureSaml() {

        def realm = jenkinsRule.instance.securityRealm

        assertThat(realm.getIdpMetadataConfiguration().getUrl()).isEqualTo('https://nexus.microsoftonline-p.com/federationmetadata/saml20/federationmetadata.xml')
        assertThat(realm.getIdpMetadataConfiguration().getPeriod()).isEqualTo(15)

        assertThat(realm.getUsernameAttributeName()).isEqualTo('http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name')
        assertThat(realm.getDisplayNameAttributeName()).isEqualTo('http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name')
        assertThat(realm.getGroupsAttributeName()).isEqualTo('http://schemas.microsoft.com/ws/2008/06/identity/claims/groups')
        assertThat(realm.getMaximumAuthenticationLifetime()).isEqualTo(1209600)
        assertThat(realm.getAdvancedConfiguration()).isNull()
        assertThat(realm.getBinding()).isEqualTo('HTTP-Redirect')
        assertThat(realm.getEncryptionData()).isNull()
        assertThat(realm.getUsernameCaseConversion()).isEqualTo('None')
        assertThat(realm.getEmailAttributeName()).isEqualTo('email.attribute')
        assertThat(realm.getLogoutUrl()).isEqualTo('https://logout.com')
        assertThat(realm.getSamlCustomAttributes()).isEmpty()
    }
}
