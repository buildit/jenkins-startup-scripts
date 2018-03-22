import jenkins.model.Jenkins

def instance = Jenkins.getInstance()
def numExecutors = config.active

println("Setting number of executors on master to: ${numExecutors}")

instance.setNumExecutors(numExecutors)
instance.save()
