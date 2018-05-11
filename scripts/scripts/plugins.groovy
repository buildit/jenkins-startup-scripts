import org.apache.ivy.Ivy
import org.apache.ivy.core.module.descriptor.DefaultDependencyArtifactDescriptor
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor
import org.apache.ivy.core.module.descriptor.DefaultModuleDescriptor
import org.apache.ivy.core.module.id.ModuleRevisionId
import org.apache.ivy.core.report.ResolveReport
import org.apache.ivy.core.resolve.ResolveOptions
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.plugins.parser.xml.XmlModuleDescriptorWriter
import org.apache.ivy.plugins.resolver.URLResolver

import java.security.MessageDigest

DEFAULT_ARTIFACT_PATTERN = "http://updates.jenkins-ci.org/download/plugins/[module]/[revision]/[module].[ext]"
PLUGIN_EXT = "hpi"
PLUGIN_CACHE_DIR = "${jenkinsHome}/plugins-cache"
PLUGINS_DIR = "${jenkinsHome}/plugins"
PLUGIN_LOG = "${PLUGINS_DIR}/plugins.log"
RETRY_COUNT = 5


String hash = getHash(config)

File log = new File(PLUGIN_LOG)

if (log.exists()) {
    if (log.text == hash) {
        println("No changes to list of plugins detected. Nothing to do.")
    } else {
        println("Changes detected to plugin list - contents of ${log.absolutePath} is '${log.text}' new hash is '${hash}'. Please redeploy jenkins instance.")
    }
    return
}

println("No plugin log found. Initiating plugin download")

def pluginsCache = []
config.each { k, v ->
    def artifactPattern = v.artifactPattern ?: DEFAULT_ARTIFACT_PATTERN
    pluginsCache.addAll(resolvePlugins(artifactPattern, v.pluginArtifacts))
}

println("Wiping plugin directory and copying plugins from cache")

new AntBuilder().delete(dir: PLUGINS_DIR, failonerror: true)

pluginsCache.each {
    println("Copying ${it.name} from cache to ${PLUGINS_DIR}/${it.name}")
    new AntBuilder().copy(file: "${it.absolutePath}", tofile: "${PLUGINS_DIR}/${it.name}")
}

println("Writing hash to ${log.absolutePath}")
log.text = hash

println("Initiating restart")
instance.restart()

String hashString(String string) {
    MessageDigest digest = MessageDigest.getInstance('MD5')
    byte[] arrayBytes = digest.digest(string.getBytes("UTF-8"))
    StringBuffer stringBuffer = new StringBuffer()
    for (int i = 0; i < arrayBytes.length; i++) {
        stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1))
    }
    return stringBuffer.toString()
}

def resolvePlugins(pattern, plugins, retries=RETRY_COUNT) {

    IvySettings ivySettings = new IvySettings()
    ivySettings.setDefaultCache(new File("${PLUGIN_CACHE_DIR}"))
    URLResolver resolver = new URLResolver()
    resolver.setM2compatible(true)
    resolver.setName('central')
    resolver.setCheckmodified(true)
    resolver.setChangingMatcher("regexp")
    resolver.addArtifactPattern(pattern)
    ivySettings.addResolver(resolver)
    ivySettings.setDefaultResolver(resolver.getName())
    Ivy ivy = Ivy.newInstance(ivySettings)

    File ivyfile = File.createTempFile('ivy', '.xml')
    ivyfile.deleteOnExit()

    DefaultModuleDescriptor md = DefaultModuleDescriptor.newDefaultInstance(ModuleRevisionId.newInstance('', '', ''))

    plugins.each {
        def bits = it.toString().split(":")
        if (bits.length != 2) {
            throw new IllegalArgumentException("Error parsing plugin pluginArtifact: ${it.toString()}")
        }
        def artifactId = bits[0]
        def version = bits[1]

        DefaultDependencyDescriptor dd = new DefaultDependencyDescriptor(md, ModuleRevisionId.newInstance('', artifactId, version, [changing: 'true']), false, false, true)
        DefaultDependencyArtifactDescriptor dad = new DefaultDependencyArtifactDescriptor(dd, artifactId, PLUGIN_EXT, PLUGIN_EXT, null, [:])
        dd.addDependencyArtifact('default', dad)
        md.addDependency(dd)
    }
    XmlModuleDescriptorWriter.write(md, ivyfile)

    String[] confs = ['default']
    ResolveOptions resolveOptions = new ResolveOptions().setConfs(confs)

    ResolveReport report = ivy.resolve(ivyfile.toURL(), resolveOptions)

    if(report.hasError()) {
        if(retries > 0) {
            newRetryCount = retries - 1
            println("Error resolving plugins. Initiating retry. Retries remaining = ${newRetryCount}")
            return resolvePlugins(pattern, plugins, newRetryCount)
        } else {
            throw new IllegalStateException("Error resolving plugins. See log for details.")
        }
    }

    def results = []
    report.getAllArtifactsReports().each {
        results.add(it.getLocalFile())
    }

    return results
}

def getHash(config) {
    // hash based on all the pluginArtifacts elements in config file
    def pluginArtifactsString = ''
    config.each { k, v ->
        pluginArtifactsString += v.pluginArtifacts.toString().replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", "");
    }
    return hashString(pluginArtifactsString)
}


