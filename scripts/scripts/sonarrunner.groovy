import hudson.plugins.sonar.SonarRunnerInstallation
import hudson.tools.InstallSourceProperty
import hudson.tools.ZipExtractionInstaller
import jenkins.model.Jenkins

def descriptor = Jenkins.getInstance().getDescriptor("hudson.plugins.sonar.SonarRunnerInstallation")

List<SonarRunnerInstallation> installations = []

config?.each { instance ->

    println("Adding sonar runner with name ${instance.name} with install URL ${instance.url}")

    def installer = new ZipExtractionInstaller(instance.label as String, instance.url as String, instance.subdir as String)
    def installProperty = new InstallSourceProperty([installer])
    installations.add(new SonarRunnerInstallation(instance.name, "", [installProperty]))

}

descriptor.setInstallations(installations.toArray(new SonarRunnerInstallation[installations.size()]))
descriptor.save()
