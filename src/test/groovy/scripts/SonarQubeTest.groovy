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


class SonarQubeTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/sonarqube.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    static void setUp() {
        addScriptToLocalDataZip(SonarQubeTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["sonar-2.6.1.hpi", "maven-plugin-2.7.1.hpi", "mailer-1.20.hpi", "javadoc-1.1.hpi", "junit-1.23.hpi",
            "jquery-1.11.2-0.hpi", "display-url-api-2.2.0.hpi", "workflow-step-api-2.14.hpi", "workflow-api-2.26.hpi",
            "script-security-1.40.hpi", "structs-1.10.hpi"])
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
