package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static junit.framework.TestCase.assertTrue
import static org.hamcrest.Matchers.equalTo
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertThat

class JiraTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(JiraTest.class, ["scripts/jira.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["jira-2.2.1.hpi", "matrix-project-1.12.hpi", "mailer-1.20.hpi", "junit-1.23.hpi", "script-security-1.44.hpi",
            "structs-1.14.hpi", "workflow-step-api-2.14.hpi", "workflow-api-2.27.hpi", "junit-1.23.hpi", "scm-api-2.2.6.hpi",
            "display-url-api-2.2.0.hpi", "mailer-1.20.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureJiraSitesWhenAvailable() {
        def plugin = jenkinsRule.jenkins.getPlugin('jira')
        plugin.load()

        def descriptor = jenkinsRule.instance.getDescriptor('hudson.plugins.jira.JiraProjectProperty')
        def sites = descriptor.getSites()

        assertThat(sites.size() as int, equalTo(2))

        assertThat(sites[0].url as String, equalTo('https://jira.foo.com/'))
        assertThat(sites[0].alternativeUrl as String, equalTo('https://jira.foo.com/'))
        assertThat(sites[0].userName as String, equalTo('admin'))
        assertThat(sites[0].password as String, equalTo('secret'))
        assertFalse(sites[0].supportsWikiStyleComment as boolean)
        assertFalse(sites[0].recordScmChanges as boolean)
        assertFalse(sites[0].updateJiraIssueForAllStatus as boolean)
        assertThat(sites[0].userPattern, equalTo(null))
        assertFalse(sites[0].useHTTPAuth as boolean)
        assertThat(sites[0].groupVisibility, equalTo(null))
        assertThat(sites[0].roleVisibility, equalTo(null))

        assertThat(sites[1].url as String, equalTo('http://jira.bar.com/'))
        assertThat(sites[1].alternativeUrl as String, equalTo('http://jira.bar.com/'))
        assertThat(sites[1].userName as String, equalTo('user'))
        assertThat(sites[1].password as String, equalTo('p@ss'))
        assertTrue(sites[1].supportsWikiStyleComment as boolean)
        assertTrue(sites[1].recordScmChanges as boolean)
        assertTrue(sites[1].updateJiraIssueForAllStatus as boolean)
        assertThat(sites[1].userPattern as String, equalTo('5'))
        assertTrue(sites[1].useHTTPAuth as boolean)
        assertThat(sites[1].groupVisibility as String, equalTo('6'))
        assertThat(sites[1].roleVisibility as String, equalTo('7'))
    }
}
