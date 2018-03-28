package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

class MesosTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(MesosTest.class, ["scripts/mesos.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["mesos-0.14.1.hpi", "credentials-2.1.16.hpi", "metrics-3.1.2.10.hpi", "jackson2-api-2.8.10.1.hpi", "structs-1.10.hpi"])
    void shouldSetUpHostDetailsFromConfig() {
        def clouds = jenkinsRule.instance.clouds
        def mesosConfig = clouds[0]
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
