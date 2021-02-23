import jenkins.model.Jenkins
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.CredentialsScope

File disableScript = new File(Jenkins.get().getRootDir(), ".disable-create-vagrant-userpass-credential")
if (disableScript.exists()) {
    return
}

instance = Jenkins.get()
domain = Domain.global()
store = instance.getExtensionList("com.cloudbees.plugins.credentials.SystemCredentialsProvider")[0].getStore()

usernameAndPassword = new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  "vagrant",
  "vagrant",
  "vagrant",
  "vagrant",
)

store.addCredentials(domain, usernameAndPassword)
instance.save()
disableScript.createNewFile()