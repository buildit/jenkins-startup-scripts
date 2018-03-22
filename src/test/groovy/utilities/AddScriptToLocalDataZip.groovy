package utilities

import org.junit.Test
import org.zeroturnaround.zip.FileSource
import org.zeroturnaround.zip.ZipEntrySource
import org.zeroturnaround.zip.ZipUtil

import static utilities.ResourcePath.resourcePath

class AddScriptToLocalDataZip {

    public static void addScriptToLocalDataZip(Class testClass, String scriptName, String scriptPath, String targetPath, Map scriptsToPath=[:]) {
        addScriptToLocalDataZip(testClass, scriptName, [scriptPath], targetPath)
    }

    public static void addScriptToLocalDataZip(Class testClass, List scriptNames, String scriptPath, String targetPath, Map scriptsToPath=[:]){
        def allTestFiles = [:]
        scriptNames.each {
            def scriptSource = new FileSource("${targetPath}/${it}", new File("${scriptPath}/${it}"))
            allTestFiles.put("${scriptPath}/${it}", scriptSource)
        }
        scriptsToPath.each { k, v ->
            def scriptSource = new FileSource("${targetPath}/${k}", new File("${v}"))
            allTestFiles.put("${targetPath}/${k}", scriptSource)
        }
        def directoryPath = resourcePath(testClass.getCanonicalName().replace('.', '/')) as String
        if(directoryPath == null){
            return
        }
        def directory = new File(directoryPath)

        for(def it : testClass.getMethods()) {
            if(it.getAnnotation(Test) != null){
                def zipTestFiles = it.getAnnotation(ZipTestFiles)
                def zip = new File(directory, "${it.getName()}.zip")
                def specificTestFiles = [:]
                specificTestFiles.putAll(allTestFiles)

                if(zipTestFiles != null) {
                    zipTestFiles.files().each() { filename ->
                        File file = new File(directory, filename)
                        specificTestFiles.put(file.absolutePath, new FileSource(filename, file))
                    }
                }
                println("Creating ${zip.getName()} to hold ${allTestFiles}")
                ZipUtil.pack(specificTestFiles.values() as ZipEntrySource[], zip)
            }
        }
    }
}


