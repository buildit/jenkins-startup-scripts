import org.jenkinsci.plugins.saml.*
import jenkins.model.Jenkins

def idpMetadataConfig = new IdpMetadataConfiguration(config.idpMetadataXml ?: null, config.idpMetadataUrl ?: null, Long.parseLong(config.refreshPeriod))
def realm = new SamlSecurityRealm(idpMetadataConfig,
                                  config.displayNameAttribute ?: '',
                                  config.groupAttribute ?: '',
                                  config.maximumAuthLifetime ?: 86400, // 24h default
                                  config.usernameAttribute ?: '',
                                  config.emailAttribute ?: '',
                                  config.logoutUrl ?: '',
                                  null,
                                  null,
                                  config.usernameCaseConversion ?: '',
                                  config.dataBindingMethod ?: '',
                                  null)

def instance = Jenkins.getInstance()
instance.setSecurityRealm(realm)
instance.save()
