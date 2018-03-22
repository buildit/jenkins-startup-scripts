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

class HashicorpVaultTest {

    private static
    final List SCRIPTS = ["main.groovy", "scripts/hashicorpvault.groovy", "scripts/credentials.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(HashicorpVaultTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @WithPlugin(["hashicorp-vault-plugin-2.1.0.hpi", "workflow-scm-step-2.6.hpi", "cloudbees-folder-6.1.0.hpi",
            "credentials-2.1.16.hpi", "workflow-step-api-2.14.hpi", "structs-1.10.hpi", "ssh-credentials-1.13.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureVaultDetailsFromConfig() {
        def vaultConfig = new File(jenkinsRule.jenkins.root.getAbsoluteFile(), "com.datapipe.jenkins.vault.configuration.GlobalVaultConfiguration.xml").text
        assertThat(vaultConfig, containsString("<vaultUrl>http://vault.server.url</vaultUrl>"))
        assertThat(vaultConfig, containsString("<vaultCredentialId>vault</vaultCredentialId>"))
    }
}
