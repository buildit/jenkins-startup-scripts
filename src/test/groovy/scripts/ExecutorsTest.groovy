package scripts

import jenkins.model.Jenkins
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.assertThat

class ExecutorsTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(ExecutorsTest.class, ["scripts/executors.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldConfigureExecutors() {
        def expected = 3
        def actual = Jenkins.instance.getNumExecutors()

        assertThat(actual).isEqualTo(expected)
    }

}
