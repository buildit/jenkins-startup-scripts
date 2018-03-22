package utilities

import org.codehaus.groovy.control.CompilerConfiguration

class ScriptLoader {

    static load(script, args=[:], classLoader=this.class.classLoader){
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration()
        def shell = new GroovyShell(classLoader, new Binding(), compilerConfiguration)
        def file = new File("${script}")
        if(!script.toString().startsWith("/")){
            file = new File("${System.getProperty("user.dir")}/${script}")
        }
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
}
