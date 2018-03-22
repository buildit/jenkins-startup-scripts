#!/usr/bin/env groovy

import jenkins.model.GlobalConfiguration
import org.jenkinsci.plugins.github_branch_source.*

GitHubConfiguration gitHubConfig = GlobalConfiguration.all().get(GitHubConfiguration.class)

Endpoint gheApiEndpoint = new Endpoint(config.apiUrl, config.name)
List<Endpoint> endpointList = new ArrayList<Endpoint>()
endpointList.add(gheApiEndpoint)
gitHubConfig.setEndpoints(endpointList)
