import jenkins.plugins.publish_over_ssh.BapSshHostConfiguration

def publish_ssh = instance.getDescriptor("jenkins.plugins.publish_over_ssh.BapSshPublisherPlugin")

if (config) {
    def configuration = new BapSshHostConfiguration(
            config.name,
            config.hostname,
            config.username,
            config.encryptedPassword,
            config.remoteRootDir,
            config.port,
            config.timeout,
            config.overrideKey,
            config.keyPath,
            config.key,
            config.disableExec
    )
    publish_ssh.addHostConfiguration(configuration)
    publish_ssh.save()
}