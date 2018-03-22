import jenkins.model.Jenkins

def instance = Jenkins.getInstance()

def portFromEnv = System.getenv("JNLP_PORT")

if (config?.jnlp?.port) {
    println("Setting jnlp port from config to ${config.jnlp.port}")

    instance.setSlaveAgentPort(config.jnlp.port)
    instance.save()

} else if (portFromEnv) {
    println("Setting jnlp port from env to ${portFromEnv}")

    instance.setSlaveAgentPort(Integer.parseInt(portFromEnv))
    instance.save()
}

if (portFromEnv && config?.jnlp?.port) {
    println("Warning: JNLP port set in ENV and Config file. Env: ${portFromEnv}, Config: ${config?.jnlp?.port}. Just so you know - Config always wins :-)")
}


