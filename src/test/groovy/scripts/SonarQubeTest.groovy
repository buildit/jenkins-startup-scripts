package scripts

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

class SonarQubeTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SonarQubeTest.class, ["scripts/sonarqube.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["sonar-2.8.hpi", "maven-plugin-3.1.2.hpi", "mailer-1.20.hpi", "javadoc-1.1.hpi", "junit-1.23.hpi",
            "jquery-1.11.2-0.hpi", "display-url-api-2.2.0.hpi", "workflow-step-api-2.14.hpi", "workflow-api-2.27.hpi",
            "script-security-1.44.hpi", "structs-1.14.hpi", "scm-api-2.2.6.hpi","apache-httpcomponents-client-4-api-4.5.3-2.1.hpi",
            "jsch-0.1.54.1.hpi","ssh-credentials-1.13.hpi","credentials-2.1.16.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureSonarInstallationsFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("hudson.plugins.sonar.SonarPublisher")
        def installations = descriptor.getInstallations()
        assertThat(installations.size(), equalTo(2))
        assertThat(installations[0].getName(), equalTo("SONAR6"))
        assertThat(installations[0].getServerUrl(), equalTo("http://10.113.140.170:9000/sonar"))
        assertThat(installations[0].getServerAuthenticationToken(), equalTo("someToken"))

        assertThat(installations[1].getName(), equalTo("SONAR6-2"))
        assertThat(installations[1].getServerUrl(), equalTo("http://10.113.140.171:9000/sonar"))
        assertThat(installations[1].getServerAuthenticationToken(), equalTo("someToken1"))
        assertThat(installations[1].getAdditionalProperties(), equalTo("-X"))
        assertThat(installations[1].getAdditionalAnalysisProperties(), equalTo("sonar.organization=buildit"))
    }
}
