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

class JavaTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/java.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(JavaTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureJavaInstallationsFromConfig() {
        def jdk7 = jenkinsRule.jenkins.getJDK("jdk7")
        def jdk8 = jenkinsRule.jenkins.getJDK("jdk8")
        assertThat(jdk7.properties[0].tool.properties[0].installers.url[0] as String, equalTo("http://localhost/jdk-7u80-oth-JPR"))
        assertThat(jdk8.properties[0].tool.properties[0].installers.url[0] as String, equalTo("http://localhost/jdk-8u102-oth-JPR"))
    }
}
