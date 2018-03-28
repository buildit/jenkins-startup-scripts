package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class GitlabTest  extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(GitlabTest.class, ["scripts/gitlab.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["gitlab-plugin-1.4.8.hpi", "credentials-2.1.16.hpi", "plain-credentials-1.4.hpi",
            "git-3.7.0.hpi", "git-client-2.7.0.hpi", "matrix-project-1.12.hpi", "workflow-step-api-2.14.hpi",
            "ssh-credentials-1.13.hpi", "junit-1.23.hpi", "script-security-1.40.hpi", "mailer-1.20.hpi", "scm-api-2.2.6.hpi",
            "workflow-scm-step-2.6.hpi", "structs-1.10.hpi", "apache-httpcomponents-client-4-api-4.5.3-2.1.hpi", "jsch-0.1.54.1.hpi",
            "display-url-api-2.2.0.hpi", "workflow-api-2.26.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureGerritServerFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("com.dabsquared.gitlabjenkins.connection.GitLabConnectionConfig")

        def gitLabConnection = getConnectionByName("gitlab", descriptor.connections)
        assertThat(gitLabConnection.name as String, equalTo('gitlab'))
        assertThat(gitLabConnection.url as String, equalTo('http://gitlab.platform.com/'))
        assertThat(gitLabConnection.apiTokenId as String, equalTo('gitlabCredentials'))
        assertThat(gitLabConnection.ignoreCertificateErrors, equalTo(false))
        assertThat(gitLabConnection.connectionTimeout as Integer, equalTo(15))
        assertThat(gitLabConnection.readTimeout as Integer, equalTo(15))

        def gitlabUsingDefaults = getConnectionByName("gitlabUsingDefaults", descriptor.connections)
        assertThat(gitlabUsingDefaults.name as String, equalTo('gitlabUsingDefaults'))
        assertThat(gitlabUsingDefaults.url as String, equalTo('http://gitlab.platform.com/'))
        assertThat(gitlabUsingDefaults.apiTokenId as String, equalTo('gitlabCredentials'))
        assertThat(gitlabUsingDefaults.ignoreCertificateErrors as Boolean, equalTo(true))
        assertThat(gitlabUsingDefaults.connectionTimeout as Integer, equalTo(10))
        assertThat(gitlabUsingDefaults.readTimeout as Integer, equalTo(10))
    }

    def getConnectionByName(name, connections) {
        for (def connection : connections) {
            if (connection.name == name) {
                return connection
            }
        }
    }
}
