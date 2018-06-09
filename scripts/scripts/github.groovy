#!/usr/bin/env groovy
import jenkins.branch.OrganizationFolder
import jenkins.model.GlobalConfiguration
import org.jenkinsci.plugins.github_branch_source.*
import static jenkins.model.Jenkins.instance as jenkins

addEndpoints()

config.organisations.each {
    createOrganisationFolder(it)
}

void createOrganisationFolder(organisation) {

    def orgName = organisation.name
    def folder = jenkins.getItem(orgName)

    if(!folder) {
        println "project ${orgName} doesn't exist, creating project"
        folder = jenkins.createProject(OrganizationFolder, orgName)
    }
    else {
        // for some weird reason an 'instanceof' check doesn't work in a running Jenkins...
        def className = folder.getClass().getCanonicalName()
        println "project ${orgName} already exists as type:" + folder.getClass().getCanonicalName()
        if (className != 'jenkins.branch.OrganizationFolder') {
            throw new RuntimeException("A job already exists with the organisation name ${orgName}")
        }
    }

    folder.displayName = organisation.displayName
    folder.description = organisation.description

    GitHubSCMNavigator navigator = new GitHubSCMNavigator(organisation.project.owner)

    navigator.credentialsId = organisation.project.credentialsId

    navigator.apiUri = lookupApiUri(organisation.project.apiEndpoint)
    navigator.traits = [
            new jenkins.scm.impl.trait.WildcardSCMSourceFilterTrait(organisation.project.repositoryNamePattern, ''),
            new jenkins.scm.impl.trait.RegexSCMHeadFilterTrait('.*'),
            new BranchDiscoveryTrait(3),
            new OriginPullRequestDiscoveryTrait(3),
    ]

    if (organisation.containsKey('jenkinsfiles')) {
        // clear out defaults
        folder.getProjectFactories().clear()
        organisation.jenkinsfiles.each { value ->
            def factory = new org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProjectFactory()
            factory.setScriptPath(value)
            folder.getProjectFactories().add(factory)
        }
    }

    folder.navigators.replace(navigator)
    jenkins.save()
}

String lookupApiUri(String apiEndpoint) {
    GitHubConfiguration gitHubConfig = GitHubConfiguration.get()
    gitHubConfig.getEndpoints().findResult { it.name == apiEndpoint ? it.apiUri : null }
}

void addEndpoints() {
    GitHubConfiguration gitHubConfig = GlobalConfiguration.all().get(GitHubConfiguration.class)
    gitHubConfig.getEndpoints().clear()

    config.githubEnterpriseEndpoints.each { endpoint ->
        Endpoint gheApiEndpoint = new Endpoint(endpoint.url, endpoint.name)
        gitHubConfig.updateEndpoint(gheApiEndpoint)
    }
}







