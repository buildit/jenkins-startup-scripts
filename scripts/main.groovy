import jenkins.model.Jenkins

import static groovy.io.FileType.FILES

def main(){

    def scriptsFile = new File("${baseDirectory}/config/scripts.config")
    def scriptsConfig = scriptsFile.exists() ? new ConfigSlurper().parse(scriptsFile.toURI().toURL()) : [:]

    def classLoader = getAugmentedClassLoader("${baseDirectory}/lib")

    scriptsConfig.scripts.each {
        def config = evaluateConfigPath(jenkinsConfig, it.value.configPath)
        if (config) {
            runScript("${baseDirectory}/${it.value.script}", [config: config, instance: Jenkins.getInstance(), jenkinsHome: jenkinsHome()], classLoader)
        }
    }
}

def jenkinsHome(){
    return Jenkins.getInstance().getProperties().get("rootPath").toString()
}

def runScript(script, args, classLoader=this.class.classLoader){
    def bindings = new Binding()
    args.each{ k, v -> bindings.setVariable(k.toString(), v) }
    def shell = new GroovyShell(classLoader as ClassLoader, bindings)
    def file = new File("${script}")
    try{
        if(file.exists()){
            println("Running initialisation script : ${file.name}")
            shell.parse(file).run()
        }
    }catch(Exception e){
        println("Error running initialisation script : ${e.getMessage()}")
        e.printStackTrace()
    }
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

def evaluateConfigPath(jenkinsConfig, script){
    if(!script){
        return true
    }
    def bindings = new Binding()
    bindings.setVariable("config", jenkinsConfig)
    bindings.setVariable("script", script)
    def shell = new GroovyShell(this.class.classLoader, bindings)
    def result = shell.parse("return ${script}").run()
    return result
}
