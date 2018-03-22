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

class CustomToolTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/customtool.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(CustomToolTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["custom-tools-plugin-0.4.4.hpi", "extended-choice-parameter-0.28.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureCustomToolInstallationsFromConfig() {
        def descriptor = jenkinsRule.jenkins.getDescriptor('com.cloudbees.jenkins.plugins.customtools.CustomTool')
        assertThat(descriptor.getInstallations().length as Integer, equalTo(1))
        assertThat(descriptor.getInstallations()[0].getName() as String, equalTo('cf'))
    }

}
