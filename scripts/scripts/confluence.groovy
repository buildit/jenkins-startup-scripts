import com.myyearbook.hudson.plugins.confluence.ConfluenceSite

def confluencePublisher = instance.getDescriptor("com.myyearbook.hudson.plugins.confluence.ConfluencePublisher")

def List<ConfluenceSite> sites = []

config.each { site ->

    def confSite = new com.myyearbook.hudson.plugins.confluence.ConfluenceSite(
            new URL(site.url), site.username, site.password
    )

    sites.add(confSite)
}

confluencePublisher.setSites(sites)
confluencePublisher.save()
