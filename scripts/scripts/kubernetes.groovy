import org.csanchez.jenkins.plugins.kubernetes.*
import org.csanchez.jenkins.plugins.kubernetes.model.*
import org.csanchez.jenkins.plugins.kubernetes.volumes.*
import org.csanchez.jenkins.plugins.kubernetes.volumes.workspace.*

def addKubernetesCloud(cloudList, config) {
    def cloud = new KubernetesCloud(
            cloudName = config.cloudName ?: 'Kubernetes',
            templates = null,
            serverUrl = config.serverUrl ?: '',
            namespace = config.namespace ?: '',
            jenkinsUrl = config.jenkinsUrl ?: '',
            containerCapStr = config.containerCapStr ?: '',
            connectTimeout = config.connectTimeout ?: 300,
            readTimeout = config.readTimeout ?: 300,
            retentionTimeout = config.retentionTimeout ?: 300
    )
    cloud.serverCertificate = config.serverCertificate ?: ''
    cloud.skipTlsVerify = config.skipTlsVerify ?: false
    cloud.credentialsId = config.credentialsId ?: ''
    cloud.jenkinsTunnel = config.jenkinsTunnel ?: ''

    cloud.templates = buildPodTemplates(config.podTemplates)

    cloudList.add(cloud)
}

def buildPodTemplates(podTemplates) {
    def results = []
    podTemplates.each { template ->

        def podTemplate = new PodTemplate()

        podTemplate.inheritFrom = template.inheritFrom ?: ''
        podTemplate.name = template.name ?: ''
        podTemplate.namespace = template.namespace ?: ''
        podTemplate.image = template.image ?: ''
        podTemplate.command = template.command ?: ''
        podTemplate.args = template.args ?: ''
        podTemplate.remoteFs = template.remoteFs ?: ''
        podTemplate.label = template.label ?: ''
        podTemplate.serviceAccount = template.serviceAccount ?: ''
        podTemplate.nodeSelector = template.nodeSelector ?: ''
        podTemplate.resourceRequestCpu = template.resourceRequestCpu ?: ''
        podTemplate.resourceRequestMemory = template.resourceRequestMemory ?: ''
        podTemplate.resourceLimitCpu = template.resourceLimitCpu ?: ''
        podTemplate.resourceLimitMemory = template.resourceLimitMemory ?: ''

        podTemplate.privileged = template.privileged ?: false
        podTemplate.alwaysPullImage = template.alwaysPullImage ?: false
        podTemplate.instanceCap = template.instanceCap ?: 0
        podTemplate.slaveConnectTimeout = template.slaveConnectTimeout ?: ''
        podTemplate.idleMinutes = template.idleMinutes ?: ''
        podTemplate.customWorkspaceVolumeEnabled = template.customWorkspaceVolumeEnabled ?: ''

        podTemplate.containers = buildContainerTemplates(template.containerTemplates)
        podTemplate.envVars = buildEnvVars(template.envVars)
        podTemplate.workspaceVolume = buildWorkspaceVolume(template.workspaceVolume)
        podTemplate.volumes = buildPodVolumes(template.podVolumes)

        podTemplate.slaveConnectTimeout = template.slaveConnectTimeout ?: ''
        podTemplate.nodeUsageMode = buildNodeUsageMode(template.nodeUsageMode)

        podTemplate.annotations = buildPodAnnotations(template.annotations)
        podTemplate.imagePullSecrets = buildImagePullSecrets(template.imagePullSecrets)

        // hoping this will not be needed as it looks like a pain to implement
        //podTemplate.nodeProperties = buildNodeProperties(template.nodeProperties)

        results.add(podTemplate)
    }
    return results
}

def buildContainerTemplates(containers) {
    def results = []
    containers.each { container ->

        def containerTemplate = new ContainerTemplate(container.name ?: '', container.image ?: '')

        containerTemplate.workingDir = container.workingDir ?: ''
        containerTemplate.command = container.command ?: ''
        containerTemplate.args = container.args ?: ''
        containerTemplate.resourceRequestCpu = container.resourceRequestCpu ?: ''
        containerTemplate.resourceRequestMemory = container.resourceRequestMemory ?: ''
        containerTemplate.resourceLimitCpu = container.resourceLimitCpu ?: ''
        containerTemplate.resourceLimitMemory = container.resourceLimitMemory ?: ''

        containerTemplate.privileged = container.privileged ?: false
        containerTemplate.alwaysPullImage = container.alwaysPullImage ?: false
        containerTemplate.ttyEnabled = container.ttyEnabled ?: false

        containerTemplate.envVars = buildEnvVars(container.envVars)

        containerTemplate.ports = buildPortMappings(container.ports)
        containerTemplate.livenessProbe = buildLivenessProbe(container.livenessProbe)

        results.add(containerTemplate)
    }
    return results
}

