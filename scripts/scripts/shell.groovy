if (config) {
    def descriptor = instance.getDescriptor("hudson.tasks.Shell")
    descriptor.setShell(config?.path)
    descriptor.save()
}
