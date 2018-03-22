import jenkins.model.Jenkins

def descriptor = Jenkins.getInstance().getDescriptor("jenkins.model.JenkinsLocationConfiguration")
descriptor.setUrl(config.host.url)
descriptor.setAdminAddress(config.host.adminEmail)

descriptor.save()
