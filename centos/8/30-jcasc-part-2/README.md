# basic

## Prerequisites

* `ansible-galaxy collection install -r requirements.yml`

## Startup

* `vagrant up`
* `ansible-playbook -i inventory/virtualbox.hosts playbooks/virtualbox.yml`

## Setup Jenkins

* change password for `vagrant` credential and connect agent
* Configure Global Security
  * Authentication -> Jenkins' own user database
    * uncheck "Allow users to sign up"
  * Authorization -> Logged-in users can do anything
    * uncheck "Allow anonymous read access"
  * Click Save
* create admin user

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


## Cleanup

* `vagrant halt; vagrant destroy -f`