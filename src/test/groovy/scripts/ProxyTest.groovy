package scripts

import jenkins.model.Jenkins
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

class ProxyTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/proxy.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(ProxyTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureProxyFromConfig() {
        def settings = Jenkins.instance.proxy
        assertThat(settings.name as String, equalTo("proxy.sandbox.local"))
        assertThat(settings.port as Integer, equalTo(3128))
        assertThat(settings.noProxyHost as String, equalTo("localhost,127.0.0.1"))
        assertThat(settings.userName as String, equalTo("spiderman"))
        assertThat(settings.password as String, equalTo("p3t3rPark3r"))
    }
}
