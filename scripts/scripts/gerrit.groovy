import com.sonyericsson.hudson.plugins.gerrit.trigger.*
import net.sf.json.*;

def plugin = instance.getPlugin('gerrit-trigger')

if (config) {
    println('Configuring Gerrit Trigger')

    removeAllGerritServers(plugin)
    addGerritServersFromConfig(plugin, config)

    plugin.save()
}

private removeAllGerritServers(plugin) {
    def servers = plugin.getServers()
    servers.each {
        plugin.removeServer(it)
    }
}

private addGerritServersFromConfig(plugin, config) {
    config?.each { server ->
        println("Adding Gerrit server with name ${server.name}")

        GerritServer gerritServer = new GerritServer(server.name)
        def gerritConfig = gerritServer.getConfig()
        server.gerritAuthKeyFile = "${server.gerritAuthKeyFile}".toString()
        gerritConfig.setValues(JSONObject.fromObject(server))
        gerritServer.setConfig(gerritConfig)
        plugin.addServer(gerritServer)
        gerritServer.start()
        gerritServer.startConnection()
    }
}
