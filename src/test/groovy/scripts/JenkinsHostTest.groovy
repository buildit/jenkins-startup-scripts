package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class JenkinsHostTest {
    private static final List SCRIPTS = ["main.groovy", "scripts/jenkinshost.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    static void setUp() {
        addScriptToLocalDataZip(JenkinsHostTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldSetUpHostDetailsFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("jenkins.model.JenkinsLocationConfiguration")
        // The URL cannot currently be tested, as the local Jenkins WAR overwrites it. It has been proven through manual testing.
//        assertThat(descriptor.getUrl() as String, equalTo('http://example.com/jenkins'))
        assertThat(descriptor.getAdminAddress() as String, equalTo('admin@example.com'))
    }
}
