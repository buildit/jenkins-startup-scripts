import hudson.plugins.sonar.SonarGlobalConfiguration
import hudson.plugins.sonar.SonarInstallation
import hudson.plugins.sonar.model.TriggersConfig
import jenkins.model.Jenkins

import java.util.logging.Level
import java.util.logging.Logger

import static hudson.plugins.sonar.utils.SQServerVersions.SQ_5_1_OR_LOWER
import static hudson.plugins.sonar.utils.SQServerVersions.SQ_5_3_OR_HIGHER

def descriptor = Jenkins.getInstance().getDescriptorByType(SonarGlobalConfiguration.class)
def logger = Logger.getLogger("sonarqube.groovy")

logger.log(Level.INFO, "Configuring sonar installations")

List<SonarInstallation> installations = []

config?.each { instance ->

    println("Adding sonar installer with name ${instance.name} and url ${instance.serverUrl}")
    logger.log(Level.INFO, "Adding sonar installer with name ${instance.name} and url ${instance.serverUrl}")
    String version = SQ_5_1_OR_LOWER //default to 5.1 or lower to maintain backwards compatibility to when this was hardcoded to that
    if (instance.version.equals("5.3 or higher")) {
        version = SQ_5_3_OR_HIGHER
    }
    def installation = new SonarInstallation(instance.name as String,
            instance.serverUrl as String, version, instance.serverAuthenticationToken as String,
            instance.databaseUrl as String, instance.databaseLogin as String, instance.databasePassword as String,
            "", "", new TriggersConfig(),
            instance.sonarLogin as String, instance.sonarPassword as String, "")

    installations.add(installation)

}
descriptor.setInstallations(installations.toArray(new SonarInstallation[installations.size()]))
descriptor.save()
