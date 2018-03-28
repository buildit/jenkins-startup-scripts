package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class CustomToolTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(CustomToolTest.class, ["scripts/customtool.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["custom-tools-plugin-0.4.4.hpi", "extended-choice-parameter-0.28.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureCustomToolInstallationsFromConfig() {
        def descriptor = jenkinsRule.jenkins.getDescriptor('com.cloudbees.jenkins.plugins.customtools.CustomTool')
        assertThat(descriptor.getInstallations().length as Integer, equalTo(1))
        assertThat(descriptor.getInstallations()[0].getName() as String, equalTo('cf'))
    }

}
