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

class SshTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/ssh.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts";
    private static final String SCRIPT_TARGET = "init.groovy.d";

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(SshTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
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
