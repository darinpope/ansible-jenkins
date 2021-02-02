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
* `yum -y install wget`
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
* https://adoptopenjdk.net/installation.html#linux-pkg

```
cat <<'EOF' > /etc/yum.repos.d/adoptopenjdk.repo
[AdoptOpenJDK]
name=AdoptOpenJDK
baseurl=http://adoptopenjdk.jfrog.io/adoptopenjdk/rpm/centos/$releasever/$basearch
enabled=1
gpgcheck=1
gpgkey=https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public
EOF
```

* repo to get latest version Git
  * `yum -y install https://packages.endpoint.com/rhel/7/os/x86_64/endpoint-repo-1.7-1.x86_64.rpm`

* Jenkins repo
  * `wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo`
  * `rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key`

* create directories
  * `mkdir -p /var/cache/jenkins/tmp`
  * `mkdir -p /var/cache/jenkins/heapdumps`

* `yum remove git*`
* `yum -y install adoptopenjdk-11-hotspot git jenkins fontconfig`
* edit `/etc/sysconfig/jenkins`
  * `JENKINS_JAVA_OPTIONS="-Djava.awt.headless=true -Djava.io.tmpdir=/var/cache/jenkins/tmp/"`
  * `JENKINS_ARGS="--pluginroot=/var/cache/jenkins/plugins"`
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
* https://adoptopenjdk.net/installation.html#linux-pkg

```
cat <<'EOF' > /etc/yum.repos.d/adoptopenjdk.repo
[AdoptOpenJDK]
name=AdoptOpenJDK
baseurl=http://adoptopenjdk.jfrog.io/adoptopenjdk/rpm/centos/$releasever/$basearch
enabled=1
gpgcheck=1
gpgkey=https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public
EOF
```

* repo to get latest version Git
  * `yum -y install https://packages.endpoint.com/rhel/7/os/x86_64/endpoint-repo-1.7-1.x86_64.rpm`

* `yum remove git*`
* `yum -y install adoptopenjdk-11-hotspot git fontconfig wget`
* install Docker and unzip (https://docs.docker.com/engine/install/centos/)
  * `yum remove docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-engine`
  * `yum -y install yum-utils`
  * `yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo`
  * `yum -y install docker-ce docker-ce-cli containerd.io unzip`
  * `groupadd docker`
  * `systemctl enable docker`
  * `systemctl start docker`
  * `exit`
  * `sudo usermod -aG docker $USER`
  * `exit`
  * `vagrant ssh agent1`
  * `docker run hello-world`
  * `sudo su -`
* install Maven
  * `mkdir -p /opt/tools/maven`
  * `cd /opt/tools/maven`
  * `wget https://mirrors.sonic.net/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz`
  * `tar zxvf apache-maven-3.6.3-bin.tar.gz`
  * `rm -f apache-maven-3.6.3-bin.tar.gz`
  * `ln -s apache-maven-3.6.3 latest`
* install Gradle
  * `mkdir -p /opt/tools/gradle`
  * `cd /opt/tools/gradle`
  * `wget https://services.gradle.org/distributions/gradle-6.8.1-bin.zip`
  * `unzip gradle-6.8.1-bin.zip`
  * `rm -f gradle-6.8.1-bin.zip`
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