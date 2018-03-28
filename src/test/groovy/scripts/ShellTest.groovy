package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class ShellTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(ShellTest.class, ["scripts/shell.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureShellFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("hudson.tasks.Shell");
        assertThat(descriptor.getShell() as String, equalTo("/bin/bash"))
    }
}
