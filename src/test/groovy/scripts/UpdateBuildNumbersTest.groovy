package scripts

import com.github.tomakehurst.wiremock.junit.WireMockRule
import hudson.model.Job
import hudson.triggers.TimerTrigger
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static org.hamcrest.core.IsEqual.equalTo
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class UpdateBuildNumbersTest {

    private static
    final List SCRIPTS = ["main.groovy", "scripts/jobs.groovy", "scripts/env.groovy", "scripts/updatebuildnumbers.groovy", "config/scripts.config"]

    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"
    private static final LAST_BUILD_NUMBER = 10
    public static final int PORT = 51234


    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(PORT))

    @BeforeClass
    static void setUp() {
        addScriptToLocalDataZip(UpdateBuildNumbersTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
    }

    @Test(timeout = 90000L)
    @LocalData
    @ZipTestFiles(files = ["jenkins.config", "jobs-config/test/config.xml", "jobs-config/jobdsl/config.xml"])
    void shouldUpdateNextBuildNumber() {
        givenJobDslIsScheduled()
        givenResponseFromEtcd()
        waitUntilTestJobIsUpdated()
        assertThat(testJob().getNextBuildNumber() as Integer, equalTo(LAST_BUILD_NUMBER + 1))
    }

    private void givenJobDslIsScheduled() {
        def job = jenkinsRule.jenkins.getItemByFullName("jobdsl")
        def causeAction = new hudson.model.CauseAction(new TimerTrigger.TimerTriggerCause())
        jenkinsRule.jenkins.getQueue().schedule(job, 1, causeAction)
    }

    private void waitUntilTestJobIsUpdated() {
        while (testJob() == null || testJob().getNextBuildNumber() == 1) {
            Thread.sleep(2000)
            println 'waiting for test job to be updated'
        }
    }

    private Job testJob() {
        return jenkinsRule.jenkins.getItemByFullName("test")
    }

    private void givenResponseFromEtcd() {
        configureFor("localhost", PORT)
        stubFor(get(urlEqualTo("/v2/keys/statements/jenkins_jobs/test"))
                .willReturn(aResponse()
                .withStatus(200)
                .withBody(etcdResponse())))
    }

    private String etcdResponse() {
        "{\n" +
                "\"action\": \"get\",\n" +
                "\"node\": {\n" +
                "\"key\": \"/statements/jenkins_jobs/test\",\n" +
                "\"value\": \"" + LAST_BUILD_NUMBER + "\",\n" +
                "\"modifiedIndex\": 17,\n" +
                "\"createdIndex\": 17\n" +
                "}\n" +
                "}"
    }

}
