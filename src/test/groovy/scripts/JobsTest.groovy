package scripts

import hudson.model.FreeStyleProject
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.hamcrest.collection.IsEmptyCollection.empty
import static org.hamcrest.core.IsNull.notNullValue
import static org.junit.Assert.assertThat

class JobsTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(JobsTest.class, ["scripts/jobs.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jobs-config/test/config.xml"])
    void shouldCreateTestJob() {
        assertThat(jenkinsRule.jenkins.getItem("test"), notNullValue())
    }

    @Test(timeout = 30000L)
    @LocalData
    @ZipTestFiles(files = ["jobs-config/seed/config.xml"])
    void shouldCreateAndRunSeedJob() {
        FreeStyleProject seed = jenkinsRule.jenkins.getItem("seed");
        while (jenkinsRule.jenkins.getQueue().getItems().size() > 0) {
            sleep(500)
        }
        assertThat(seed.getBuilds().lastBuild, notNullValue())
    }

    @Test(timeout = 30000L)
    @LocalData
    @ZipTestFiles(files = ["jobs-config/global-seed/config.xml"])
    void shouldCreateAndRunJobWithNameThatEndsInDashSeed() {
        FreeStyleProject seed = jenkinsRule.jenkins.getItem("global-seed");
        while (jenkinsRule.jenkins.getQueue().getItems().size() > 0) {
            sleep(500)
        }
        assertThat(seed.getBuilds().lastBuild, notNullValue())
    }

    @Test
    void shouldNotErrorWhenNoJobFilesExist() {
        assertThat(jenkinsRule.jenkins.jobNames, empty())
    }

}
