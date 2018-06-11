package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.containsString
import static org.junit.Assert.assertThat

class HashicorpVaultTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(HashicorpVaultTest.class, ["scripts/hashicorpvault.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["hashicorp-vault-plugin-2.1.0.hpi", "workflow-scm-step-2.6.hpi", "cloudbees-folder-6.4.hpi",
            "credentials-2.1.16.hpi", "workflow-step-api-2.14.hpi", "structs-1.14.hpi", "ssh-credentials-1.13.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureVaultDetailsFromConfig() {
        def vaultConfig = new File(jenkinsRule.jenkins.root.getAbsoluteFile(), "com.datapipe.jenkins.vault.configuration.GlobalVaultConfiguration.xml").text
        assertThat(vaultConfig, containsString("<vaultUrl>http://vault.server.url</vaultUrl>"))
        assertThat(vaultConfig, containsString("<vaultCredentialId>vault</vaultCredentialId>"))
    }
}
