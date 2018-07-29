import hudson.security.HudsonPrivateSecurityRealm
import jenkins.model.Jenkins

// need to start at the root of the config so we can check for security config as well
if (config.users.accounts) {

    if (!config.security) {
        throw new RuntimeException("You MUST have a security block configured if you are configuring users.")
    }

    def instance = Jenkins.getInstance()
    def realm = new HudsonPrivateSecurityRealm(false, false, null)
    instance.setSecurityRealm(realm)

    config.users.accounts.each {username, password ->
        realm.createAccount(username, password)
    }

    instance.save()
}


