import hudson.plugins.sonar.SonarGlobalConfiguration
import hudson.plugins.sonar.SonarInstallation
import hudson.plugins.sonar.model.TriggersConfig
import jenkins.model.Jenkins

import java.util.logging.Level
import java.util.logging.Logger

def descriptor = Jenkins.getInstance().getDescriptorByType(SonarGlobalConfiguration.class)
def logger = Logger.getLogger("sonarqube.groovy")

logger.log(Level.INFO, "Configuring sonar installations")

List<SonarInstallation> installations = []

config?.each { instance ->

    println("Adding sonar installer with name ${instance.name} and url ${instance.serverUrl}")
    logger.log(Level.INFO, "Adding sonar installer with name ${instance.name} and url ${instance.serverUrl}")

    def installation = new SonarInstallation(
            instance.name as String,
            instance.serverUrl as String, 
            instance.serverAuthenticationToken as String,
            null,
            "-XX", 
            new TriggersConfig(),
            "sonar.organization=bleugh"
        )

    installations.add(installation)

}
descriptor.setInstallations(installations.toArray(new SonarInstallation[installations.size()]))
descriptor.save()
