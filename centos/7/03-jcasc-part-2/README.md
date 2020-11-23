# basic

## Prerequisites

* `ansible-galaxy collection install -r requirements.yml`

## Startup

* `vagrant up`
* `ansible-playbook -i inventory/virtualbox.hosts playbooks/virtualbox.yml`

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