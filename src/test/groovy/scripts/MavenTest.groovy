package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static hudson.tasks.Maven.DescriptorImpl
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class MavenTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(MavenTest.class, ["scripts/maven.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureMavenInstallationsFromConfig() {
        def extensions = jenkinsRule.jenkins.getExtensionList(DescriptorImpl.class)
        assertThat(extensions.installations[0].length, equalTo(2))
        assertThat(extensions.installations[0][0].name, equalTo("maven-3.3.3"))
        assertThat(extensions.installations[0][0].properties[0].installers[0].url, equalTo("http://test_url"))
        assertThat(extensions.installations[0][1].name, equalTo("maven-3.3.9"))
    }
}
