package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class PasswordsTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(PasswordsTest.class, ["scripts/passwords.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["envinject-1.91.3.hpi", "maven-plugin-3.1.2.hpi", "mailer-1.20.hpi", "javadoc-1.1.hpi", "junit-1.23.hpi",
            "display-url-api-2.2.0.hpi", "workflow-step-api-2.14.hpi", "script-security-1.44.hpi", "structs-1.14.hpi",
            "workflow-api-2.27.hpi", "scm-api-2.2.6.hpi","apache-httpcomponents-client-4-api-4.5.3-2.1.hpi","jsch-0.1.54.1.hpi",
            "ssh-credentials-1.13.hpi","credentials-2.1.16.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigurePasswordsFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("envInject");
        def entries = descriptor.envInjectGlobalPasswordEntries
        assertThat(entries.size() as Integer, equalTo(1))
        assertThat(entries[0].name as String, equalTo("repository"))
        assertThat(entries[0].value as String, equalTo("somes3cret"))
    }
}
