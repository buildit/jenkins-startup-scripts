import hudson.plugins.groovy.GroovyInstallation
import hudson.plugins.groovy.GroovyInstaller
import hudson.tools.InstallSourceProperty
import hudson.tools.ZipExtractionInstaller

def descriptor = instance.getDescriptorByType(GroovyInstallation.DescriptorImpl.class)

def List<GroovyInstallation> installations = []

config?.each { version ->
    println("Adding Groovy installer with name ${version.name}")

    def installer
    if (version.url) {
        // Install from URL
        installer = new ZipExtractionInstaller(version.label as String, version.url as String, version.subdir as String)
    } else if (version.version) {
        // Install from Groovy Website
        installer = new GroovyInstaller(version.version as String)
    }

    def groovy = new GroovyInstallation(version.name as String, '', [new InstallSourceProperty([installer])])
    installations.add(groovy)
}

descriptor.setInstallations(installations.toArray(new GroovyInstallation[installations.size()]))
descriptor.save()
