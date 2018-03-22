package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class KubernetesTest {
    private static final List SCRIPTS = ["main.groovy", "scripts/kubernetes.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    static void setUp() {
        addScriptToLocalDataZip(KubernetesTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["kubernetes-1.1.4.hpi", "workflow-step-api-2.14.hpi", "credentials-2.1.16.hpi", "durable-task-1.15.hpi", "variant-1.1.hpi", "structs-1.10.hpi"])
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
