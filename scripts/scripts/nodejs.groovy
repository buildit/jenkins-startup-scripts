import hudson.tools.InstallSourceProperty
import jenkins.model.Jenkins
import hudson.tools.*;
import jenkins.plugins.nodejs.tools.*

new File("${jenkinsHome}/node").mkdirs()

def descriptor = Jenkins.getInstance().getDescriptor(NodeJSInstallation);

println("Configured node installations: ${config.nodeInstallations}")

def List<NodeJSInstallation> installations = []

config?.each { version ->

    println("Adding node installer with name ${version.name} and version ${version.url}")

    def installer
    if (version.url) {
        installer = new ZipExtractionInstaller(version.label as String, version.url as String, version.subdir as String);
    } else {
        installer = new NodeJSInstaller(version.version as String, "", 72);
    }

    def installation = new NodeJSInstallation(version.name as String, "", [new InstallSourceProperty([installer])])

    installations.add(installation)

}
descriptor.setInstallations(installations.toArray(new NodeJSInstallation[installations.size()]))
descriptor.save()
