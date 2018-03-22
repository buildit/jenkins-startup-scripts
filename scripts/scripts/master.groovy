config.labels.each { label ->
    println "Adding label ${label} to master."
    instance.setLabelString("${instance.getLabelString()} ${label}")
}

instance.save()
