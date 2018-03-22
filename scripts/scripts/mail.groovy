import jenkins.model.*

def inst = Jenkins.getInstance()

def desc = inst.getDescriptor("hudson.tasks.Mailer")

desc.setSmtpHost("${config.host}")
desc.setDefaultSuffix("${config.defaultSuffix}")

desc.save()
