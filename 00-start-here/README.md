# start-here

## Prerequisites

* add the following entries to `/etc/hosts`:

```
192.168.32.13 jenkins
192.168.32.15 agent1
```

## Startup

* `vagrant up`

## Install Jenkins

* `vagrant ssh jenkins`
* `sudo su -`
* `yum install wget`
*  add the following entries to `/etc/hosts`:

```
192.168.32.13 jenkins
192.168.32.15 agent1
```
* https://adoptopenjdk.net/installation.html#linux-pkg
* https://packages.endpoint.com/
* yum remove git
* yum install adoptopenjdk-11-hotspot fontconfig git

## Cleanup

* `vagrant halt; vagrant destroy -f`