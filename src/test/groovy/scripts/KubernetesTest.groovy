package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

class KubernetesTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(KubernetesTest.class, ["scripts/kubernetes.groovy"])
    }
    
    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["kubernetes-1.5.2.hpi", "workflow-step-api-2.14.hpi", "credentials-2.1.16.hpi", "durable-task-1.16.hpi", "variant-1.1.hpi", "structs-1.14.hpi",
            "kubernetes-credentials-0.3.0.hpi", "plain-credentials-1.4.hpi"])
    void shouldSetUpHostDetailsFromConfig() {

        def clouds = jenkinsRule.instance.clouds
        def cloud = clouds[0]
        assertThat(cloud.name as String, equalTo('My Cloud'))
        assertThat(cloud.namespace as String, equalTo('default'))
        assertThat(cloud.serverUrl as String, equalTo('https://rancher.com/r/projects/1a120948/kubernetes:6443'))
        assertThat(cloud.jenkinsUrl as String, equalTo('http://jenkins-k8s.platform.com/'))
        assertThat(cloud.serverCertificate as String, equalTo('QmFzaWMgT0VFMk1UWkZSRVZHTlVKQk9UUkROVGM0TjBNNldtZFRjRlZ5TkVRM19HOXFRMVkzWkVWQldHbzVOMU5YZEdoRk5GcEJaVTVSWWpGS1lsVkJUQT08'))
        assertThat(cloud.credentialsId as String, equalTo('credentialsId'))
        assertThat(cloud.jenkinsTunnel as String, equalTo('jenkinsTunnel'))
        assertThat(cloud.skipTlsVerify as Boolean, equalTo(true))
        assertThat(cloud.containerCap as Integer, equalTo(100))
        assertThat(cloud.retentionTimeout as Integer, equalTo(300))
        assertThat(cloud.connectTimeout as Integer, equalTo(300))
        assertThat(cloud.readTimeout as Integer, equalTo(300))
        def podTemplate = cloud.templates[0]
        assertThat(podTemplate.inheritFrom as String, equalTo("base"))
        assertThat(podTemplate.name as String, equalTo("myPod"))
        assertThat(podTemplate.namespace as String, equalTo("default"))
        assertThat(podTemplate.envVars.size() as Integer, equalTo(2))

    }
}
