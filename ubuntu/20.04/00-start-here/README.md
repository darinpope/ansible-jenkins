# start-here

## Prerequisites

* vagrant
* add the following entries to your local `/etc/hosts`:

```
192.168.32.13 jenkins
192.168.32.15 agent1
```

## Startup

* `vagrant up`

## Install Jenkins

* `vagrant ssh jenkins`
* `sudo su -`
* create `/etc/security/limits.d/30-jenkins.conf`
  * https://support.cloudbees.com/hc/en-us/articles/222446987-Prepare-Jenkins-for-Support

```
jenkins soft core unlimited
jenkins hard core unlimited
jenkins soft fsize unlimited
jenkins hard fsize unlimited
jenkins soft nofile 4096
jenkins hard nofile 8192
jenkins soft nproc 30654
jenkins hard nproc 30654
```

*  add the following entries to `/etc/hosts`:

```
192.168.32.13 jenkins
192.168.32.15 agent1
```

* install AdoptOpenJDK 11
  * `apt-get -y install wget apt-transport-https gnupg`
  * `wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public | apt-key add -`
  * `echo "deb https://adoptopenjdk.jfrog.io/adoptopenjdk/deb $(lsb_release -cs) main" | tee /etc/apt/sources.list.d/adoptopenjdk.list`
  * `apt-get update`
  * `apt-get -y install adoptopenjdk-11-hotspot fontconfig`
  * more details: https://adoptopenjdk.net/installation.html#linux-pkg

* create directories
  * `mkdir -p /var/cache/jenkins/tmp`
  * `mkdir -p /var/cache/jenkins/heapdumps`

* install Jenkins
  * `wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | apt-key add -`
  * `echo "deb https://pkg.jenkins.io/debian-stable binary/" | tee /etc/apt/sources.list.d/jenkins.list`
  * `apt-get update`
  * `apt-get -y install jenkins`
  * more details: https://pkg.jenkins.io/debian-stable/

* `systemctl stop jenkins`
* `rm -rf /var/lib/jenkins/.* 2> /dev/null`
* `rm -rf /var/cache/jenkins/heapdumps/.* 2> /dev/null`
* `rm -rf /var/cache/jenkins/tmp/.* 2> /dev/null`
* `rm -rf /var/cache/jenkins/war`
* `rm -f /var/log/jenkins/jenkins.log`
* edit `/etc/default/jenkins`
  * `JAVA_ARGS="-Djava.awt.headless=true -Djava.io.tmpdir=/var/cache/$NAME/tmp/ -Dorg.apache.commons.jelly.tags.fmt.timeZone=America/New_York -Duser.timezone=America/New_York"`
  * `JENKINS_ARGS="--webroot=/var/cache/$NAME/war --httpPort=$HTTP_PORT --pluginroot=/var/cache/$NAME/plugins"`
* `chown -R jenkins:jenkins /var/cache/jenkins`
* `systemctl start jenkins`
* `tail -f /var/log/jenkins/jenkins.log`

## Setup Jenkins UI

* vagrant ssh jenkins
  * `sudo cat /var/lib/jenkins/secrets/initialAdminPassword`
* Install suggested plugins
* Create First Admin User
* Jenkins URL: http://jenkins:8080/
* Click Start using Jenkins
* http://jenkins:8080/restart


## Install Agent

* `vagrant ssh agent1`
* `sudo su -`
*  add the following entries to `/etc/hosts`:

```
192.168.32.13 jenkins
192.168.32.15 agent1
```
* install AdoptOpenJDK 11
  * `apt-get -y install wget apt-transport-https gnupg`
  * `wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public | apt-key add -`
  * `echo "deb https://adoptopenjdk.jfrog.io/adoptopenjdk/deb $(lsb_release -cs) main" | tee /etc/apt/sources.list.d/adoptopenjdk.list`
  * `apt-get update`
  * `apt-get -y install adoptopenjdk-11-hotspot fontconfig`
  * more details: https://adoptopenjdk.net/installation.html#linux-pkg

* install Docker and unzip (https://docs.docker.com/engine/install/ubuntu/)
  * `apt-get remove docker docker-engine docker.io containerd runc`
  * `apt-get update`
  * `apt-get -y install apt-transport-https ca-certificates curl gnupg-agent software-properties-common`
  * `curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -`
  * `echo "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker-ce.list`
  * `apt-get update`
  * `apt-get -y install docker-ce docker-ce-cli containerd.io`
  * `usermod -a -G docker vagrant`
  * `exit`
  * `exit`
  * `vagrant ssh agent1`
  * `docker run hello-world`
  * `sudo su -`
* install Maven
  * `mkdir -p /opt/tools/maven`
  * `cd /opt/tools/maven`
  * `wget https://mirrors.sonic.net/apache/maven/maven-3/3.8.2/binaries/apache-maven-3.8.3-bin.tar.gz`
  * `tar zxvf apache-maven-3.8.3-bin.tar.gz`
  * `rm -f apache-maven-3.8.3-bin.tar.gz`
  * `ln -s apache-maven-3.8.3 latest`
* install Gradle
  * `mkdir -p /opt/tools/gradle`
  * `cd /opt/tools/gradle`
  * `wget https://services.gradle.org/distributions/gradle-7.0-bin.zip`
  * `unzip gradle-7.0-bin.zip`
  * `rm -f gradle-7.0-bin.zip`
  * `ln -s gradle-6.8 latest`
* `echo "PATH=/opt/tools/gradle/latest/bin:\$PATH" > /etc/profile.d/gradle.sh`
* `echo "PATH=/opt/tools/maven/latest/bin:\$PATH" > /etc/profile.d/maven.sh`
* `chown -R vagrant:vagrant /opt/tools`

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