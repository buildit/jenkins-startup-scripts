config.each {
    println("executing command: ${it.key}")
    execute("${it.value.command}")
}

private execute(String command) {
    return execute(command, new File(System.properties.'user.dir'))
}

private execute(String command, File workingDir) {
    def process = new ProcessBuilder(addShellPrefix(command))
            .directory(workingDir)
            .redirectErrorStream(true)
            .start()
    process.inputStream.eachLine { println it }
    process.waitFor()
    println("exit value: ${process.exitValue()}")
    if (process.exitValue() > 0) {
        throw new RuntimeException("Process exited with non-zero value: ${process.exitValue()}")
    }
}

private addShellPrefix(String command) {
    commandArray = new String[3]
    commandArray[0] = "sh"
    commandArray[1] = "-c"
    commandArray[2] = command
    return commandArray
}
