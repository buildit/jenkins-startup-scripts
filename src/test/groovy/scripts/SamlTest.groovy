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
    @WithPlugin(["matrix-auth-2.3.hpi", "saml-1.0.7.hpi", "icon-shim-2.0.3.hpi",
                 "bouncycastle-api-2.16.1.hpi", "mailer-1.20.hpi", "display-url-api-2.2.0.hpi"])
    void shouldConfigureSaml() {

        def realm = jenkinsRule.instance.securityRealm

        assertThat(realm.getIdpMetadataConfiguration().getXml()).isEqualTo(expectedMetadataXml)
        assertThat(realm.getIdpMetadataConfiguration().getUrl()).isEqualTo('https://nexus.microsoftonline-p.com/federationmetadata/saml20/federationmetadata.xml')
        assertThat(realm.getIdpMetadataConfiguration().getPeriod()).isEqualTo(15)

        assertThat(realm.getUsernameAttributeName()).isEqualTo('http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name')
        assertThat(realm.getDisplayNameAttributeName()).isEqualTo('http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name')
        assertThat(realm.getGroupsAttributeName()).isEqualTo('http://schemas.microsoft.com/ws/2008/06/identity/claims/groups')
        assertThat(realm.getMaximumAuthenticationLifetime()).isEqualTo(1209600)
        assertThat(realm.getAdvancedConfiguration()).isNull()
        assertThat(realm.getBinding()).isEqualTo('urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect')
        assertThat(realm.getEncryptionData()).isNull()
        assertThat(realm.getUsernameCaseConversion()).isEqualTo('None')
        assertThat(realm.getEmailAttributeName()).isEqualTo('email.attribute')
        assertThat(realm.getLogoutUrl()).isEqualTo('https://logout.com')
        assertThat(realm.getSamlCustomAttributes()).isEmpty()
    }

    def expectedMetadataXml = '''\
&lt;EntityDescriptor xmlns=&quot;urn:oasis:names:tc:SAML:2.0:metadata&quot; xmlns:alg=&quot;urn:oasis:names:tc:SAML:metadata:algsupport&quot; ID=&quot;_0c0d1ca7-7292-4bc6-801c-f880f6098f4e&quot; entityID=&quot;urn:federation:MicrosoftOnline&quot;&gt;
&lt;Signature xmlns=&quot;http://www.w3.org/2000/09/xmldsig#&quot;&gt;
&lt;SignedInfo&gt;
&lt;CanonicalizationMethod Algorithm=&quot;http://www.w3.org/2001/10/xml-exc-c14n#&quot;/&gt;
&lt;SignatureMethod Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#rsa-sha1&quot;/&gt;
&lt;Reference URI=&quot;#_0c0d1ca7-7292-4bc6-801c-f880f6098f4e&quot;&gt;
&lt;Transforms&gt;
&lt;Transform Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#enveloped-signature&quot;/&gt;
&lt;Transform Algorithm=&quot;http://www.w3.org/2001/10/xml-exc-c14n#&quot;/&gt;
&lt;/Transforms&gt;
&lt;DigestMethod Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#sha1&quot;/&gt;
&lt;DigestValue&gt;7XES82rGE5v/Hdsytuvg9wp8iBE=&lt;/DigestValue&gt;
&lt;/Reference&gt;
&lt;/SignedInfo&gt;
&lt;SignatureValue&gt;
OOSrXuQqx/V/XjkOnEMuSVaGsb5a25e5kT5ntSDn6TwVjQR35l1o918OoEzF0+KNe+MZhfGdzzntFsG96Ifq8lM8hcVzwG6/xH9Ar+jdwSe7cemd3j7d48H1WIIx6M60rzHlkHnegbrXbUwWZLGAGyQu/P2z9Z8lIjzzjbZi0THFx5aZR5goI91N6eLYBP3ms75QDTb3749InVb+Yc//klycFkFfa5kViDqkNJULw8d+S6BGPsfzd+V72ih16YLegHLZAOhv0Te07QiVgFvNbArcJcyUbXZf+VHW4XyG2YYCL1F1cwT5GFDdemq7Fc+TacEPqq5SxsAq7Nu9pM78Sg==
&lt;/SignatureValue&gt;
&lt;KeyInfo&gt;
&lt;X509Data&gt;
&lt;X509Certificate&gt;
MIIDYDCCAkigAwIBAgIJALLJPAyvf2sjMA0GCSqGSIb3DQEBBQUAMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleTAeFw0xNDA3MTgxOTUzNDBaFw0xOTA3MTcxOTUzNDBaMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANYDKgByFZdqtTnnpF4IfIp4i2XLg2rLIo+mu4DmW9gRLlBJCNc7YESUxpKzuFYaANd8fWsDigJZTXbhOQApSpw4xXFnor2vJ1zm94LtqjcVEXTjUml5gAIS4pwuOU3ZfO/0eTG0gDYp4a0L/mzzTRsnwe/8WMPIE75Bq2zAyAZ9aePvl3QX7cXYLPfeK4QTgK3B5lwe1wWu3y5oQidjcSok8Frf80xzuCYuOa+ZUK3JibpLLCrT4uwiqf+KREDSdc4bPPlq0PWI4sQr1tha8yypRSvOH+/MxcfSRSnl6Uc+gm8nVEEWWIu4hhu6NIfG91mMUqJuzkgLCi6Gov6JS8UCAwEAAaOBijCBhzAdBgNVHQ4EFgQUnQoq7sI3R8rde4sQs6nGEbJm3LcwWQYDVR0jBFIwUIAUnQoq7sI3R8rde4sQs6nGEbJm3LehLaQrMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleYIJALLJPAyvf2sjMAsGA1UdDwQEAwIBxjANBgkqhkiG9w0BAQUFAAOCAQEAf4jaNhKzRG3k+52WoM9nnISP7rlWIeWwH6EQGUlF6ozSP/03gYMAdqpdhww5zNwKzi7TQVbDC0pgq/tqzHv6JEI0R4B6h7/TJ1pYPxdvIFQrE27RHESltH/m+5UkVnayLqRD3/fi4zf4aEpxSDZ73MCR5LanPGqvlAMz29AL3g1ynj+eu7xMfFsM/8+qJaCXuxT5/30eeLEe+PYikA/PhEwp+qkDQWPvdAwEghuUaFvtKAgDZierjpGzHZnYkXTTDTHVe1iP7tsAJH5qK3qdcv3UGPyZrjC/lietJcAcnwVoZQ93v2ieGfcKKN+PFN9M59/BkPo62HPoGNNx2ZDQaQ==
&lt;/X509Certificate&gt;
&lt;/X509Data&gt;
&lt;/KeyInfo&gt;
&lt;/Signature&gt;
&lt;Extensions&gt;
&lt;alg:DigestMethod Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#sha1&quot;/&gt;
&lt;alg:SigningMethod Algorithm=&quot;http://www.w3.org/2000/09/xmldsig#rsa-sha1&quot;/&gt;
&lt;/Extensions&gt;
&lt;SPSSODescriptor protocolSupportEnumeration=&quot;urn:oasis:names:tc:SAML:2.0:protocol&quot; WantAssertionsSigned=&quot;true&quot;&gt;
&lt;KeyDescriptor use=&quot;signing&quot;&gt;
&lt;ds:KeyInfo xmlns:ds=&quot;http://www.w3.org/2000/09/xmldsig#&quot;&gt;
&lt;ds:X509Data&gt;
&lt;ds:X509Certificate&gt;
MIIDYDCCAkigAwIBAgIJALLJPAyvf2sjMA0GCSqGSIb3DQEBBQUAMCkxJzAlBgNV BAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleTAeFw0xNDA3MTgxOTUz NDBaFw0xOTA3MTcxOTUzNDBaMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25p bmcgUHVibGljIEtleTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANYD KgByFZdqtTnnpF4IfIp4i2XLg2rLIo+mu4DmW9gRLlBJCNc7YESUxpKzuFYaANd8 fWsDigJZTXbhOQApSpw4xXFnor2vJ1zm94LtqjcVEXTjUml5gAIS4pwuOU3ZfO/0 eTG0gDYp4a0L/mzzTRsnwe/8WMPIE75Bq2zAyAZ9aePvl3QX7cXYLPfeK4QTgK3B 5lwe1wWu3y5oQidjcSok8Frf80xzuCYuOa+ZUK3JibpLLCrT4uwiqf+KREDSdc4b PPlq0PWI4sQr1tha8yypRSvOH+/MxcfSRSnl6Uc+gm8nVEEWWIu4hhu6NIfG91mM UqJuzkgLCi6Gov6JS8UCAwEAAaOBijCBhzAdBgNVHQ4EFgQUnQoq7sI3R8rde4sQ s6nGEbJm3LcwWQYDVR0jBFIwUIAUnQoq7sI3R8rde4sQs6nGEbJm3LehLaQrMCkx JzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleYIJALLJPAyv f2sjMAsGA1UdDwQEAwIBxjANBgkqhkiG9w0BAQUFAAOCAQEAf4jaNhKzRG3k+52W oM9nnISP7rlWIeWwH6EQGUlF6ozSP/03gYMAdqpdhww5zNwKzi7TQVbDC0pgq/tq zHv6JEI0R4B6h7/TJ1pYPxdvIFQrE27RHESltH/m+5UkVnayLqRD3/fi4zf4aEpx SDZ73MCR5LanPGqvlAMz29AL3g1ynj+eu7xMfFsM/8+qJaCXuxT5/30eeLEe+PYi kA/PhEwp+qkDQWPvdAwEghuUaFvtKAgDZierjpGzHZnYkXTTDTHVe1iP7tsAJH5q K3qdcv3UGPyZrjC/lietJcAcnwVoZQ93v2ieGfcKKN+PFN9M59/BkPo62HPoGNNx 2ZDQaQ==
&lt;/ds:X509Certificate&gt;
&lt;/ds:X509Data&gt;
&lt;/ds:KeyInfo&gt;
&lt;/KeyDescriptor&gt;
&lt;KeyDescriptor use=&quot;signing&quot;&gt;
&lt;ds:KeyInfo xmlns:ds=&quot;http://www.w3.org/2000/09/xmldsig#&quot;&gt;
&lt;ds:X509Data&gt;
&lt;ds:X509Certificate&gt;
MIIDYDCCAkigAwIBAgIJAMkTRrgd+CoIMA0GCSqGSIb3DQEBCwUAMCkxJzAlBgNV BAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleTAeFw0xNjEyMDYyMjA2 MjlaFw0yMTEyMDUyMjA2MjlaMCkxJzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25p bmcgUHVibGljIEtleTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAN4S BAyO92gibTP+qXxrc/+OVG/kznAlMf4VW+jCakTjHYh3m+WzWa0HI0ZmQX1O7roZ MTWGjLQFpSA8BaTnHPxYgdK48wkdVIMeCPDeqvgZfZsuPIP57MjJb/v0X6PlHU6X 2L/SWfNCGVjWwbL+LdwWIPdVN9jhCuh99UHGHZ0Be5sZLqg2sBildMUwqKnj5V+G v9w4ZqSaF5jz7SzSm5d+a9A9CrX70q1ynZSMsgtsLJ1+spscCJD+hfMVTL8hqDUQ BxoPS5AvnPC9iSkUYDsyOzhm1pIpTgb6SrZ59IF/MKsGINM0hHRaRs7gxg+V0Sy4 VA6gVLJlSM7JIKCHG/sCAwEAAaOBijCBhzAdBgNVHQ4EFgQUzSeR6iod/rh1A50s /s8c91qBYtYwWQYDVR0jBFIwUIAUzSeR6iod/rh1A50s/s8c91qBYtahLaQrMCkx JzAlBgNVBAMTHkxpdmUgSUQgU1RTIFNpZ25pbmcgUHVibGljIEtleYIJAMkTRrgd +CoIMAsGA1UdDwQEAwIBxjANBgkqhkiG9w0BAQsFAAOCAQEA0xjNKxnEDYiMoiUO qm1i3fjBEW348eTpkRbHmJizHrnnaLEQn+8JOqSmMntDRd9iuk+/GOR4ALJEJ6Lu KvRUK5/cc79nN2Muq0cnGPHu6OhVsBLRG/UnPnrwtuLTwuxso5d9oZ3QckRE+yKl c8fYFhOWe43lGKb8PTlveZcZ7y0eJ5gFOMhM5AuwjWFvR/GiiQ3JTEvqM/MoCHE/ +4+VvE6AYg5+AI/LF0kYFP8f9aazqoBjljDj40+IMkj6LGkdoSKqx2SnT0F9VgPX 9E/ffrIOhyj+jwkA4M7sXaFoVs8cLdLurTVSjMuEvquO3lHbPunOXBPv029oTSEs V4ArsQ==
&lt;/ds:X509Certificate&gt;
&lt;/ds:X509Data&gt;
&lt;/ds:KeyInfo&gt;
&lt;/KeyDescriptor&gt;
&lt;SingleLogoutService Binding=&quot;urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST&quot; Location=&quot;https://login.microsoftonline.com/login.srf&quot;/&gt;
&lt;NameIDFormat&gt;
urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress
&lt;/NameIDFormat&gt;
&lt;NameIDFormat&gt;urn:mace:shibboleth:1.0:nameIdentifier&lt;/NameIDFormat&gt;
&lt;NameIDFormat&gt;
urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified
&lt;/NameIDFormat&gt;
&lt;NameIDFormat&gt;
urn:oasis:names:tc:SAML:2.0:nameid-format:transient
&lt;/NameIDFormat&gt;
&lt;NameIDFormat&gt;
urn:oasis:names:tc:SAML:2.0:nameid-format:persistent
&lt;/NameIDFormat&gt;
&lt;AssertionConsumerService isDefault=&quot;true&quot; index=&quot;0&quot; Binding=&quot;urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST&quot; Location=&quot;https://login.microsoftonline.com/login.srf&quot;/&gt;
&lt;AssertionConsumerService index=&quot;1&quot; Binding=&quot;urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign&quot; Location=&quot;https://login.microsoftonline.com/login.srf&quot;/&gt;
 &lt;!--
 PAOS functionality is NOT supported by this service. The binding is only included to ease setup and integration with Shibboleth ECP
--&gt;
&lt;AssertionConsumerService index=&quot;2&quot; Binding=&quot;urn:oasis:names:tc:SAML:2.0:bindings:PAOS&quot; Location=&quot;https://login.microsoftonline.com/login.srf&quot;/&gt;
&lt;/SPSSODescriptor&gt;
&lt;/EntityDescriptor&gt;
'''
}
