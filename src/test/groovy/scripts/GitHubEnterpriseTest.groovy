package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.containsString
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class GitHubEnterpriseTest {
    private static final List SCRIPTS = ["main.groovy", "scripts/githubEnterprise.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    static void setUp() {
        addScriptToLocalDataZip(GitHubEnterpriseTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["github-branch-source-2.3.2.hpi", "github-1.28.1.hpi", "credentials-2.1.16.hpi",
            "display-url-api-2.2.0.hpi", "git-3.7.0.hpi", "github-api-1.90.hpi", "scm-api-2.2.6.hpi",
            "structs-1.10.hpi", "git-client-2.7.0.hpi", "mailer-1.20.hpi", "matrix-project-1.12.hpi",
            "ssh-credentials-1.13.hpi", "apache-httpcomponents-client-4-api-4.5.3-2.1.hpi", "jsch-0.1.54.1.hpi",
            "junit-1.23.hpi", "script-security-1.40.hpi", "workflow-api-2.26.hpi", "workflow-step-api-2.14.hpi",
            "workflow-scm-step-2.6.hpi", "jackson2-api-2.8.10.1.hpi", "plain-credentials-1.4.hpi",
            "token-macro-2.3.hpi", "workflow-job-2.16.hpi", "workflow-support-2.16.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureGitHubEnterprise() {
        def nodejsConfig = new File(jenkinsRule.jenkins.root.getAbsoluteFile(), "org.jenkinsci.plugins.github_branch_source.GitHubConfiguration.xml").text
        assertThat(nodejsConfig, containsString("<apiUri>https://github.com/</apiUri>"))
        assertThat(nodejsConfig, containsString("<name>GitHub</name>"))
    }
}
