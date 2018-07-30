package scripts

import hudson.util.Secret
import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class CredentialsTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(CredentialsTest.class, ["scripts/credentials.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["azure-credentials-1.6.0.hpi", "hashicorp-vault-plugin-2.1.0.hpi", "hashicorp-vault-credentials-plugin-0.0.9.hpi", "credentials-binding-1.16.hpi",
            "credentials-2.1.16.hpi", "workflow-api-2.27.hpi", "workflow-step-api-2.14.hpi", "structs-1.14.hpi", "plain-credentials-1.4.hpi",
            "ssh-credentials-1.13.hpi", "sauce-ondemand-1.164.hpi", "maven-plugin-3.1.2.hpi", "matrix-project-1.12.hpi",
            "workflow-basic-steps-2.5.hpi", "run-condition-1.0.hpi", "workflow-cps-2.53.hpi", "junit-1.23.hpi", "workflow-job-2.21.hpi",
            "script-security-1.44.hpi", "javadoc-1.1.hpi", "token-macro-2.3.hpi",
            "workflow-scm-step-2.6.hpi", "workflow-support-2.18.hpi", "ace-editor-1.0.1.hpi", "jquery-detached-1.2.1.hpi",
            "scm-api-2.2.6.hpi", "gitlab-plugin-1.4.8.hpi", "git-3.7.0.hpi", "git-client-2.7.0.hpi",
            "cloudbees-folder-6.4.hpi", "apache-httpcomponents-client-4-api-4.5.3-2.1.hpi", "jsch-0.1.54.1.hpi", "display-url-api-2.2.0.hpi",
            "mailer-1.20.hpi","windows-azure-storage-0.3.9.hpi", "azure-commons-0.2.6.hpi","copyartifact-1.40.hpi","blueocean-rest-1.5.0.hpi",
            "blueocean-commons-1.5.0.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureCredentialsFromConfig() {

        def usernamePasswordCredentials = getCredentialsOfType("UsernamePasswordCredentialsImpl")
        assertThat(usernamePasswordCredentials[0].getUsername() as String, equalTo("repository"))
        assertThat(usernamePasswordCredentials[0].getPassword() as String, equalTo("somes3cret"))
        assertThat(usernamePasswordCredentials[1].getUsername() as String, equalTo("cloud"))
        assertThat(usernamePasswordCredentials[1].getPassword() as String, equalTo("cl0ud"))
        assertThat(usernamePasswordCredentials[2].getId() as String, equalTo("somewhere-cool"))
        assertThat(usernamePasswordCredentials[2].getUsername() as String, equalTo("somewhere"))
        assertThat(usernamePasswordCredentials[2].getPassword() as String, equalTo("somes3cret"))

        def basicSSHUserPrivateKey = getCredentialsOfType("BasicSSHUserPrivateKey")
        assertThat(basicSSHUserPrivateKey[0].getUsername() as String, equalTo("ssh"))
        assertThat(basicSSHUserPrivateKey[0].getPassphrase() as String, equalTo("cl0ud"))
        assertThat(basicSSHUserPrivateKey[0].getPrivateKeySource().getPrivateKeyFile() as String, equalTo(".ssh/id_rsa"))

        def credentials = getCredentialsOfType("HashicorpVaultCredentialsImpl")
        assertThat(credentials[0].getKey() as String, equalTo("super/secret"))
        assertThat(credentials[0].getUsernameKey() as String, equalTo("username"))
        assertThat(credentials[0].getPasswordKey() as String, equalTo("password"))
        assertThat(credentials[0].getDescription() as String, equalTo("vault credentials"))

        def sauceLabsCredentials = getCredentialsOfType("SauceCredentials")
        assertThat(sauceLabsCredentials[0].getUsername() as String, equalTo("slUser"))
        assertThat(sauceLabsCredentials[0].getApiKey() as String, equalTo("slApiKey"))
        assertThat(sauceLabsCredentials[0].getDescription() as String, equalTo("SauceLabs credentials"))

        def gitlabCredentials = getCredentialsOfType("GitLabApiToken")
        assertThat(gitlabCredentials[0].getApiToken() as String, equalTo("somes3cret"))
        assertThat(gitlabCredentials[0].getDescription() as String, equalTo("Gitlab credentials"))

        def stringCredentials = getCredentialsOfType("StringCredential")
        assertThat(stringCredentials[0].getSecret() as String, equalTo("somestring"))
        assertThat(stringCredentials[0].getDescription() as String, equalTo("auth token"))

        def azureCredentials = getCredentialsOfType("com.microsoft.azure.util.AzureCredentials")
        assertThat(azureCredentials[0].getId() as String, equalTo("azure-sp-id"))
        assertThat(azureCredentials[0].getDescription() as String, equalTo("azure SP"))
        assertThat(azureCredentials[0].getSubscriptionId() as String, equalTo("aaaaa-bbbbb-ccccc"))
        assertThat(azureCredentials[0].getClientId() as String, equalTo("ddddd-eeeee-fffff"))
        assertThat(Secret.decrypt(azureCredentials[0].getClientSecret()) as String, equalTo("asadfasfasdfjk"))
        assertThat(azureCredentials[0].getTenant() as String, equalTo("ggggg-hhhhh-iiiii"))
        assertThat(azureCredentials[0].getAzureEnvironmentName() as String, equalTo("Azure"))

        def azureSecretString = getCredentialsOfType("SecretStringCredentials")
        assertThat(azureSecretString[0].getId() as String, equalTo("secret-id"))
        assertThat(azureSecretString[0].getDescription() as String, equalTo("secret-description"))
        assertThat(azureSecretString[0].getServicePrincipalId() as String, equalTo("azure-sp-id"))
        assertThat(azureSecretString[0].getSecretIdentifier() as String, equalTo("https://mysecret"))

        def azureStorageAccount = getCredentialsOfType("com.microsoftopentechnologies.windowsazurestorage.helper.AzureCredentials")
        assertThat(azureStorageAccount[0].getId() as String, equalTo("az-storage-creds-id"))
        assertThat(azureStorageAccount[0].getDescription() as String, equalTo("az storage credentials"))
        assertThat(azureStorageAccount[0].getStorageAccountName() as String, equalTo("testsa"))
        assertThat(Secret.decrypt(azureStorageAccount[0].getStorageKey()) as String, equalTo("somestoragekey"))
        assertThat(azureStorageAccount[0].getBlobEndpointURL() as String, equalTo("https://testsa.blob.core.windows.net"))
    }

    private getCredentialsOfType(String type) {
        def provider = Jenkins.instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0]
        provider.getCredentials().findAll {
            it.class.toString().contains(type)
        }
    }
}
