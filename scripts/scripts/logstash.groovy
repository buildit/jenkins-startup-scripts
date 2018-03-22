import jenkins.plugins.logstash.LogstashInstallation

def descriptor = LogstashInstallation.getLogstashDescriptor()
descriptor.type = config.type
descriptor.host = config.host
descriptor.port = config.port
descriptor.key = config.key
descriptor.save()
