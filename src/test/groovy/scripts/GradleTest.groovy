package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class GradleTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(GradleTest.class, ["scripts/gradle.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin("gradle-1.25.hpi")
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureGradleInstallationsFromConfig() {
        def descriptor = jenkinsRule.jenkins.getDescriptor('hudson.plugins.gradle.GradleInstallation')
        assertThat(descriptor.getInstallations().length, equalTo(3))
        assertThat(descriptor.getInstallations()[0].getName(), equalTo('gradle-2'))
        assertThat(descriptor.getInstallations()[1].getName(), equalTo('gradle-3'))
        assertThat(descriptor.getInstallations()[2].getName(), equalTo('gradle-2-1'))
    }
}
