import org.jenkinsci.plugins.scriptsecurity.scripts.*

ScriptApproval scriptApproval = ScriptApproval.get()
scriptApproval.clearApprovedSignatures()

config.approvedSignatures.each { signature ->
    scriptApproval.approveSignature(signature)
}

config.approvedScriptHashes.each { hash ->
    scriptApproval.approveScript(hash)
}

scriptApproval.save()
