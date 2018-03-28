package scripts

import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.Matchers.hasItems
import static org.junit.Assert.assertThat

class EnvTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(EnvTest.class, ["scripts/env.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureEnvVariablesFromConfig() {
        def envVarsNodePropertyList = Jenkins.instance.globalNodeProperties.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)
        def envVars = envVarsNodePropertyList[0].envVars
        assertThat(envVars.keySet(), hasItems("url"))
        assertThat(envVars.values(), hasItems("http://www.bbc.co.uk"))
    }
}
