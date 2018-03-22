import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig

config.each {
    if (!(it.value instanceof String)) {
        createDirectories(it.value.path)
        File file = new File(it.value.path)
        file.createNewFile()
        writeFile(it.value, file, config)
        execute("chmod ${it.value.mode} ${it.value.path}")
    }
}

def writeFile(value, file, config) {
    switch (value.source) {
        case 'HashicorpVault':
            def contents = fetchContentsFromVault(value, config)
            file.text = contents.trim()
            break
        default:
            file.text = value.contents.trim()
            break
    }
}

def fetchContentsFromVault(value, config) {
    String contentKey = getContentKey(value)
    String vaultUrl = getVaultUrl(value, config)
    String vaultToken = getVaultToken(value, config)
    VaultConfig vaultConfig = new VaultConfig()
            .address(vaultUrl)
            .token(vaultToken)
            .build()
    Vault vault = new Vault(vaultConfig)

    String contents = vault.logical().read(value.key).getData().get(contentKey)
    if (value.base64Encoded) {
        contents = new String(contents.decodeBase64())
    }
    if (!contents) {
        println("Warning: '${contentKey}' from vault ${value.key}@${vaultUrl} is null")
        return ""
    }
    return contents
}

def getVaultUrl(value, config) {
    value.url ?: config.vaultUrl
}

def getVaultToken(value, config) {
    value.token ?: config.vaultToken
}

def getContentKey(value) {
    return value.contentKey ?: "contents"
}

private createDirectories(path) {
    if (path.indexOf("/") != -1) {
        def directoryPath = path.substring(0, path.lastIndexOf('/'))
        File directory = new File(directoryPath)
        directory.mkdirs()
    }
}

private execute(String command) {
    return execute(command, new File(System.properties.'user.dir'))
}

private execute(String command, File workingDir) {
    println("executing command: ${command}")
    def process = new ProcessBuilder(addShellPrefix(command))
            .directory(workingDir)
            .redirectErrorStream(true)
            .start()
    process.inputStream.eachLine { println it }
    process.waitFor();
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
