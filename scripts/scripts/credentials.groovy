import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import hudson.util.Secret
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl

def domain = Domain.global()
def store = instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

removeAllCredentials(store, domain)

config.each { key, credentials ->
    println("Adding ${key} to global credentials")
    def user = user(credentials, key)
    store.addCredentials(domain, user)
}

private removeAllCredentials(store, domain) {
    store.getCredentials(domain).each {
        println("Removing ${it.id} from global credentials")
        store.removeCredentials(domain, it)
    }
}

private user(credentials, key) {
    // Credentials whose classes rely on a plugin must not be imported or called explicitly, else this whole script will fail on instances
    // that don't have the plugin installed. The constructor is obtained using reflection, instead.
    switch (credentials.type) {
        case 'SSH':
            return new BasicSSHUserPrivateKey(CredentialsScope.GLOBAL, key, credentials.username,
                    new BasicSSHUserPrivateKey.FileOnMasterPrivateKeySource(credentials.privateKeyFile),
                    credentials.password, credentials.description)
        case 'HashicorpVault':
            def constructor = Class.forName("digital.buildit.jenkins.credentials.vault.HashicorpVaultCredentialsImpl").getConstructor(CredentialsScope.class, String.class, String.class, String.class, String.class, String.class)
            return constructor.newInstance(CredentialsScope.GLOBAL, key, credentials.key, credentials.passwordKey, credentials.usernameKey, credentials.description)
        case 'HashicorpVaultTokenCredential':
            def constructor = Class.forName("com.datapipe.jenkins.vault.credentials.VaultTokenCredential").getConstructor(CredentialsScope.class, String.class, String.class, Secret.class)
            return constructor.newInstance(CredentialsScope.GLOBAL, key, credentials.description, new Secret(credentials.token))
        case 'SauceLabs':
            def constructor = Class.forName("hudson.plugins.sauce_ondemand.credentials.SauceCredentials").getConstructor(CredentialsScope.class, String.class, String.class, String.class, String.class)
            return constructor.newInstance(CredentialsScope.GLOBAL, key, credentials.username, credentials.apiKey, credentials.description)
        case 'GitLabApiToken':
            def constructor = Class.forName("com.dabsquared.gitlabjenkins.connection.GitLabApiTokenImpl").getConstructor(CredentialsScope.class, String.class, String.class, Secret.class)
            return constructor.newInstance(CredentialsScope.GLOBAL, key, credentials.description, new Secret(credentials.token))
        case 'StringCredential':
            return new StringCredentialsImpl(CredentialsScope.GLOBAL, key, credentials.description, new Secret(credentials.token))
        case 'AzureServicePrincipal':
            def constructor = Class.forName("com.microsoft.azure.util.AzureCredentials").getConstructor(CredentialsScope.class, String.class, String.class, String.class, String.class, String.class)
            def azureServicePrincipal = constructor.newInstance(CredentialsScope.GLOBAL, credentials.id, credentials.description, credentials.subscriptionId, credentials.clientId, credentials.clientSecret)
            azureServicePrincipal.setTenant(credentials.tenantId)
            azureServicePrincipal.setAzureEnvironmentName(credentials.azureEnvironment)
            return azureServicePrincipal
        case 'SecretStringCredentials':
            def constructor = Class.forName("com.microsoft.jenkins.keyvault.SecretStringCredentials").getConstructor(CredentialsScope.class, String.class, String.class, String.class, String.class)
            return constructor.newInstance(CredentialsScope.GLOBAL, credentials.id, credentials.description, credentials.servicePrincipalId, credentials.secretIdentifier)
        case 'AzureStorageAccount':
            def constructor = Class.forName("com.microsoftopentechnologies.windowsazurestorage.helper.AzureCredentials").getConstructor(CredentialsScope.class, String.class, String.class, String.class, String.class, String.class)
            return constructor.newInstance(CredentialsScope.GLOBAL, credentials.id, credentials.description, credentials.storageAccountName, credentials.storageKey, credentials.endpointUrl)
        default:
            return new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, credentials.id ?: key, credentials.description, credentials.username, credentials.password)
    }
}