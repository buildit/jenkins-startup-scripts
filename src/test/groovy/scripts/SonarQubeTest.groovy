package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath


class SonarQubeTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SonarQubeTest.class, ["scripts/sonarqube.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["sonar-2.6.1.hpi", "maven-plugin-2.17.hpi", "mailer-1.20.hpi", "javadoc-1.1.hpi", "junit-1.23.hpi",
            "jquery-1.11.2-0.hpi", "display-url-api-2.2.0.hpi", "workflow-step-api-2.14.hpi", "workflow-api-2.27.hpi",
            "script-security-1.44.hpi", "structs-1.14.hpi", "scm-api-2.2.6.hpi","apache-httpcomponents-client-4-api-4.5.3-2.1.hpi",
            "jsch-0.1.54.1.hpi","ssh-credentials-1.13.hpi","credentials-2.1.16.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureSonarInstallationsFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("hudson.plugins.sonar.SonarPublisher");
        def installations = descriptor.getInstallations();
        assertThat(installations.size(), equalTo(2));
        assertThat(installations[0].getName(), equalTo("SONAR"));
        assertThat(installations[0].getServerUrl(), equalTo("http://10.113.140.170:9000/sonar"));
        assertThat(installations[0].getSonarLogin(), equalTo("admin"));
        assertThat(installations[0].getSonarPassword(), equalTo("somes3cret"));
        assertThat(installations[0].getServerVersion(), equalTo("5.1"))
        assertThat(installations[0].getDatabaseUrl(), equalTo("jdbc:mysql://10.113.140.170:3306/sonarqube?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useConfigs=maxPerformance"))
        assertThat(installations[0].getDatabaseLogin(), equalTo("sonarqube"))
        assertThat(installations[0].getDatabasePassword(), equalTo("Y/a5t0YI5fmWqx1NEEYoKQ=="))
        assertThat(installations[0].getServerAuthenticationToken(), equalTo(""))
        assertThat(installations[1].getName(), equalTo("SONAR6"));
        assertThat(installations[1].getServerUrl(), equalTo("http://10.113.140.170:9000/sonar"));
        assertThat(installations[1].getSonarLogin(), equalTo(""));
        assertThat(installations[1].getSonarPassword(), equalTo(""));
        assertThat(installations[1].getServerVersion(), equalTo("5.3"))
        assertThat(installations[1].getDatabaseUrl(), equalTo(""))
        assertThat(installations[1].getDatabaseLogin(), equalTo(""))
        assertThat(installations[1].getDatabasePassword(), equalTo(""))
        assertThat(installations[1].getServerAuthenticationToken(), equalTo("someToken"))
    }
}
