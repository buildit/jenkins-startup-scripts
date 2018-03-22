import hudson.plugins.jira.JiraProjectProperty
import hudson.plugins.jira.JiraSite
import jenkins.model.Jenkins

def instance = Jenkins.getInstance()
def descriptor = instance.getDescriptorByType(JiraProjectProperty.DescriptorImpl)

descriptor.@sites.clear()

config?.sites?.each { name, site ->
    println("Processing JIRA site: ${name}")
    JiraSite jiraSite = new JiraSite(
            new URL(site.url),
            new URL(site.alternativeUrl),
            site.userName,
            site.password,
            site.supportsWikiStyleComment,
            site.recordScmChanges,
            site.userPattern,
            site.updateJiraIssueForAllStatus,
            site.groupVisibility,
            site.roleVisibility,
            site.useHTTPAuth)
    descriptor.setSites(jiraSite)
}


descriptor.save()
