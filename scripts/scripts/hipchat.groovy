#!/usr/bin/env groovy
import jenkins.model.Jenkins
import jenkins.plugins.hipchat.HipChatNotifier

def instance = Jenkins.getInstance()
def descriptor = instance.getDescriptorByType(HipChatNotifier.DescriptorImpl)

descriptor.setServer(config.server)
descriptor.v2Enabled = (config.v2Enabled ?: 'true').toBoolean()
descriptor.setRoom(config.room)
descriptor.setSendAs(config.sendAs)
descriptor.setCredentialId(config.credentialsId)

descriptor.save()
