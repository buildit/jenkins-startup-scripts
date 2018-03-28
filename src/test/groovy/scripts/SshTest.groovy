package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class SshTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SshTest.class, ["scripts/ssh.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin("publish-over-ssh-1.14.hpi")
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigurePublishOverSshFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("jenkins.plugins.publish_over_ssh.BapSshPublisherPlugin");
        def configuration = descriptor.getHostConfigurations()[0];
        assertThat(configuration.getName() as String, equalTo("dev-p1-app-01"));
        assertThat(configuration.getUsername() as String, equalTo("honey.bajaj"));
        assertThat(configuration.getRemoteRootDir() as String, equalTo("/home/honey.bajaj"));
        assertThat(configuration.getPort() as int, equalTo(22));
        assertThat(configuration.getTimeout() as int, equalTo(300000));
        assertThat(configuration.isOverrideKey() as boolean, equalTo(false));
        assertThat(configuration.isDisableExec() as boolean, equalTo(false));
        assertThat(configuration.getKeyPath() as String, equalTo(''));
        assertThat(configuration.getKey() as String, containsString('PRIVATE'));
    }
}
