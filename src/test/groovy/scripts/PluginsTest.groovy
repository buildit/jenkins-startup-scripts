package scripts

import com.google.common.net.MediaType
import jenkins.model.Jenkins
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.nio.file.Files
import java.nio.file.Paths

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.ResourcePath.resourcePath

class PluginsTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/plugins.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"
    private static final String RESTART_LOG = "restart.log"
    private static final String PLUGIN_LOG = "plugins/plugins.log"

    private static final Server SERVER = createPluginServer()

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    static void setUp() {
        addScriptToLocalDataZip(PluginsTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
        Jenkins.metaClass.static.restart = {
            def jenkinsHome = Jenkins.getInstance().getProperties().get("rootPath").toString()
            File jenkinsConfig = new File("${jenkinsHome}/${RESTART_LOG}")
            jenkinsConfig.withWriter { writer ->
                writer.write("Restart Triggered!")
            }
        }
    }

    @AfterClass
    static void tearDown() {
        SERVER.stop()
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config", "lib/ivy-2.4.0.jar"])
    void shouldCopyPluginsToJenkinsHomeAndRestart() {
        def jenkinsHome = Jenkins.getInstance().getProperties().get("rootPath").toString()

        def pluginsDir = "${jenkinsHome}/plugins/"
        def pluginsCache = "${jenkinsHome}/plugins-cache/"

        String mavenPlugin = "maven-plugin-2.7.1.hpi"
        String activeDirectory = "active-directory-2.0.hpi"
        String aceEditor = "ace-editor-1.0.1.hpi"

        assertThat(new File("${jenkinsHome}/${RESTART_LOG}").exists(), is(true))
        assertThat(new File("${jenkinsHome}/${PLUGIN_LOG}").exists(), is(true))

        assertEquals("86f5739f6ea2dc30910a89b0d3f7047d",
                new String(Files.readAllBytes(Paths.get("${jenkinsHome}/${PLUGIN_LOG}"))))

        assertThat(new File("${pluginsDir}/${mavenPlugin}").exists(), is(true))
        assertThat(new File("${pluginsDir}/${activeDirectory}").exists(), is(true))
        assertThat(new File("${pluginsDir}/${aceEditor}").exists(), is(true))


        assertThat(fileExists(pluginsCache, mavenPlugin), is(true))
        assertThat(fileExists(pluginsCache, activeDirectory), is(true))
        assertThat(fileExists(pluginsCache, aceEditor), is(true))
    }

    static createPluginServer() throws Exception {
        Server server = new Server(4567)
        HandlerList handlers = new HandlerList()
        handlers.setHandlers([new AbstractHandler() {
            void handle(String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException,
                    ServletException {
                response.setHeader("Content-Disposition", "attachmentfilename=\"plugin.zip\"")
                response.setContentType(MediaType.OCTET_STREAM.toString())
                File plugin = new File(resourcePath(request.getPathInfo().replaceFirst("/", ""), "plugins"))
                response.getOutputStream().write(plugin.getBytes())
            }
        }] as Handler[])
        server.setHandler(handlers)
        server.start()
        return server
    }

    def fileExists(dir, fileName) {
        def found = false
        new File(dir).eachFileRecurse(groovy.io.FileType.FILES) {
            if (it.absolutePath.endsWith(fileName as String)) {
                found = true
            }
        }
        return found
    }
}
