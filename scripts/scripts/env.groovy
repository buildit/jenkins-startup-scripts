import jenkins.model.Jenkins

def instance = Jenkins.getInstance()
def globalNodeProperties = instance.getGlobalNodeProperties()
def envVarsNodePropertyList = globalNodeProperties.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)

def envVars = null

if (envVarsNodePropertyList == null || envVarsNodePropertyList.size() == 0) {
    def newEnvVarsNodeProperty = new hudson.slaves.EnvironmentVariablesNodeProperty();
    globalNodeProperties.add(newEnvVarsNodeProperty)
    envVars = newEnvVarsNodeProperty.getEnvVars()
} else {
    envVars = envVarsNodePropertyList.get(0).getEnvVars()
}

config?.each { key, value ->
    println("Adding environment variable ${key} : ${value}")
    envVars.put(key, value)
}

instance.save()
