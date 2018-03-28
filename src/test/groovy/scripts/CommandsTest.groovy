package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.startsWith
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class CommandsTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(CommandsTest.class, ["scripts/commands.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldCreateFileInLocationWithContents() {
        def jenkinsHome = jenkinsRule.instance.getRootDir()
        def file = new File("${jenkinsHome}/spiderman")
        assertThat(file.text as String, startsWith("Peter Parker"))
    }
}
