package scripts

import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class JnlpTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/jnlp.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    private static final String PORT_FROM_ENV = "6000"
    private static final String PORT_FROM_CONFIG = "5000"


    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(JnlpTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
        System.metaClass.static.getenv = { String key ->
            return [JNLP_PORT: PORT_FROM_ENV, TEST_CONFIG_FILE: "jenkinsConfigWithPortSet.config,jenkinsConfigWithPortNotSet.config"].get(key)
        }
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkinsConfigWithPortSet.config"])
    void shouldConfigurePortFromConfig() {
        def expected = Integer.parseInt(PORT_FROM_CONFIG)
        def actual = Jenkins.instance.getSlaveAgentPort()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkinsConfigWithPortNotSet.config"])
    void shouldConfigurePortFromEnv() {
        def expected = Integer.parseInt(PORT_FROM_ENV)
        def actual = Jenkins.instance.getSlaveAgentPort()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkinsConfigWithPortSet.config"])
    void shouldConfigurePortFromConfigWhenBothConfigAndEnvSet() {
        def expected = Integer.parseInt(PORT_FROM_CONFIG)
        def actual = Jenkins.instance.getSlaveAgentPort()

        assertThat(actual).isEqualTo(expected)
    }

}
