package scripts

import hudson.tools.ZipExtractionInstaller
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

class SonarRunnerTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SonarRunnerTest.class, ["scripts/sonarrunner.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["sonar-2.6.1.hpi", "maven-plugin-2.17.hpi", "mailer-1.20.hpi", "javadoc-1.1.hpi", "junit-1.23.hpi",
            "jquery-1.11.2-0.hpi", "display-url-api-2.2.0.hpi", "workflow-step-api-2.14.hpi", "workflow-api-2.27.hpi",
            "script-security-1.44.hpi", "structs-1.14.hpi","scm-api-2.2.6.hpi","apache-httpcomponents-client-4-api-4.5.3-2.1.hpi",
            "jsch-0.1.54.1.hpi","ssh-credentials-1.13.hpi","credentials-2.1.16.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureSonarInstallationsFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("hudson.plugins.sonar.SonarRunnerInstallation")
        def installations = descriptor.getInstallations()
        assertThat(installations.size() as int, equalTo(2))

        def installer = getInstaller(installations[0])
        assertThat(installations[0].getName() as String, equalTo("sonar"))
        assertThat(installer.label as String, equalTo("master"))
        assertThat(installer.url as String, equalTo("http://example.com/sonar.zip"))
        assertThat(installer.subdir as String, equalTo("bin"))

        installer = getInstaller(installations[1])
        assertThat(installations[1].getName() as String, equalTo("sonar14"))
        assertThat(installer.label as String, equalTo("slave"))
        assertThat(installer.url as String, equalTo("http://example.com/sonar14.zip"))
        assertThat(installer.subdir as String, equalTo("bin"))
    }

    // The Jenkins API here is a bit crazy.
    private ZipExtractionInstaller getInstaller(installation) {
        def installSourceDescriptor = jenkinsRule.instance.getDescriptor("hudson.tools.InstallSourceProperty")
        def zipExtractionInstallerDescriptor = jenkinsRule.instance.getDescriptor("hudson.tools.ZipExtractionInstaller")
        return installation.getProperties().get(installSourceDescriptor).installers.get(zipExtractionInstallerDescriptor)
    }
}
