import hudson.model.JDK
import hudson.tools.InstallSourceProperty
import hudson.tools.ZipExtractionInstaller

def descriptor = new JDK.DescriptorImpl();

println("Configured java installations: ${config.javaInstallations}")

def List<JDK> installations = []

config?.each { version ->

    println("Adding java installer with name ${version.name}")

    def installer = new ZipExtractionInstaller(version.label as String, version.url as String, version.subdir as String);
    def jdk = new JDK(version.name as String, null, [new InstallSourceProperty([installer])])
    installations.add(jdk)

}
descriptor.setInstallations(installations.toArray(new JDK[installations.size()]))
descriptor.save()
