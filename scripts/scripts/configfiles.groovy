import org.jenkinsci.plugins.configfiles.maven.*
import org.jenkinsci.plugins.configfiles.maven.security.*

def store = instance.getExtensionList('org.jenkinsci.plugins.configfiles.GlobalConfigFiles')[0]

config.each { key, config ->
    println("Adding ${key} to global maven settings")
    def serverCreds = new ArrayList()

    config.servers.each { server ->
        serverCredentialMapping = new ServerCredentialMapping(server.serverId, server.credentialsId)
        serverCreds.add(serverCredentialMapping)
    }

    def globalConfig = new GlobalMavenSettingsConfig(config.id, config.name, config.comment, config.content, Boolean.parseBoolean(config.replaceAll), serverCreds)
    store.save(globalConfig)
}