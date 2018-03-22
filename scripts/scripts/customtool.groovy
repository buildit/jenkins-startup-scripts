import com.cloudbees.jenkins.plugins.customtools.CustomTool
import com.synopsys.arc.jenkinsci.plugins.customtools.versions.ToolVersionConfig
import hudson.tools.*

def descriptor = instance.getDescriptor("com.cloudbees.jenkins.plugins.customtools.CustomTool")

List installations = []

config.each { tool ->

    println("Adding custom installer with name ${tool.name}")

    List<ToolInstaller> installers = new ArrayList<ToolInstaller>();
    installers.add(new ZipExtractionInstaller(tool.label as String, tool.url as String, tool.subdir as String));

    List<ToolProperty<ToolInstallation>> properties = new ArrayList<ToolProperty<ToolInstallation>>();
    properties.add(new InstallSourceProperty(installers));

    CustomTool installation = new CustomTool(tool.name, null, properties, "./", null, ToolVersionConfig.DEFAULT, null);

    installations.add(installation)

}
descriptor.setInstallations(installations.toArray(new CustomTool[installations.size()]))
descriptor.save()
