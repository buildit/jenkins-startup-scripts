import hudson.model.Node
import hudson.plugins.sshslaves.SSHLauncher
import hudson.slaves.*
import hudson.slaves.EnvironmentVariablesNodeProperty.Entry

removeAllNodes(instance)

config?.each { key, node ->
    println("Adding node ${key}")

    def env = node?.env?.collect { k, v -> new Entry(k, v) }
    def slave = new DumbSlave(
            node.name as String, node.description as String, node.remoteFS as String,
            node.numExecutors as String, mode(node.mode), node.label as String,
            launcher(node.launchType, node.host, node.port as Integer, node.jnlpTunnel, node.jnlpVmArgs, node.credentialsId, node.javaPath),
            RetentionStrategy.INSTANCE)
    slave.getNodeProperties().add(new EnvironmentVariablesNodeProperty(env))
    instance.addNode(slave)
}

instance.save()

private def removeAllNodes(instance) {
    instance.getNodes().each { node ->
        println("Removing node with name ${node.name}")
        instance.removeNode(node)
    }
}

private Node.Mode mode(mode) {
    switch (mode) {
        case 'EXCLUSIVE':
            return Node.Mode.EXCLUSIVE
        case 'NORMAL':
            return Node.Mode.NORMAL
        default:
            return Node.Mode.NORMAL
    }
}

private ComputerLauncher launcher(launchType, host, port, jnlpTunnel, jnlpVmArgs, credentialsId, javaPath) {
    switch (launchType) {
        case 'SSH':
            return new SSHLauncher(host, port ?: 22, credentialsId, '', javaPath, '', '')
        case 'JNLP':
            return new JNLPLauncher(jnlpTunnel, jnlpVmArgs)
        default:
            return new JNLPLauncher(jnlpTunnel, jnlpVmArgs)
    }
}
