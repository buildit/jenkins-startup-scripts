package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static hudson.tasks.Maven.DescriptorImpl
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class MavenTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/maven.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(MavenTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureMavenInstallationsFromConfig() {
        def extensions = jenkinsRule.jenkins.getExtensionList(DescriptorImpl.class)
        assertThat(extensions.installations[0].length, equalTo(2))
        assertThat(extensions.installations[0][0].name, equalTo("maven-3.3.3"))
        assertThat(extensions.installations[0][0].properties[0].installers[0].url, equalTo("http://test_url"))
        assertThat(extensions.installations[0][1].name, equalTo("maven-3.3.9"))
    }
}
