import com.microsoft.azure.vmagent.AzureVMCloud
import com.microsoft.azure.vmagent.builders.*
import jenkins.model.Jenkins

def clouds = instance.clouds
if (clouds) {
    clouds.removeAll(instance.clouds.getAll(AzureVMCloud.class))
}

config.each { name, details ->
    addAzureCloud(details)
}

def addAzureCloud(cloudConfig) {
    def cloudBuilder = new AzureVMCloudBuilder()
        .withCloudName(cloudConfig.cloudName)
        .withAzureCredentialsId(cloudConfig.credentialsId)
        .withMaxVirtualMachinesLimit(cloudConfig.maxVmLimit)
        .withDeploymentTimeout(cloudConfig.deploymentTimeout)
        .withNewResourceGroupName(cloudConfig.newResourceGroupName ?: '')
        .withExistingResourceGroupName(cloudConfig.existingResourceGroupName ?: '')

    def templateBuilder = new AzureVMTemplateBuilder()
            .withName(cloudConfig.template.name)
            .withDescription(cloudConfig.template.description)
            .withWorkspace(cloudConfig.template.agentWorkspace)
            .withLabels(cloudConfig.template.labels)
            .withLocation(cloudConfig.template.location)
            .withVirtualMachineSize(cloudConfig.template.vmSize)
            .withStorageAccountType(cloudConfig.template.storageAccountType)
            .withNewStorageAccount(cloudConfig.template.newStorageAccountName ?: '')
            .withExistingStorageAccount(cloudConfig.template.existingStorageAccountName ?: '')
            .withDiskType(cloudConfig.template.diskType)
            .withShutdownOnIdle(cloudConfig.template.shutdownOnIdle)
            .withUsageMode(cloudConfig.template.usageMode ?: '')
            .withAdminCredential(cloudConfig.template.vmAdminCredentialsId)

    if (cloudConfig.template.builtInImage) {

        def imageConfig = cloudConfig.template.builtInImage

        def builtInImage = new BuiltInImageBuilder()
            .withBuiltInImageName(imageConfig.imageName)
            .withInstallGit(imageConfig.installGit)
            .withInstallMaven(imageConfig.installMaven)
            .withInstallDocker(imageConfig.installDocker)
            .build()

        templateBuilder.withBuiltInImage(builtInImage)
    }
    if (cloudConfig.template.advancedImage) {

        def imageConfig = cloudConfig.template.advancedImage

        def advancedImage = new AdvancedImageBuilder()
            .withCustomManagedImage(imageConfig.customManagedImageId)
            .withOsType(imageConfig.osType)
            .withLaunchMethod(imageConfig.launchMethod)
            .withInitScript(imageConfig.initScript)
            .withRunScriptAsRoot(imageConfig.runScriptAsRoot)
            .withDoNotUseMachineIfInitFails(imageConfig.doNotUseMachineIfInitFails)
            .withVirtualNetworkName(imageConfig.virtualNetworkName)
            .withVirtualNetworkResourceGroupName(imageConfig.virtualNetworkResourceGroupName)
            .withSubnetName(imageConfig.subnetName)
            .withUsePrivateIP(imageConfig.usePrivateIP)
            .withNumberOfExecutors(imageConfig.numberOfExecutors)
            .withJvmOptions(imageConfig.jvmOptions)
            .withPreInstallSsh(imageConfig.preInstallSsh)
            .build()

        templateBuilder.withAdvancedImage(advancedImage)
    }

    template = templateBuilder.build()
    cloud = cloudBuilder.addToTemplates(template).build()
    Jenkins.getInstance().clouds.add(cloud)
}