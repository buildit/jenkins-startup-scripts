package scripts

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

class SlackTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SlackTest.class, ["scripts/credentials.groovy", "scripts/slack.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin([
            "credentials-2.1.16.hpi",
            "display-url-api-2.2.0.hpi", "junit-1.23.hpi",
            "plain-credentials-1.4.hpi", "scm-api-2.2.6.hpi", "script-security-1.44.hpi",
            "slack-2.3.hpi", "structs-1.14.hpi",
            "workflow-api-2.27.hpi", "workflow-step-api-2.14.hpi"
    ])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureSlackFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor('jenkins.plugins.slack.SlackNotifier')
        assertThat(descriptor.getTeamDomain(), equalTo('buildit'))
        assertThat(descriptor.getTokenCredentialId(), equalTo('authToken'))
        assertThat(descriptor.getBotUser(), equalTo(false))
        assertThat(descriptor.getRoom(), equalTo(""))
    }
}
