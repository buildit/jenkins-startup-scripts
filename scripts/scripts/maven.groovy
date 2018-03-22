import hudson.tasks.Maven
import hudson.tasks.Maven.MavenInstallation
import hudson.tools.InstallSourceProperty
import hudson.tools.ToolProperty
import hudson.tools.ToolPropertyDescriptor
import hudson.tools.ZipExtractionInstaller
import hudson.util.DescribableList
import jenkins.model.Jenkins

def extensions = Jenkins.instance.getExtensionList(Maven.DescriptorImpl.class)[0]

def List<MavenInstallation> installations = []

config?.each { version ->

    println("Adding maven installer with name ${version.name} and version ${version.url}")

    def describableList = new DescribableList<ToolProperty<?>, ToolPropertyDescriptor>()
    def installer = new ZipExtractionInstaller(version.label as String, version.url as String, version.subdir as String);

    describableList.add(new InstallSourceProperty([installer]))

    installations.add(new MavenInstallation(version.name as String, "", describableList))

}
extensions.setInstallations(installations.toArray(new MavenInstallation[installations.size()]))
extensions.save()
