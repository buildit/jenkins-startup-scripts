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

class SystemPropertiesTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SystemPropertiesTest.class, ["scripts/systemProperties.groovy"])
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
