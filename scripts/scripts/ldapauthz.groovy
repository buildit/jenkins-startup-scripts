import hudson.*
import hudson.model.*
import hudson.security.*
import jenkins.*
import jenkins.model.*

def administrators = [
        name : "jenkins-administrators",
        roles: [
                "hudson.model.Computer.Build",
                "hudson.model.Computer.Configure",
                "hudson.model.Computer.Connect",
                "hudson.model.Computer.Create",
                "hudson.model.Computer.Delete",
                "hudson.model.Computer.Disconnect",
                "hudson.model.Computer.Provision",
                "com.cloudbees.plugins.credentials.CredentialsProvider.Create",
                "com.cloudbees.plugins.credentials.CredentialsProvider.Delete",
                "com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains",
                "com.cloudbees.plugins.credentials.CredentialsProvider.Update",
                "com.cloudbees.plugins.credentials.CredentialsProvider.View",
                "hudson.model.Hudson.Administer",
                "hudson.model.Hudson.ConfigureUpdateCenter",
                "hudson.model.Hudson.Read",
                "hudson.model.Hudson.RunScripts",
                "hudson.model.Hudson.UploadPlugins",
                "hudson.model.Item.Build",
                "hudson.model.Item.Cancel",
                "hudson.model.Item.Configure",
                "hudson.model.Item.Create",
                "hudson.model.Item.Delete",
                "hudson.model.Item.Discover",
                "hudson.model.Item.Move",
                "hudson.model.Item.Read",
                "hudson.model.Item.Workspace",
                "hudson.model.Run.Delete",
                "hudson.model.Run.Replay",
                "hudson.model.Run.Update",
                "hudson.model.View.Configure",
                "hudson.model.View.Create",
                "hudson.model.View.Delete",
                "hudson.model.View.Read",
                "hudson.scm.SCM.Tag",
                "hudson.security.HealthCheck",
                "hudson.security.ThreadDump",
                "hudson.security.View"
        ]
]

def developers = [
        name : "jenkins-developers",
        roles: [
                "hudson.model.Computer.Build",
                "hudson.model.Computer.Configure",
                "hudson.model.Computer.Connect",
                "hudson.model.Computer.Create",
                "hudson.model.Computer.Delete",
                "hudson.model.Computer.Disconnect",
                "hudson.model.Computer.Provision",
                "com.cloudbees.plugins.credentials.CredentialsProvider.Create",
                "com.cloudbees.plugins.credentials.CredentialsProvider.Delete",
                "com.cloudbees.plugins.credentials.CredentialsProvider.ManageDomains",
                "com.cloudbees.plugins.credentials.CredentialsProvider.Update",
                "com.cloudbees.plugins.credentials.CredentialsProvider.View",
                "hudson.model.Hudson.Read",
                "hudson.model.Item.Build",
                "hudson.model.Item.Cancel",
                "hudson.model.Item.Configure",
                "hudson.model.Item.Create",
                "hudson.model.Item.Delete",
                "hudson.model.Item.Discover",
                "hudson.model.Item.Move",
                "hudson.model.Item.Read",
                "hudson.model.Item.Workspace",
                "hudson.model.Run.Delete",
                "hudson.model.Run.Replay",
                "hudson.model.Run.Update",
                "hudson.model.View.Configure",
                "hudson.model.View.Create",
                "hudson.model.View.Delete",
                "hudson.model.View.Read",
                "hudson.scm.SCM.Tag",
                "hudson.security.HealthCheck",
                "hudson.security.ThreadDump",
                "hudson.security.View"
        ]
]

def viewers = [
        name : "jenkins-viewer",
        roles: [
                "hudson.model.Hudson.Read",
                "hudson.model.Item.Read",
                "hudson.model.View.Configure",
                "hudson.model.View.Create",
                "hudson.model.View.Delete",
                "hudson.model.View.Read",
                "hudson.security.HealthCheck",
                "hudson.security.View"
        ]
]

def webhook = [
        name : "jenkins-webhook",
        roles: [
                "hudson.model.Item.Build"
        ]
]

def anonymous = [
        name : "anonymous",
        roles: []
]

administrators.name = config?.adminGroup ?: administrators.name
developers.name = config?.developerGroup ?: developers.name
viewers.name = config?.viewersGroup ?: viewers.name
webhook.name = config?.webhookGroup ?: webhook.name

administrators.roles = config?.adminRoles ?: administrators.roles
developers.roles = config?.developerRoles ?: developers.roles
viewers.roles = config?.viewerRoles ?: viewers.roles
anonymous.roles = config?.anonymousRoles ?: anonymous.roles
webhook.roles = config?.webhookRoles ?: webhook.roles

def strategy = new GlobalMatrixAuthorizationStrategy()

[administrators, developers, viewers, anonymous, webhook].each { group ->
    group.roles.each { role ->
        println("Add ${role} to ${group.name}: ${Permission.fromId(role).toString()}")
        if (Permission.fromId(role) != null) {
            strategy.add(Permission.fromId(role), group.name)
        }
    }
}

instance.setAuthorizationStrategy(strategy)
instance.save()
