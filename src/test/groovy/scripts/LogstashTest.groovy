package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class LogstashTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(LogstashTest.class, ["scripts/logstash.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin(["logstash-1.3.0.hpi", "mask-passwords-2.10.1.hpi", "structs-1.14.hpi", "junit-1.23.hpi", "script-security-1.40.hpi",
            "workflow-step-api-2.14.hpi", "workflow-api-2.26.hpi", "scm-api-2.2.6.hpi"])
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureLogstashFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor('jenkins.plugins.logstash.LogstashInstallation')
        assertThat(descriptor.type as String, equalTo('SYSLOG'))
        assertThat(descriptor.host as String, equalTo('10.113.140.169'))
        assertThat(descriptor.port as Integer, equalTo(5122))
        assertThat(descriptor.key as String, equalTo('logstash'))
    }
}
