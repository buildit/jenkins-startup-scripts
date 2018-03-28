package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.containsString
import static org.junit.Assert.assertThat

class NodeJsTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(NodeJsTest.class, ["scripts/nodejs.groovy"])
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
