package scripts

import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.jvnet.hudson.test.JenkinsRule
import org.jvnet.hudson.test.recipes.LocalData
import utilities.ZipTestFiles

import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermissions

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.startsWith
import static org.junit.Assert.assertThat
import static utilities.AddScriptToLocalDataZip.addScriptToLocalDataZip
import static utilities.HttpJsonServer.startServer
import static utilities.ResourcePath.resourcePath

class FilesTest {

    private static final List SCRIPTS = ["main.groovy", "scripts/files.groovy", "config/scripts.config"]
    private static final String SCRIPT_PATH = "scripts"
    private static final String SCRIPT_TARGET = "init.groovy.d"

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @BeforeClass
    public static void setUp() {
        addScriptToLocalDataZip(FilesTest.class, SCRIPTS, SCRIPT_PATH, SCRIPT_TARGET, ["loader.groovy": resourcePath("loader/local.groovy", "")])
        def defaultVaultPath = "${resourcePath("root", "FilesTest/default") as String}".replaceAll("root", "")
        // start server from root/default of resources
        def specificVaultPath = "${resourcePath("root", "FilesTest/specific") as String}".replaceAll("root", "")
        // start server from root/specific of resources
        startServer("${defaultVaultPath}", 51234)
        startServer("${specificVaultPath}", 51235)
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldCreateFileInLocationWithContents() {
        def jenkinsHome = jenkinsRule.instance.getRootDir()
        def file = new File("${jenkinsHome}/.ssh/id_rsa")
        try {
            def permissions = PosixFilePermissions.toString(Files.getPosixFilePermissions(file.toPath()))
            assertThat(permissions, equalTo("rw-------"))
        } catch (UnsupportedOperationException e) {
        }
        assertThat(file.text as String, startsWith("-----BEGIN RSA PRIVATE KEY----"))
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldHandleCreatingFileInRootDirectory() {
        def file = new File("settings.xml")
        try {
            def permissions = PosixFilePermissions.toString(Files.getPosixFilePermissions(file.toPath()))
            assertThat(permissions, equalTo("rw-------"))
        } catch (UnsupportedOperationException e) {
        }
        assertThat(file.text as String, startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))
        file.deleteOnExit()
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldHandleCreationOfEmptyFiles() {
        def file = new File("empty.txt")
        try {
            def permissions = PosixFilePermissions.toString(Files.getPosixFilePermissions(file.toPath()))
            assertThat(permissions, equalTo("rwxrwxrwx"))
        } catch (UnsupportedOperationException e) {
        }
        assertThat(file.text as String, equalTo(""))
        file.deleteOnExit()
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldCreateFileInLocationWithContentsFromVaultWithBase64Encoding() {
        def jenkinsHome = jenkinsRule.instance.getRootDir()
        def file = new File("${jenkinsHome}/.ssh/id_rsa_vault")
        try {
            def permissions = PosixFilePermissions.toString(Files.getPosixFilePermissions(file.toPath()))
            assertThat(permissions, equalTo("rw-------"))
        } catch (UnsupportedOperationException e) {
        }
        assertThat(file.text as String, startsWith("-----BEGIN RSA PRIVATE KEY----"))
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldCreateFileInLocationWithContentsFromVaultUsingDefaultConfig() {
        def jenkinsHome = jenkinsRule.instance.getRootDir()
        def file = new File("${jenkinsHome}/.ssh/passcode")
        try {
            def permissions = PosixFilePermissions.toString(Files.getPosixFilePermissions(file.toPath()))
            assertThat(permissions, equalTo("rw-------"))
        } catch (UnsupportedOperationException e) {
        }
        assertThat(file.text as String, startsWith("R3a11yS3cr3t"))
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    void shouldCreateFileInLocationWithContentsFromVault() {
        def jenkinsHome = jenkinsRule.instance.getRootDir()
        def file = new File("${jenkinsHome}/.ssh/password")
        try {
            def permissions = PosixFilePermissions.toString(Files.getPosixFilePermissions(file.toPath()))
            assertThat(permissions, equalTo("rw-------"))
        } catch (UnsupportedOperationException e) {
        }
        assertThat(file.text as String, startsWith("Sup3rS3cr3t"))
    }
}

