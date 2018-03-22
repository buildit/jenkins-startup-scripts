import com.splunk.splunkjenkins.*
import com.splunk.splunkjenkins.model.*
import jenkins.model.GlobalConfiguration

if (config) {
    SplunkJenkinsInstallation splunkConfig = GlobalConfiguration.all().get(SplunkJenkinsInstallation.class)
    splunkConfig.setEnabled(true)
    splunkConfig.setHost(config.splunkHostUrl ?: null)
    splunkConfig.setPort(config.splunkHostPort ?: null)
    splunkConfig.setToken(config.token ?: null)
    splunkConfig.setUseSSL(config.useSSL)
    splunkConfig.setMetadataHost(config.jenkinsMasterHostname ?: null)
    splunkConfig.setRawEventEnabled(config.rawEventsEnabled ?: null)
    splunkConfig.setMetadataSource(config.eventSource ?: null)
    splunkConfig.setSplunkAppUrl(config.splunkJenkinsAppUrl ?: null)
    splunkConfig.setMaxEventsBatchSize(config.maxEventsBatchSize ?: null)
    splunkConfig.setRetriesOnError(config.retriesOnError ?: null)
    splunkConfig.setIgnoredJobs(config.ignoredJobNamesPattern ?: null)
    splunkConfig.setScriptPath(config.scriptPath ?: null)
    splunkConfig.setScriptContent(config.scriptContent ?: null)
    if (config.customMetaData) {
        def customMetaData = []
        config.customMetaData.each {
            customMetaData.add(new MetaDataConfigItem(it.dataSource as String, it.configItem as String, it.value as String))
        }
        splunkConfig.metadataItemSet = customMetaData
    }
    splunkConfig.save()
}
