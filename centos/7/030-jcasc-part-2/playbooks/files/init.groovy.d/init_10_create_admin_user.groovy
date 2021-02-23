import jenkins.model.*
import hudson.security.*

File disableScript = new File(Jenkins.get().getRootDir(), ".disable-create-admin-user")
if (disableScript.exists()) {
    return
}

def instance = Jenkins.get()

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount('admin','admin')

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)

instance.setSecurityRealm(hudsonRealm)
instance.setAuthorizationStrategy(strategy)
instance.save()
disableScript.createNewFile()