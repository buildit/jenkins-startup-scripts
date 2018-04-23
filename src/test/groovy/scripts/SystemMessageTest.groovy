package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
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
    @WithPlugin(["antisamy-markup-formatter-1.5.hpi"])
    void shouldConfigureSystemMessageFromConfig() {
        def descriptor = jenkinsRule.instance.getSystemMessage();
        assertEquals("<b>Testing System Message</b>", descriptor)
    }
}
