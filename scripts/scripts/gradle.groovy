import hudson.plugins.gradle.GradleInstallation
import hudson.plugins.gradle.GradleInstaller
import hudson.tools.InstallSourceProperty
import hudson.tools.ZipExtractionInstaller

def descriptor = instance.getDescriptorByType(GradleInstallation.DescriptorImpl.class)

def List<GradleInstallation> installations = []

config?.each { version ->
    println("Adding Gradle installer with name ${version.name}")

    def installer
    if (version.url) {
        // Install from URL
        installer = new ZipExtractionInstaller(version.label as String, version.url as String, version.subdir as String)
    } else if (version.version) {
        // Install from gradle.org
        installer = new GradleInstaller(version.version as String)
    }

    def gradle = new GradleInstallation(version.name as String, '', [new InstallSourceProperty([installer])])
    installations.add(gradle)
}

descriptor.setInstallations(installations.toArray(new GradleInstallation[installations.size()]))
descriptor.save()
