Thread.start {
    while (jenkins.model.Jenkins.instance.getItemByFullName("jobdsl") == null ||
            jenkins.model.Jenkins.instance.getItemByFullName("jobdsl").getLastCompletedBuild() == null) {
        Thread.sleep(1000)
        println 'Waiting for jobdsl job'
    }

    def projectName = getProjectName()
    def etcHost = getEtcdHost()

    for (job in jenkins.model.Jenkins.theInstance.getAllItems()) {
        println "Found job: ${job.name}"

        try {
            n = job.getNextBuildNumber()
            if (n == 1) {
                URL apiUrl = "${etcHost}/v2/keys/${projectName}/jenkins_jobs/${job.name}".toURL()
                try {
                    def o = new groovy.json.JsonSlurper().parse(apiUrl.newReader())
                    println "Using etcd API for job: ${apiUrl}"
                    println "Current build number: ${o.node.value}"
                    int nextBuildNumber = Integer.parseInt(o.node.value) + 1
                    println "Next build number: ${nextBuildNumber}"
                    jenkins.model.Jenkins.instance.getItemByFullName("${job.name}").updateNextBuildNumber(nextBuildNumber)
                } catch (FileNotFoundException e) {
                    println "Job ${job.name} not configured, ignoring"
                }
            } else {
                println "Job ${job.name} has already a next build number ${n}, ignoring"
            }
        } catch (Exception e) {
            println "Job ${job.name} can not be configured: ${e.getMessage()}"
        }

    }

}

private String getProjectName() {
    if (!config['projectName']) {
        throw new IllegalStateException("projectName is not defined in updatebuildnumbers config")
    }
    println "Project name ${config.projectName} found for updatebuildnumbers"
    return config.projectName
}

private String getEtcdHost() {
    def envVarsNodePropertyList = jenkins.model.Jenkins.instance.globalNodeProperties.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)

    if (envVarsNodePropertyList[0] == null || envVarsNodePropertyList[0].envVars.get('ETCD_HOST') == null) {
        throw new IllegalStateException("ETCD_HOST is not defined in environment variables")
    } else {
        println "Host ${envVarsNodePropertyList[0].envVars.get('ETCD_HOST')} found for updatebuildnumbers"
        return envVarsNodePropertyList[0].envVars.get('ETCD_HOST')
    }
}
