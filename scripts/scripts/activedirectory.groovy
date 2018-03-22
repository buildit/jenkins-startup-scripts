import hudson.plugins.active_directory.ActiveDirectoryDomain
import hudson.plugins.active_directory.ActiveDirectorySecurityRealm
import hudson.plugins.active_directory.GroupLookupStrategy

def adDomain = new ActiveDirectoryDomain(config.domain, config.server)
def realm = new ActiveDirectorySecurityRealm(config.domain, Collections.singletonList(adDomain),
        config.site, config.bindName, config.bindPassword, config.server, GroupLookupStrategy.AUTO, false, true, null)
instance.setSecurityRealm(realm)
instance.save()



