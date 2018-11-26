package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertThat

class AquaSecTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(AquaSecTest.class, ["scripts/aquasec.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin([
            "aqua-security-scanner-3.0.12.hpi",
            "structs-1.14.hpi", "jdk-tool-1.1.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureAquaSecPlugin() {
        def descriptor = jenkinsRule.instance.getDescriptor('org.jenkinsci.plugins.aquadockerscannerbuildstep.AquaDockerScannerBuilder')
        assertFalse(descriptor.getCaCertificates() as boolean)
        assertThat(descriptor.getAquaScannerImage() as String, equalTo('aquasec/scanner-cli:3.2'))
        assertThat(descriptor.getApiURL() as String, equalTo('https://aquasec-server-url.com'))
        assertThat(descriptor.getUser() as String, equalTo('scanner'))
        assertThat(descriptor.getPassword() as String, equalTo('scanner123'))
        assertThat(descriptor.getVersion() as String, equalTo('3.x'))
        assertThat(descriptor.getTimeout() as int, equalTo(0))
        assertThat(descriptor.getRunOptions() as String, equalTo('test'))
    }
}
