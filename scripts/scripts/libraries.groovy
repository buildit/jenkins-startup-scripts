import hudson.plugins.git.BranchSpec
import hudson.plugins.git.GitSCM
import hudson.plugins.git.SubmoduleConfig
import hudson.plugins.git.extensions.GitSCMExtension
import hudson.scm.SCM
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMRetriever

def globalLibsDesc = instance.getDescriptor("org.jenkinsci.plugins.workflow.libs.GlobalLibraries")

def results = []
config?.each { library ->
    def branch = library.value.scm.branch == null ? '*/master' : library.value.scm.branch
    SCM scm = new GitSCM(GitSCM.createRepoList(library.value.scm.url, library.value.scm.credentialsId),
            Collections.singletonList(new BranchSpec(branch)),
            false, Collections.<SubmoduleConfig> emptyList(),
            null, null, Collections.<GitSCMExtension> emptyList())

    SCMRetriever retriever = new SCMRetriever(scm)
    LibraryConfiguration pipeline = new LibraryConfiguration(library.key, retriever)
    pipeline.setDefaultVersion(library.value.defaultVersion)
    pipeline.setImplicit(library.value.implicit)
    pipeline.setAllowVersionOverride(library.value.allowVersionOverride)
    results.add(pipeline)
}
globalLibsDesc.get().setLibraries(results)
