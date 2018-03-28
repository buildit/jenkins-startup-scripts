package scripts

import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class ProxyTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(ProxyTest.class, ["scripts/proxy.groovy"])
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
