import org.jenkinsci.plugins.aquadockerscannerbuildstep.AquaDockerScannerBuilder
import jenkins.model.Jenkins
import net.sf.json.JSONObject

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// get AquaSec plugin
def aquasec = jenkins.getExtensionList(AquaDockerScannerBuilder.DescriptorImpl.class)[0]

// define form and request
JSONObject formData = ['aquaScannerImage': config.aquaScannerImage, 'apiURL': config.apiUrl, 'user': config.user,
                       'password': config.password, 'version': config.version, 'timeout': config.timeout,
                       'runOptions': config.runOptions, 'caCertificates': config.caCertificates] as JSONObject

// add AquaSec configuration to Jenkins
aquasec.configure(null, formData)

// save to disk
aquasec.save()
jenkins.save()