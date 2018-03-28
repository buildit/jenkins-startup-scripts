package utilities

import org.junit.Test
import org.zeroturnaround.zip.FileSource
import org.zeroturnaround.zip.ZipEntrySource
import org.zeroturnaround.zip.ZipUtil

import static utilities.ResourcePath.resourcePath

class AddScriptToLocalDataZip {

    public static void addScriptToLocalDataZip(Class testClass, List scriptNames, String scriptPath, String targetPath){
        def allTestFiles = [:]
        scriptNames.each {
            def file = new File("${scriptPath}/${it}")
            if(!file.exists()){
                file = new File(resourcePath("${it}"))
            }
            def scriptSource = new FileSource("${targetPath}/${it}", file)
            allTestFiles.put("${scriptPath}/${it}", scriptSource)
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


