# basic

https://github.com/jenkinsci/plugin-installation-manager-tool

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
          snyk --version
        """
      }
    }
  }
}
```

## Create git-client-plugin job

```
pipeline {
  agent {label "linux"}
  environment {
    SNYK_TOKEN = credentials("snyk-token")
  }
  stages {
    stage("checkout") {
      steps {
        git 'https://github.com/darinpope/git-client-plugin.git'
      }
    }
    stage("Snyk scan") {
      steps {
        sh '''
          snyk auth $SNYK_TOKEN
          snyk test
        '''
      }
    }
    stage("Maven ") {
      steps {
        sh """
          mvn clean verify
        """
      }
    }
  }
}
```

## Cleanup

* `vagrant halt; vagrant destroy -f`