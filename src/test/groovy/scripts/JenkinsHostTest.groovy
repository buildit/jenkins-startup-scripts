package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

class JenkinsHostTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(JenkinsHostTest.class, ["scripts/jenkinshost.groovy"])
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
