package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.Matchers.hasItems
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class SystemPropertiesTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/systemProperties.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(SystemPropertiesTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureSystemPropertiesFromConfig() {
        def systemPropertyList = System.getProperties()
        assertThat(systemPropertyList.keySet(), hasItems("hudson.model.DirectoryBrowserSupport.CSP"))
        assertThat(systemPropertyList.get("hudson.model.DirectoryBrowserSupport.CSP"), equalTo(""))
    }
}
