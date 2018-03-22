import com.anchore.jenkins.plugins.anchore.AnchoreBuilder
import jenkins.model.Jenkins

def instance = Jenkins.getInstance()
def descriptor = instance.getDescriptorByType(AnchoreBuilder.DescriptorImpl)

descriptor.setDebug(config.debug)
descriptor.setEnabled(config.enabled)
descriptor.setUseSudo(config.useSudo)
descriptor.setEnginemode(config.engineMode)
descriptor.setEngineurl(config.engineUrl)
descriptor.setEngineuser(config.engineUser)
descriptor.setEnginepass(config.enginePass)
descriptor.setEngineverify(config.engineVerifySSL)
descriptor.setContainerImageId(config.containerImageId)
descriptor.setContainerId(config.containerId)

descriptor.save()
