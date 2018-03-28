package scripts

import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class MailTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(MailTest.class, ["scripts/mail.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureMailFromConfig() {
        def settings = Jenkins.instance.getDescriptor("hudson.tasks.Mailer")
        assertThat(settings.smtpHost as String, equalTo("159.34.192.34"))
        assertThat(settings.defaultSuffix as String, equalTo("@somewhere.com"))
    }
}
