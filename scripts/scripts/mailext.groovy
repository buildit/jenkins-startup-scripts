import jenkins.model.*

if (!pluginAvailable()) {
    return
}

def inst = Jenkins.getInstance()

def desc = inst.getDescriptor("hudson.plugins.emailext.ExtendedEmailPublisher")

desc.setSmtpServer("${config.host}")
desc.setDefaultSuffix("${config.defaultSuffix}")

desc.save()

static pluginAvailable() {
    try {
        Class.forName("hudson.plugins.emailext.ExtendedEmailPublisher")
    } catch (ClassNotFoundException ignored) {
        return false
    }
    return true
}
