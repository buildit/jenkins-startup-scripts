package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.*;

class AzureVmAgentsTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(AzureVmAgentsTest.class, ["scripts/azureVmAgents.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["azure-vm-agents-0.7.1.hpi", "credentials-2.1.16.hpi", "plain-credentials-1.4.hpi", "azure-commons-0.2.6.hpi",
                 "cloud-stats-0.18.hpi", "azure-credentials-1.6.0.hpi", "workflow-step-api-2.14.hpi", "ssh-credentials-1.13.hpi",
                 "structs-1.14.hpi", "credentials-binding-1.16.hpi"])
    void shouldSetupAzureVmAgents() {

        def clouds = jenkinsRule.instance.clouds
        assertThat(clouds.size()).isEqualTo(2)

        // 'built-in cloud' assertions

        def buildInCloud = clouds[0]
        assertThat(buildInCloud.name).isEqualTo('ubuntu-builtin-image-cloud-name')
        assertThat(buildInCloud.azureCredentialsId).isEqualTo('azure-sp')
        assertThat(buildInCloud.maxVirtualMachinesLimit).isEqualTo(2)
        assertThat(buildInCloud.deploymentTimeout).isEqualTo(1200)
        assertThat(buildInCloud.newResourceGroupName).isEqualTo("new-resource-group")

        // 'built-in' cloud template assertions

        def template = buildInCloud.getAzureAgentTemplate("ubuntu-template-name")
        assertThat(template.templateName).isEqualTo("ubuntu-template-name")
        assertThat(template.templateDesc).isEqualTo("ubuntu built-in image")

        assertThat(template.agentWorkspace).isEqualTo("/opt/jenkins/workspace")
        assertThat(template.labels).isEqualTo("ubuntu-label")
        assertThat(template.location).isEqualTo("UK West")
        assertThat(template.virtualMachineSize).isEqualTo("Standard_DS2_v2")
        assertThat(template.storageAccountType).isEqualTo("Standard_LRS")

        // returns random string
        //assertThat(template.newStorageAccountName).isEqualTo("new-storage-account")

        assertThat(template.diskType).isEqualTo("managed")
        assertThat(template.shutdownOnIdle).isEqualTo(false)
        assertThat(template.usageMode).isEqualTo("Use this node as much as possible")
        assertThat(template.credentialsId).isEqualTo("my-admin-credential-id")

        //  'built-in' cloud image assertions

        assertThat(template.builtInImage).isEqualTo("Ubuntu 16.04 LTS")
        assertThat(template.isInstallGit).isEqualTo(true)
        assertThat(template.isInstallMaven).isEqualTo(false)
        assertThat(template.isInstallDocker).isEqualTo(true)

        ///////////////////////////////////////////////////////////////////////////////////////

        // 'advanced image' cloud assertions

        def advancedImageCloud = clouds[1]
        assertThat(advancedImageCloud.name).isEqualTo('ubuntu-advanced-image-cloud-name')
        assertThat(advancedImageCloud.azureCredentialsId).isEqualTo('azure-sp')
        assertThat(advancedImageCloud.maxVirtualMachinesLimit).isEqualTo(10)
        assertThat(advancedImageCloud.deploymentTimeout).isEqualTo(600)
        assertThat(advancedImageCloud.existingResourceGroupName).isEqualTo("existing-resource-group")

        // 'advanced' cloud template assertions

        def advancedTemplate = advancedImageCloud.getAzureAgentTemplate("advanced-template-name")
        assertThat(advancedTemplate.templateName).isEqualTo("advanced-template-name")
        assertThat(advancedTemplate.templateDesc).isEqualTo("advanced image")

        assertThat(advancedTemplate.agentWorkspace).isEqualTo("/var/jenkins/workspace")
        assertThat(advancedTemplate.labels).isEqualTo("advanced-label")
        assertThat(advancedTemplate.location).isEqualTo("UK South")
        assertThat(advancedTemplate.virtualMachineSize).isEqualTo("Standard_D4s_v3")
        assertThat(advancedTemplate.storageAccountType).isEqualTo("Standard_LRS")
        assertThat(advancedTemplate.existingStorageAccountName).isEqualTo("existing-storage-account")
        assertThat(advancedTemplate.diskType).isEqualTo("managed")
        assertThat(advancedTemplate.shutdownOnIdle).isEqualTo(true)
        assertThat(advancedTemplate.usageMode).isEqualTo("Only build jobs with label expressions matching this node")
        assertThat(advancedTemplate.credentialsId).isEqualTo("my-admin-credential-id")

        //  advanced image assertions

        assertThat(advancedTemplate.imageId).isEqualTo("/subscriptions/UUID/resourceGroups/resource-group-name/providers/Microsoft.Compute/images/jenkins-agent-20180712181349")
        assertThat(advancedTemplate.osType).isEqualTo('Linux')
        assertThat(advancedTemplate.agentLaunchMethod).isEqualTo('SSH')
        assertThat(advancedTemplate.initScript).isEqualTo('apt-get update && apt-get upgrade -y')
        assertThat(advancedTemplate.executeInitScriptAsRoot).isEqualTo(true)
        assertThat(advancedTemplate.doNotUseMachineIfInitFails).isEqualTo(true)
        assertThat(advancedTemplate.virtualNetworkName).isEqualTo('my-vnet')
        assertThat(advancedTemplate.virtualNetworkResourceGroupName).isEqualTo('my-vnet-rg')
        assertThat(advancedTemplate.subnetName).isEqualTo('my-subnet')
        assertThat(advancedTemplate.usePrivateIP).isEqualTo(true)
        assertThat(advancedTemplate.noOfParallelJobs).isEqualTo(4)
        assertThat(advancedTemplate.jvmOptions).isEqualTo('-Xms4G -Xmx4G')
        assertThat(advancedTemplate.preInstallSsh).isEqualTo(false)

        //Thread.sleep(1000 * 60 * 5)
    }
}
