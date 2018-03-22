import com.datapipe.jenkins.vault.configuration.GlobalVaultConfiguration
import com.datapipe.jenkins.vault.configuration.VaultConfiguration
import jenkins.model.GlobalConfiguration

if (config) {
    GlobalVaultConfiguration globalConfig = GlobalConfiguration.all().get(GlobalVaultConfiguration.class)
    globalConfig.setConfiguration(new VaultConfiguration(config.url, config.credentialsId))

    globalConfig.save()
}
