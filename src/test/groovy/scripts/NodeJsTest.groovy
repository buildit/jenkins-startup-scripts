package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.containsString
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class NodeJsTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/nodejs.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(NodeJsTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin("nodejs-0.2.1.hpi")
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureNodeInstallationsFromConfig() {
        def nodejsConfig = new File(jenkinsRule.jenkins.root.getAbsoluteFile(), "nodejs.xml").text
        assertThat(nodejsConfig, containsString("<name>nodejs-4.2.3</name>"))
        assertThat(nodejsConfig, containsString("<id>4.2.3</id>"))
    }
}
