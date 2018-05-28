

import groovy.text.SimpleTemplateEngine
import com.buildit.encryptor.Encryptor
import jenkins.model.Jenkins
import org.codehaus.groovy.control.CompilerConfiguration
import static Constants.*

import static groovy.io.FileType.FILES

SECRET = "c4cad7fd3cf61a3f"

class Constants {
    static final DEFAULT_FILE = 'jenkins.config'
    static final CONFIGFILE_SUFFIX = ['.config']
}

def baseDirectory = baseDirectory()
def classLoader = getAugmentedClassLoader("${baseDirectory}/lib")

def jenkinsConfig = getConfig([jenkinsHome: jenkinsHome()])

def main = load("${baseDirectory}/main.groovy", [jenkinsConfig: jenkinsConfig, baseDirectory: baseDirectory], classLoader)

main.main()

def jenkinsHome(){
    return Jenkins.getInstance().getProperties().get("rootPath").toString()
}

Object getConfig(bindingVariables) {

    def configStrings = configStrings(bindingVariables)
    def config = initialiseConfig()
    configStrings.each {
        config.merge(parse(it, bindingVariables))
    }
    println("")
    println("Final config is \n${config}")
    println("")
    return config
}

def initialiseConfig(){
    File tmp = File.createTempFile("temp",".tmp")
    new ConfigSlurper().parse(tmp.toURI().toURL())
}

private Serializable configStrings(bindingVariables) {
    def configFileLocationString = System.getenv("TEST_CONFIG_FILE") ? System.getenv("TEST_CONFIG_FILE") : null

    if (!configFileLocationString) {
        configFileLocationString = DEFAULT_FILE
    }

    def configStrings = []
    configFileLocationString.toString().tokenize(",").findAll { CONFIGFILE_SUFFIX.contains(getSuffix(it)) }.each {
        configStrings.addAll(toString("${bindingVariables.jenkinsHome}/${it}"))
    }
    return configStrings
}

def toString(String location){
    def result = (!new File(location).exists() || new File(location).isDirectory()) ? "" : new File(location).text
    return result
}

final String getSuffix(string){
    List bits = string.tokenize(".")
    return ".${bits.size() > 1 ? bits.get(bits.size()-1) : ''}"
}

def parse(configString, bindingVariables){
    println("")
    println("Parsing config \n${configString}")
    println("")
    def decryptedConfigString = decryptConfigValues(configString as String, SECRET as String)
    def boundConfigString = bindTemplateValues(decryptedConfigString, bindingVariables)
    def config = new ConfigSlurper().parse(boundConfigString)
    return config
}

def bindTemplateValues(configString, binding){
    return new SimpleTemplateEngine().createTemplate(configString).make(binding as Map).toString()
}

def decryptConfigValues(String config, String secret){
    println("")
    println("Using SECRET ${secret} to decrypt")
    println("")
    def matcher = config =~ /(?s)ENC\(([^\)]*)\)(?)/
    StringBuffer sb = new StringBuffer(config.length())
    while (matcher.find()) {
        matcher.groupCount()
        def match = matcher.group(1)
        def unencrypted = decryptPassword(match, secret)
        matcher.appendReplacement(sb, unencrypted)
    }
    matcher.appendTail(sb)
    return sb.toString().trim()
}

private decryptPassword(password, secret) {
    Encryptor.decrypt(secret, password)
}

def load(script, args=[:], classLoader=this.class.classLoader){
    CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
    def shell = new GroovyShell(classLoader, new Binding(), compilerConfiguration)
    def file = new File("${script}")
    try{
        if(file.exists()){
            println("Loading script : ${file.name}")
            shell.getClassLoader().addURL(file.parentFile.toURI().toURL())
            def result = shell.evaluate("new " + file.name.split("\\.", 2)[0] + "()")
            args.each{ k, v -> result."${k}" = v}
            return result
        }
    }catch(Exception e){
        println("Error loading script : ${e.getMessage()}")
        e.printStackTrace()
    }
}

def baseDirectory(){
    File thisScript = new File(getClass().protectionDomain.codeSource.location.path)
    return thisScript.getParent()
}

def getAugmentedClassLoader(String libDirectory){
    def additionalJars = []
    def lib = new File(libDirectory)
    if(lib.exists()){
        lib.traverse(type: FILES, maxDepth: 0) {
            additionalJars.add(it.toURI().toURL())
        }
    }
    return new URLClassLoader(additionalJars as URL[], this.class.classLoader)
}

private File createTempDirectory() {
    File dir = new File("${System.getProperty("java.io.tmpdir")}/${UUID.randomUUID().toString()}")
    if(!dir.exists()) dir.mkdirs()
    return dir
}