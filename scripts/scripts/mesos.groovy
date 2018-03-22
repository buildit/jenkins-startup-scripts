import org.jenkinsci.plugins.mesos.*

def addMesosCloud(cloudList, config) {

    List<MesosSlaveInfo.ContainerInfo> slaveContainers = new ArrayList<MesosSlaveInfo.ContainerInfo>()

    config.slaves.each { slave ->

        List<MesosSlaveInfo.URI> uris = new ArrayList<MesosSlaveInfo.URI>()
        List<MesosSlaveInfo.Volume> volumes = new ArrayList<MesosSlaveInfo.Volume>()
        List<MesosSlaveInfo.Parameter> parameters = new ArrayList<MesosSlaveInfo.Parameter>()
        List<MesosSlaveInfo.PortMapping> portMappings = new ArrayList<MesosSlaveInfo.PortMapping>()
        List<MesosSlaveInfo.NetworkInfo> networks = new ArrayList<MesosSlaveInfo.NetworkInfo>()


        slave.uris.each { uri ->
            uris.add(new MesosSlaveInfo.URI(
                    uri.url,
                    uri.executable,
                    uri.extract
            ))
        }

        slave.volumes.each { volume ->
            volumes.add(new MesosSlaveInfo.Volume(
                    volume.containerPath,
                    volume.hostPath,
                    volume.readOnly
            ))
        }

        slave.params.each { key, value ->
            parameters.add(new MesosSlaveInfo.Parameter(
                    key, value
            ))
        }

        slave.ports.each { port ->
            portMappings.add(new MesosSlaveInfo.PortMapping(
                    port.containerPort,
                    port.hostPort,
                    port.protocol
            ))
        }

        slave.networks.each { network ->
            networks.add(new MesosSlaveInfo.NetworkInfo(
                    network.name
            ))
        }

        def slaveContainerInfo = new MesosSlaveInfo.ContainerInfo(
                type = 'DOCKER',
                dockerImage = slave?.containerInfo?.dockerImage ?: '',
                dockerPrivilegedMode = slave?.containerInfo?.dockerPrivilegedMode ?: false,
                dockerForcePullImage = slave?.containerInfo?.dockerForcePullImage ?: false,
                dockerImageCustomizable = slave?.containerInfo?.dockerImageCustomizable ?: false,
                useCustomDockerCommandShell = slave?.containerInfo?.useCustomDockerCommandShell ?: false,
                customDockerCommandShell = slave?.containerInfo?.customDockerCommandShell ?: '',
                volumes = volumes,
                parameters = parameters,
                networking = slave?.containerInfo?.networking == 'HOST' ? 'HOST' : 'BRIDGE',
                portMappings = portMappings,
                networks = networks
        )

        def slaveInfo = new MesosSlaveInfo(
                labelString = slave.labelString ?: "mesos",
                mode = null,
                slaveCpus = (slave.slaveCpus ?: '0.1'),
                slaveMem = slave.slaveMem ?: '512',
                minExecutors = slave.minExecutors ?: '2',
                maxExecutors = slave.maxExecutors ?: '2',
                executorCpus = slave.executorCpus ?: '0.1',
                executorMem = slave.executorMem ?: '128',
                remoteFSRoot = slave.remoteFSRoot ?: 'jenkins',
                idleTerminationMinutes = slave.idleTerminationMinutes ?: '3',
                slaveAttributes = slave.slaveAttributes ?: '',
                jvmArgs = slave.jvmArgs ?: '',           //default in class
                jnlpArgs = slave.jnlpArgs ?: '',
                defaultSlave = null,
                containerInfo = slave?.containerInfo?.dockerImage ? slaveContainerInfo : null,
                additionalURIs = uris,
                nodeProperties = null
        )
        slaveContainers.add(slaveInfo)
    }
    def cloud = new MesosCloud(
            cloudName = config.master ?: 'Mesos Cloud',
            nativeLibraryPath = '/usr/lib/libmesos.so',
            master = config.master ?: '',
            description = config.description ?: '',
            frameworkName = config.frameworkName ?: 'Jenkins Scheduler',
            role = config.role ?: '*',
            slavesUser = config.slavesUser ?: 'jenkins',
            credentialsId = config.slavesCredentialsId ?: '',
            principal = config.principal ?: 'jenkins',
            secret = config.secret ?: '',
            slaveInfos = slaveContainers,
            checkpoint = (config.checkpoint ?: 'false').toBoolean(),
            onDemandRegistration = (config.onDemand ?: 'true').toBoolean(),
            jenkinsURL = config.jenkinsURL ?: '',
            declineOfferDuration = config.declineOfferDuration ?: '',
            cloudID = config.cloudId ?: ''
    )
    cloudList.add(cloud)
}

def clouds = instance.clouds
if (clouds) {
    clouds.remove(instance.clouds.get(MesosCloud.class))
}
config.each { name, details ->
    addMesosCloud(clouds, details)
}
