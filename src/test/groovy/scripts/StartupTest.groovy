package scripts

import org.junit.Rule
import org.jvnet.hudson.test.JenkinsRule

import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip

class StartupTest {

    public static final List DEFAULT_SCRIPTS = ["main.groovy", "config/scripts.config", "loader.groovy"]
    public static final String SCRIPT_PATH = "scripts"
    public static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    public static void setUp(Class clazz, List scripts) {
        def allScripts = []
        allScripts.addAll(DEFAULT_SCRIPTS)
        allScripts.addAll(scripts)
        addScriptToLocalDataZip(clazz, allScripts, SCRIPT_PATH, SCRIPT_TARGET)
    }
}
