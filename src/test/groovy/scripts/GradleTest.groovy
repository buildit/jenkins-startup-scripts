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

class GradleTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/gradle.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(GradleTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin("gradle-1.25.hpi")
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureGradleInstallationsFromConfig() {
        def descriptor = jenkinsRule.jenkins.getDescriptor('hudson.plugins.gradle.GradleInstallation')
        assertThat(descriptor.getInstallations().length, equalTo(3))
        assertThat(descriptor.getInstallations()[0].getName(), equalTo('gradle-2'))
        assertThat(descriptor.getInstallations()[1].getName(), equalTo('gradle-3'))
        assertThat(descriptor.getInstallations()[2].getName(), equalTo('gradle-2-1'))
    }
}
