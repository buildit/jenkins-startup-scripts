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

class MultiCloudTest {
    private static
    final List SCRIPTS = ["main.groovy", "scripts/kubernetes.groovy", "scripts/mesos.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    static void setUp() {
        addScriptToLocalDataZip(MultiCloudTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["mesos-0.14.1.hpi", "credentials-2.1.16.hpi", "metrics-3.1.2.10.hpi", "jackson2-api-2.8.10.1.hpi", "structs-1.10.hpi",
            "kubernetes-1.1.4.hpi", "workflow-step-api-2.14.hpi", "credentials-2.1.16.hpi", "durable-task-1.15.hpi", "variant-1.1.hpi"])
    void shouldSetUpHostDetailsFromConfig() {
        def clouds = jenkinsRule.instance.clouds
        def k8sConfig = clouds[0]
        assertThat(k8sConfig.name as String, equalTo('My Cloud'))
        assertThat(k8sConfig.namespace as String, equalTo('default'))
        assertThat(k8sConfig.serverUrl as String, equalTo('https://rancher.com/r/projects/1a120948/kubernetes:6443'))
        assertThat(k8sConfig.jenkinsUrl as String, equalTo('http://jenkins-k8s.platform.com/'))
        assertThat(k8sConfig.serverCertificate as String, equalTo('QmFzaWMgT0VFMk1UWkZSRVZHTlVKQk9UUkROVGM0TjBNNldtZFRjRlZ5TkVRM19HOXFRMVkzWkVWQldHbzVOMU5YZEdoRk5GcEJaVTVSWWpGS1lsVkJUQT08'))
        assertThat(k8sConfig.credentialsId as String, equalTo('credentialsId'))
        assertThat(k8sConfig.jenkinsTunnel as String, equalTo('jenkinsTunnel'))
        assertThat(k8sConfig.skipTlsVerify as Boolean, equalTo(true))
        assertThat(k8sConfig.containerCap as Integer, equalTo(100))
        assertThat(k8sConfig.retentionTimeout as Integer, equalTo(300))
        assertThat(k8sConfig.connectTimeout as Integer, equalTo(300))
        assertThat(k8sConfig.readTimeout as Integer, equalTo(300))

        def mesosConfig = clouds[1]
        assertThat(mesosConfig.nativeLibraryPath as String, equalTo('/usr/lib/libmesos.so'))
        assertThat(mesosConfig.master as String, equalTo('10.113.140.187:5050'))
        assertThat(mesosConfig.frameworkName as String, equalTo('jenkins'))
        assertThat(mesosConfig.slavesUser as String, equalTo('jenkins'))
        assertThat(mesosConfig.jenkinsURL as String, equalTo('http://mesos3.platform.com/'))
        assertThat(mesosConfig.slaveInfos[0].labelString as String, equalTo('label'))
        assertThat(mesosConfig.slaveInfos[0].slaveMem as Integer, equalTo(1024))
        assertThat(mesosConfig.slaveInfos[0].slaveCpus as Float, equalTo(0.2F))


        assertThat(mesosConfig.slaveInfos[0].minExecutors as Integer, equalTo(3))
        assertThat(mesosConfig.slaveInfos[0].maxExecutors as Integer, equalTo(5))
    }
}
