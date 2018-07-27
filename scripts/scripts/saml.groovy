import org.jenkinsci.plugins.saml.*
import jenkins.model.Jenkins

def idpMetadataConfig = new IdpMetadataConfiguration(null, config.idpMetadataUrl, Long.parseLong(config.refreshPeriod))
def realm = new SamlSecurityRealm(idpMetadataConfig,
                                  config.displayNameAttribute,
                                  config.groupAttribute,
                                  config.maximumAuthLifetime,
                                  config.usernameAttribute,
                                  config.emailAttribute,
                                  config.logoutUrl,
                                  null,
                                  null,
                                  config.usernameCaseConversion,
                                  config.dataBindingMethod,
                                  null)

def instance = Jenkins.getInstance()
instance.setSecurityRealm(realm)
instance.save()
