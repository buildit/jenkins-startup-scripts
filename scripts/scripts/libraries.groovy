import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration

def globalLibsDesc = instance.getDescriptor("org.jenkinsci.plugins.workflow.libs.GlobalLibraries")

def results = []
config?.each { library ->
    def branch = library.value.scm.branch == null ? '*' : library.value.scm.branch
    
    LibraryConfiguration pipeline =
        new LibraryConfiguration(library.key,
            new SCMSourceRetriever(
                new GitSCMSource(
                    null,
                    remote = library.value.scm.url,
                    credentialsId = library.value.scm.credentialsId,
                    includes = branch,
                    excludes = "",
                    ignoreOnPushNotifications = true
                )
            )
        )
    pipeline.setDefaultVersion(library.value.defaultVersion)
    pipeline.setImplicit(library.value.implicit)
    pipeline.setAllowVersionOverride(library.value.allowVersionOverride)
    pipeline.setIncludeInChangesets(false)

    results.add(pipeline)
}
globalLibsDesc.get().setLibraries(results)
