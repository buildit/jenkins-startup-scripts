package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.MatcherAssert.assertThat

class MasterTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(MasterTest.class, ["scripts/master.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldLabelMasterFromConfig() {
        assertThat(jenkinsRule.getInstance().labelString, equalTo("label1 label2"))
    }
}
