import jenkins.model.Jenkins

def descriptor = Jenkins.getInstance().getDescriptor("envInject");

def envInjectGlobalPasswordEntries = []

config?.each { key, value ->
    println("Adding password ${key}")
    envInjectGlobalPasswordEntries.add(new org.jenkinsci.plugins.envinject.EnvInjectGlobalPasswordEntry(key, value))
}

descriptor.envInjectGlobalPasswordEntries = envInjectGlobalPasswordEntries
Jenkins.getInstance().save()
