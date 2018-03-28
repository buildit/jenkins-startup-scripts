package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class ConfluenceTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(ConfluenceTest.class, ["scripts/confluence.groovy"])
    }

    @Test
    @LocalData
    @WithPlugin("confluence-publisher-1.8.hpi")
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigurePublishToConfluenceFromConfig() {
        def descriptor = jenkinsRule.instance.getDescriptor("com.myyearbook.hudson.plugins.confluence.ConfluencePublisher");
        def site = descriptor.getSites()[0]
        assertThat(site.url.getHost() as String, equalTo("confluence.sandbox.extranet.group"))
        assertThat(site.username as String, equalTo("arvind.kumar"))
        assertThat(site.password as String, equalTo("somes3cret"))

    }
}
