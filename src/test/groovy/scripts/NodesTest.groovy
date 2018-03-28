package scripts

import hudson.model.Node
import hudson.model.Slave
import hudson.slaves.JNLPLauncher
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.instanceOf
import static org.junit.Assert.assertThat

class NodesTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(NodesTest.class, ["scripts/nodes.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureNodesFromConfig() {
        def node01 = jenkinsRule.jenkins.getNode("node 01") as Slave
        assertThat(node01.getNodeDescription(), equalTo("First node"))
        assertThat(node01.getRemoteFS(), equalTo("/tmp"))
        assertThat(node01.getNumExecutors(), equalTo(1))
        assertThat(node01.getLauncher(), instanceOf(JNLPLauncher))
        assertThat(node01.getLauncher().tunnel, equalTo(':443'))
        assertThat(node01.getLauncher().vmargs, equalTo('-'))
        assertThat(node01.getMode(), equalTo(Node.Mode.NORMAL))
        assertThat(node01.getNodeProperties()[0].getEnvVars()['ANDROID_HOME'] as String, equalTo('/android'))
        assertThat(node01.getNodeProperties()[0].getEnvVars()['JAVA_HOME'] as String, equalTo('/java'))

        def node02 = jenkinsRule.jenkins.getNode("node 02") as Slave
        assertThat(node02.getNodeDescription(), equalTo("Second node"))
        assertThat(node02.getRemoteFS(), equalTo("/var"))
        assertThat(node02.getNumExecutors(), equalTo(1))
        assertThat(node02.getLauncher(), instanceOf(JNLPLauncher))
        assertThat(node02.getMode(), equalTo(Node.Mode.EXCLUSIVE))

        def node03 = jenkinsRule.jenkins.getNode("node 03") as Slave
        def launcher03 = node03.getLauncher();

        assertThat(node03.getNodeDescription(), equalTo("Third node"))
        assertThat(node03.getRemoteFS(), equalTo("/var"))
        assertThat(node03.getNumExecutors(), equalTo(1))
        assertThat(launcher03.class.getCanonicalName(), equalTo("hudson.plugins.sshslaves.SSHLauncher"))
        assertThat(launcher03.getHost(), equalTo("sshHost"))
        assertThat(launcher03.getPort(), equalTo(1234))
        assertThat(launcher03.getJavaPath(), equalTo("sshJavaPath"))
        assertThat(node03.getMode(), equalTo(Node.Mode.EXCLUSIVE))

        def node04 = jenkinsRule.jenkins.getNode("node 04") as Slave
        def launcher04 = node04.getLauncher();

        assertThat(node04.getNodeDescription(), equalTo("Fourth node"))
        assertThat(node04.getRemoteFS(), equalTo("/var"))
        assertThat(node04.getNumExecutors(), equalTo(15))
        assertThat(launcher04.class.getCanonicalName(), equalTo("hudson.plugins.sshslaves.SSHLauncher"))
        assertThat(launcher04.getHost(), equalTo("sshHost"))
        assertThat(launcher04.getPort(), equalTo(22))
        assertThat(node04.getMode(), equalTo(Node.Mode.NORMAL))
    }
}
