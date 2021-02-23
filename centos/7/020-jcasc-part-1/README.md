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
## Create prometheus-plugin job using Snyk CLI

```
pipeline {
  agent {label "linux"}
  environment {
    SNYK_TOKEN = credentials("snyk-api-token")
  }
  stages {
    stage("checkout") {
      steps {
        git 'https://github.com/darinpope/prometheus-plugin.git'
      }
    }
    stage("Snyk scan") {
      steps {
        sh '''
          snyk auth $SNYK_TOKEN
          snyk test || exit 0
        '''
      }
    }
    stage("Maven ") {
      steps {
        sh """
          mvn clean install
          mvn hpi:hpi
        """
      }
    }
  }
}
```

## Create prometheus-plugin job using Snyk plugin

```
pipeline {
  agent {label "linux"}
  stages {
    stage("checkout") {
      steps {
        git 'https://github.com/darinpope/prometheus-plugin.git'
      }
    }
    stage("Snyk scan") {
      steps {
        snykSecurity failOnIssues: false, snykInstallation: 'snyk@latest', snykTokenId: 'snyk-api-token'
      }
    }
    stage("Maven ") {
      steps {
        sh """
          mvn clean install
          mvn hpi:hpi
        """
      }
    }
  }
}
```

## Cleanup

* `vagrant halt; vagrant destroy -f`