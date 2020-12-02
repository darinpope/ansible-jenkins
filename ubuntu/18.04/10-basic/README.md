# basic

## Prerequisites

* `ansible-galaxy collection install -r requirements.yml`

## Startup

* `vagrant up`
* `ansible-playbook -i inventory/virtualbox.hosts playbooks/virtualbox.yml`

## Setup Jenkins UI

* vagrant ssh jenkins
  * `sudo cat /var/lib/jenkins/secrets/initialAdminPassword`
* Install suggested plugins
* Create First Admin User
* Jenkins URL: http://jenkins:8080/
* Click Start using Jenkins
* http://jenkins:8080/restart

## Connect agent to Jenkins

* `Manage Jenkins` -> `Configure System`
  * # of executors = `0`
  * Labels = some random string
  * Usage = `Only build jobs with label expressions matching this node`
  * Click `Save`
* `Manage Jenkins` -> `Manage Nodes and Clouds`
  * `New Node`
  * Node name = `agent 1`
    * select "Permanent Agent"
    * Click `OK`
  * Remote root directory = `/home/vagrant/jenkins-agent`
  * Labels = `linux`
  * Usage = `Only build jobs with label expressions matching this node`
  * Launch method = `Launch agents via SSH`
    * Host = `agent1`
    * add and select `vagrant` credential
    * Host Key Verification Strategy = `Non verifying Verification Strategy`
  * Click `Save`

## Create test job

```
pipeline {
  agent {label "linux"}
  stages {
    stage("Hello") {
      steps {
        sh """
          mvn --version
          gradle --version
          docker info
        """
      }
    }
  }
}
```

## Update plugins

* remove
  * `ant`
  * `gradle`
* install
  * `pipeline-utility-steps`
  * `configuration-as-code`

## Cleanup

* `vagrant halt; vagrant destroy -f`