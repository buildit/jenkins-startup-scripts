config?.each { key, value ->
    println("Setting system property ${key} : ${value}")
    System.setProperty(key, value)
}