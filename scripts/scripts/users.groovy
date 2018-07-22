import hudson.security.FullControlOnceLoggedInAuthorizationStrategy
import hudson.security.HudsonPrivateSecurityRealm
import jenkins.model.Jenkins


def users

// Leave open to adding other types of users
config.each { type, userMap ->
    if (type == 'users') {
        println "Found users"
        users = userMap
    }
    else {
        println "User type ${type} not recognised, ignoring config..."
    }
}

if (users) {

    println "creating private security realm..."

    def instance = Jenkins.getInstance()
    def realm = new HudsonPrivateSecurityRealm(false, false, null)
    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()

    strategy.setAllowAnonymousRead(false)
    instance.setSecurityRealm(realm)
    instance.setAuthorizationStrategy(strategy)

    users.each {username, password ->
        realm.createAccount(username, password)
    }

    instance.save()
}
else {
    println "No users configured"
}
