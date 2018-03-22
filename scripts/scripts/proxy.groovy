instance.proxy = new hudson.ProxyConfiguration(
        "${config.host}" as String,
        "${config.port}" as Integer,
        "${config.userName}" as String,
        "${config.password}" as String,
        "${config.noProxyHost}" as String
)
instance.save()

println "Proxy settings updated!"