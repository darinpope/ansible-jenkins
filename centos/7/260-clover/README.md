# basic

-Dhudson.model.DirectoryBrowserSupport.CSP=\"sandbox; default-src 'self'; img-src 'self'; style-src 'self';\"

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

* change master to 0
* connect agent

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