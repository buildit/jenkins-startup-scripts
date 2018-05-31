#!/usr/bin/env groovy

import jenkins.model.GlobalConfiguration
import org.jenkinsci.plugins.github_branch_source.*
import jenkins.branch.OrganizationFolder
import org.jenkinsci.plugins.github_branch_source.BranchDiscoveryTrait
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator
import org.jenkinsci.plugins.github_branch_source.OriginPullRequestDiscoveryTrait
import org.jenkinsci.plugins.github_branch_source.GitHubConfiguration
import com.cloudbees.plugins.credentials.CredentialsProvider
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials

import static jenkins.model.Jenkins.instance as jenkins

config.organisations.each {
    createOrganisationFolder(it)
}

void createOrganisationFolder(organisation) {

    if (isNewEndpoint(organisation)) {
        addEndpoint(organisation)
    }

    def folder = jenkins.createProject(OrganizationFolder, organisation.name)
    folder.displayName = organisation.displayName
    folder.description = organisation.description

    GitHubSCMNavigator navigator = new GitHubSCMNavigator(organisation.name)

    navigator.credentialsId = organisation.project.credentialsId
    navigator.scanCredentialsId = organisation.project.scanCredentialsId

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

static boolean isNewEndpoint(organisation) {
    return (organisation.containsKey('apiEndpointUrl') && organisation.containsKey('apiEndpointName'))
}

void addEndpoint(organisation) {
    GitHubConfiguration gitHubConfig = GlobalConfiguration.all().get(GitHubConfiguration.class)

    Endpoint gheApiEndpoint = new Endpoint(organisation.apiEndpointUrl, organisation.apiEndpointName)
    List<Endpoint> endpointList = new ArrayList<Endpoint>()
    endpointList.add(gheApiEndpoint)
    gitHubConfig.setEndpoints(endpointList)
}







