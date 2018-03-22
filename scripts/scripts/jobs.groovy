import hudson.triggers.TimerTrigger
import jenkins.model.Jenkins

import javax.xml.transform.stream.StreamSource

import static groovy.io.FileType.DIRECTORIES
import static groovy.io.FileType.FILES

def jobDir = new File("${jenkinsHome}/jobs-config")

createJobs(dirMap(jobDir))
runSeed()

def createJobs(map) {
    map.each {
        jobName, file ->
            def stream = new ByteArrayInputStream(file.text.getBytes())
            def job = Jenkins.getInstance().getItem(jobName)
            if (job) {
                println("Updating job with name ${jobName}")
                job.updateByXml(new StreamSource(stream))
                job.save()
            } else {
                println("Creating job with name ${jobName}")
                Jenkins.getInstance().createProjectFromXML(jobName, stream)
            }
    }
}

def runSeed() {
    instance.items.findAll { job -> job.name.equals("seed") || job.name.endsWith("-seed") }.each {
        job ->
            println("Found job with name ${job.getName()}. Scheduling run")

            causeAction = new hudson.model.CauseAction(new TimerTrigger.TimerTriggerCause())

            instance.getQueue().schedule(job, 10, causeAction)
    }
}

def dirMap(File source) {
    def map = [:]
    if (!source.exists()) {
        return map
    }
    source.traverse(type: DIRECTORIES) {
        dir ->
            null
            dir.traverse(type: FILES, nameFilter: ~/.*\.xml$/) {
                map.put(dir.name, it)
            }
    }
    return map
}
