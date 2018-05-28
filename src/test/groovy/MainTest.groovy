import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import utilities.ScriptLoader

import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

class MainTest {

    private static final String SECRET = "c4cad7fd3cf61a3f"

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    def main = ScriptLoader.load("scripts/main.groovy")

    @Before
    void setUp(){
        System.metaClass.static.getenv = { String secret ->
            return [JENKINS_STARTUP_SECRET: SECRET].get(secret)
        }
        System.metaClass.static.getenv = { String key ->
            return [JENKINS_CONFIG_FILE: ""].get(key)
        }
    }

    @Test
    void shouldReturnRelevantSubsectionOfConfiguration(){
        def result = main.evaluateConfigPath(new ConfigSlurper().parse(
                '''tools {
                    hashicorpvault = [
                            url: 'http://vault.server.url',
                            token: 'ENC(60AYACtfA69IZ0IZkf6pK1zbck1shAXB2Dxh94569eL55+DwYX6G6A==)',
                    ]
                }'''), "config?.tools?.hashicorpvault")
        assertThat(result.url as String, equalTo("http://vault.server.url"))
    }

    @Test
    void shouldReturnEmptyMapWhenRelevantSubsectionDoesNotExist(){
        def result = main.evaluateConfigPath(new ConfigSlurper().parse(
                '''tools {
                    hashicorpvault = [
                            url: 'http://vault.server.url',
                            token: 'ENC(60AYACtfA69IZ0IZkf6pK1zbck1shAXB2Dxh94569eL55+DwYX6G6A==)',
                    ]
                }'''), "config?.credentials")
        assertThat(result.size() as Integer, equalTo(0))
    }
}