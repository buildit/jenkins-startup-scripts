import com.dabsquared.gitlabjenkins.connection.*
import jenkins.model.Jenkins

GitLabConnectionConfig descriptor = (GitLabConnectionConfig) Jenkins.getInstance().getDescriptor(GitLabConnectionConfig.class)

descriptor.getConnections().clear()

config.each { k, v ->
    GitLabConnection gitLabConnection = new GitLabConnection(k,
            v.url,
            v.apiTokenId,
            v.ignoreCertificateErrors == null ? true : v.ignoreCertificateErrors,
            v.connectionTimeout ?: 10,
            v.readTimeout ?: 10)
    descriptor.addConnection(gitLabConnection)
}

descriptor.save()
