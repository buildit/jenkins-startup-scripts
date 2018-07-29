import hudson.security.*
import jenkins.model.Jenkins


def authStrategy = config.authStrategy
println "Authorization strategy is: ${authStrategy}"

def instance = Jenkins.getInstance()

switch (authStrategy) {
    case 'Logged-in users can do anything':
        def allowAnonymousRead = config?.allowAnonymousRead ?: false
        setDefaultStrategy(instance, allowAnonymousRead)
        break
    case 'Matrix-based security':
        setMatrixStrategy(instance, config.matrix)
        break
    default:
        throw new RuntimeException("Authorization strategy ${authStrategy} not supported.")

}

def setMatrixStrategy(instance, matrix) {
    def strategy = new GlobalMatrixAuthorizationStrategy()
    matrix.each { role, entity ->
        strategy.add(Permission.fromId(role), entity)
    }
    instance.setAuthorizationStrategy(strategy)
    instance.save()
}

def setDefaultStrategy(instance, allowAnonymousRead) {
    def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
    strategy.setAllowAnonymousRead(allowAnonymousRead)
    instance.setAuthorizationStrategy(strategy)
    instance.save()
}
