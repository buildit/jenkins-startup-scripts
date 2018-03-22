package scripts

import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.tuple
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class ConfigFilesTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/configfiles.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(ConfigFilesTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["config-file-provider-2.15.7.hpi", "credentials-2.1.16.hpi", "ssh-credentials-1.13.hpi", "structs-1.10.hpi", "token-macro-2.3.hpi",
    "workflow-job-2.16.hpi", "workflow-step-api-2.14.hpi", "workflow-api-2.26.hpi", "workflow-support-2.16.hpi", "scm-api-2.2.6.hpi", "script-security-1.40.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureGlobalMavenSettingsFromConfig() {
        def provider = Jenkins.instance.getExtensionList('org.jenkinsci.plugins.configfiles.GlobalConfigFiles')[0]
        def configs = provider.getConfigs()

        assertThat(configs.size()).isEqualTo(1)
        assertThat(configs).extracting("id", "name", "comment", "content", "isReplaceAll")
                .contains(tuple("id1", "name1", "comment1", "<settings>1</settings>", true))

        def credentials = configs.getAt(0).getServerCredentialMappings()
        assertThat(credentials.size()).isEqualTo(2)

        assertThat(credentials).extracting("serverId", "credentialsId")
                .contains(tuple("server1", "credentialsId1"),
                tuple("server2", "credentialsId2"))
    }
}