def buildEnvVars(envVars) {
    def results = []
    envVars.each { envVar ->
        switch (envVar.type) {
            case "SecretEnvVar":
                results.add(new SecretEnvVar(envVar.key, envVar.secretName, envVar.secretKey))
                break
            case "KeyValueEnvVar":
                results.add(new KeyValueEnvVar(envVar.key, envVar.value))
                break
            default:
                throw new RuntimeException("Please provide one of the following types [SecretEnvVar, EmptyDirVolume, HostPathWorkspaceVolume, NfsWorkspaceVolume]")
        }
    }
    return results
}

def buildNodeUsageMode(nodeUsageMode) {
    if (!nodeUsageMode) {
        return
    }
    switch (nodeUsageMode.type) {
        case 'NORMAL':
            return hudson.model.Node.Mode.NORMAL
        case 'EXCLUSIVE':
            return hudson.model.Node.Mode.EXCLUSIVE
        default:
            throw new RuntimeException("Please provide one of the following types [PersistentVolume, EmptyDirVolume, HostPathWorkspaceVolume, NfsWorkspaceVolume]")
    }
}

def buildImagePullSecrets(imagePullSecrets) {
    if (!imagePullSecrets) {
        return
    }
    def results = []
    imagePullSecrets.each { imagePullSecret ->
        results.add(new PodImagePullSecret(imagePullSecret.name))
    }
    return results
}

def buildPortMappings(portMappings) {
    if (!portMappings) {
        return
    }
    def results = []
    portMappings.each { portMapping ->
        results.add(new PortMapping(portMapping.name, portMapping.containerPort, portMapping.hostPort))
    }
    return results
}

def buildLivenessProbe(livenessProbe) {
    if (!livenessProbe) {
        return
    }
    return new ContainerLivenessProbe(livenessProbe.execArgs, livenessProbe.timeoutSeconds, livenessProbe.initialDelaySeconds, livenessProbe.failureThreshold, livenessProbe.periodSeconds, livenessProbe.successThreshold)
}

def buildPodAnnotations(podAnnotations) {
    if (!podAnnotations) {
        return
    }
    def results = []
    podAnnotations.each { podAnnotation ->
        results.add(new PodAnnotation(podAnnotation.key, podAnnotation.value))
    }
    return results
}

def buildWorkspaceVolume(workspaceVolume) {
    if (!workspaceVolume) {
        return
    }
    switch (workspaceVolume.type) {
        case 'PersistentVolumeClaimWorkspaceVolume':
            return new PersistentVolumeClaimWorkspaceVolume(workspaceVolume.claimName, workspaceVolume.readOnly)
        case 'EmptyDirWorkspaceVolume':
            return new EmptyDirWorkspaceVolume(workspaceVolume.memory)
        case 'HostPathWorkspaceVolume':
            return new HostPathWorkspaceVolume(workspaceVolume.hostPath)
        case 'NfsWorkspaceVolume':
            return new NfsWorkspaceVolume(workspaceVolume.serverAddress, workspaceVolume.serverPath, workspaceVolume.readOnly)
        default:
            throw new RuntimeException("Please provide one of the following types [PersistentVolume, EmptyDirVolume, HostPathWorkspaceVolume, NfsWorkspaceVolume]")
    }
}

def buildPodVolumes(podVolumes) {
    def results = []
    podVolumes.each { podVolume ->
        switch (podVolume.type) {
            case 'ConfigMapVolume':
                results.add(new ConfigMapVolume(podVolume.mountPath, podVolume.configMapName))
                break
            case 'EmptyDirVolume':
                results.add(new EmptyDirVolume(podVolume.mountPath, podVolume.memory))
                break
            case 'HostPathVolume':
                results.add(new HostPathVolume(podVolume.hostPath, podVolume.mountPath))
                break
            case 'NfsVolume':
                results.add(new NfsVolume(podVolume.serverAddress, podVolume.serverPath, podVolume.readOnly, podVolume.mountPath))
                break
            case 'PersistentVolumeClaim':
                results.add(new PersistentVolumeClaim(podVolume.mountPath, podVolume.claimName, podVolume.readOnly))
                break
            case 'SecretVolume':
                results.add(new SecretVolume(podVolume.mountPath, podVolume.secretName))
                break
            default:
                throw new RuntimeException("Please provide one of the following types [ConfigMapVolume, EmptyDirVolume, HostPathVolume, NfsVolume, PersistentVolumeClaim, SecretVolume]")
        }
    }
    return results
}

def clouds = instance.clouds
if (clouds) {
    clouds.remove(instance.clouds.get(KubernetesCloud.class))
}
config.each { name, details ->
    addKubernetesCloud(clouds, details)
}
