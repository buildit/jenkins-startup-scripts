package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class SplunkTest {
    private static final List SCRIPTS = ["main.groovy", "scripts/splunk.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    static void setUp() {
        addScriptToLocalDataZip(SplunkTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["splunk-devops-1.6.0.hpi", "script-security-1.40.hpi"])
    void allValuesPopulated() {
        def config = jenkinsRule.instance.getExtensionList('com.splunk.splunkjenkins.SplunkJenkinsInstallation')[0]

        // For testing in UI
        //Thread.sleep(100000000)

        assertThat(config.isEnabled()).isEqualTo(true)
        assertThat(config.getHost()).isEqualTo("http://splunk.server.url")
        assertThat(config.getToken()).isEqualTo("666")
        assertThat(config.isUseSSL()).isEqualTo(true)
        assertThat(config.getMetadataHost()).isEqualTo("jenkins.savings.com")
        assertThat(config.isRawEventEnabled()).isEqualTo(true)
        assertThat(config.getMetadataSource()).isEqualTo("jenkins")
        assertThat(config.getSplunkAppUrl()).isEqualTo("http://splunk.savings.com/en-GB/app/splunk_app_jenkins/overview/")
        assertThat(config.getMaxEventsBatchSize()).isEqualTo(666666)
        assertThat(config.getRetriesOnError()).isEqualTo(6)
        assertThat(config.getIgnoredJobs()).isEqualTo("ignore")
        assertThat(config.getScriptPath()).isNull()
        assertThat(config.getScriptContent()).isEqualTo("splunkins.sendTestReport(50)\nsplunkins.sendCoverageReport(50)")
        def metadataItems = config.metadataItemSet
        assertThat(metadataItems.size()).isEqualTo(1)
        assertThat(metadataItems[0].dataSource).isEqualTo("BUILD_EVENT")
        assertThat(metadataItems[0].keyName).isEqualTo("index")
        assertThat(metadataItems[0].value).isEqualTo("poc123")
    }
}
