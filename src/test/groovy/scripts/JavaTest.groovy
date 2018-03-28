package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class JavaTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(JavaTest.class, ["scripts/java.groovy"])
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
