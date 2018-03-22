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

class ConfluenceTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/confluence.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(ConfluenceTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin("confluence-publisher-1.8.hpi")
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigurePublishToConfluenceFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("com.myyearbook.hudson.plugins.confluence.ConfluencePublisher");
        def site = descriptor.getSites()[0]
        assertThat(site.url.getHost() as String, equalTo("confluence.sandbox.extranet.group"))
        assertThat(site.username as String, equalTo("arvind.kumar"))
        assertThat(site.password as String, equalTo("somes3cret"))

    }
}
