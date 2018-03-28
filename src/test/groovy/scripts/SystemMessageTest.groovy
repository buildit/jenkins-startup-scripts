package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.junit.Assert.assertEquals
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class SystemMessageTest  extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SystemMessageTest.class, ["scripts/systemMessage.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureSystemMessageFromConfig() {
        def descriptor = jenkinsRule.instance.getSystemMessage();
        assertEquals("<b>Testing System Message</b>", descriptor)
    }
}
