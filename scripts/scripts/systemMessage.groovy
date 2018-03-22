import hudson.markup.RawHtmlMarkupFormatter
import jenkins.model.Jenkins

def instance = Jenkins.getInstance()

instance.setSystemMessage(config.message)

disableSyntaxHighlighting = true
instance.setMarkupFormatter(new RawHtmlMarkupFormatter(!disableSyntaxHighlighting))

instance.save()
