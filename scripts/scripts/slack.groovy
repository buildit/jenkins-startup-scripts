import jenkins.model.Jenkins
import net.sf.json.JSONObject

def slackParameters = [
        slackBaseUrl:             config.baseUrl ?: '',
        slackBotUser:             config.botUser ?: 'false',
        slackBuildServerUrl:      config.buildServerUrl ?: '',
        slackRoom:                config.room ?: '',
        slackSendAs:              config.sendAs ?: 'Jenkins',
        slackTeamDomain:          config.teamDomain ?: '',
        slackToken:               '',
        slackTokenCredentialId:   config.tokenCredentialId
]

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// get Slack plugin
def slack = jenkins.getExtensionList(jenkins.plugins.slack.SlackNotifier.DescriptorImpl.class)[0]

// define form and request
JSONObject formData = ['slack': ['tokenCredentialId': config.tokenCredentialId]] as JSONObject
def request = [getParameter: { name -> slackParameters[name] }] as org.kohsuke.stapler.StaplerRequest

// add Slack configuration to Jenkins
slack.configure(request, formData)

// save to disk
slack.save()
jenkins.save()