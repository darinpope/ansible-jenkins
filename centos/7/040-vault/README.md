# basic

## Prerequisites

* `ansible-galaxy collection install -r requirements.yml`

## Startup

* `vagrant up`
* `ansible-playbook -i inventory/virtualbox.hosts playbooks/virtualbox.yml`

## Connect Vault to Jenkins

### Setup Vault Server

* `vagrant ssh vault1`
* `vault status -address=http://127.0.0.1:8200`
  * you should see something like
```
Key                Value
---                -----
Seal Type          shamir
Initialized        false
Sealed             true
Total Shares       0
Threshold          0
Unseal Progress    0/0
Unseal Nonce       n/a
Version            1.6.0
Storage Type       raft
HA Enabled         true
```  
* `vault operator init -address=http://127.0.0.1:8200`
  * capture all the output and save it somewhere. There should be 5 unseal keys and 1 initial root token.
* `vault operator unseal -address=http://127.0.0.1:8200`
  * enter one of the unseal keys
```
Key                Value
---                -----
Seal Type          shamir
Initialized        true
Sealed             true
Total Shares       5
Threshold          3
Unseal Progress    1/3
Unseal Nonce       70050c30-f6e5-cbcc-c7b0-ad7f4c8cb536
Version            1.6.0
Storage Type       raft
HA Enabled         true
```  
* `vault operator unseal -address=http://127.0.0.1:8200`
  * enter a different unseal key
```
Key                Value
---                -----
Seal Type          shamir
Initialized        true
Sealed             true
Total Shares       5
Threshold          3
Unseal Progress    2/3
Unseal Nonce       70050c30-f6e5-cbcc-c7b0-ad7f4c8cb536
Version            1.6.0
Storage Type       raft
HA Enabled         true
```  
* `vault operator unseal -address=http://127.0.0.1:8200`
  * enter a different unseal key
```
Key                     Value
---                     -----
Seal Type               shamir
Initialized             true
Sealed                  false
Total Shares            5
Threshold               3
Version                 1.6.0
Storage Type            raft
Cluster Name            vault-cluster-21870e73
Cluster ID              5644c074-472a-d509-2de9-93a88e7f5661
HA Enabled              true
HA Cluster              n/a
HA Mode                 standby
Active Node Address     <none>
Raft Committed Index    24
Raft Applied Index      24
```  
* `vault login -address=http://127.0.0.1:8200 <initial root token>`
  * i.e. `vault login -address=http://127.0.0.1:8200 s.jXJm6TQsGZagjcBIYg3qHuaw`
```
Key                  Value
---                  -----
token                s.jXJm6TQsGZagjcBIYg3qHuaw
token_accessor       HZcSD5W7I5b5Y24uBywX4JZe
token_duration       ∞
token_renewable      false
token_policies       ["root"]
identity_policies    []
policies             ["root"]
```  
* `vault status -address=http://127.0.0.1:8200`
  * you should see
```
Key                     Value
---                     -----
Seal Type               shamir
Initialized             true
Sealed                  false
Total Shares            5
Threshold               3
Version                 1.6.0
Storage Type            raft
Cluster Name            vault-cluster-21870e73
Cluster ID              5644c074-472a-d509-2de9-93a88e7f5661
HA Enabled              true
HA Cluster              https://127.0.0.1:8201
HA Mode                 active
Raft Committed Index    32
Raft Applied Index      32
```  
* `exit`

### setup the Vault plugin

* create a Vault Token Credential and enter the Initial Root Token
  * `Manage Jenkins` -> `Manage Credentials`
  * click on Global
  * click on `Add Credentials` on the left nav
  * select Kind of `Vault Token Credential`
  * enter the initial root token in `Token`
  * set the id and description to `vault-root-token`
* connect the Jenkins controller to the Vault server
  * `Manage Jenkins` -> `Configure System`
  * scroll down to Vault Plugin section
  * `Vault URL` = http://vault1:8200
  * `Vault Credential` = `vault-root-token`
  * click `Save`

## Cleanup

* `vagrant halt; vagrant destroy -f`


https://learn.hashicorp.com/tutorials/vault/getting-started-deploy?in=vault/getting-started


Unseal Key 1: S19tW/nc/TBiBREutr/RVdi8uDV42PDgUDPuwVPQdxYB
Unseal Key 2: kJbjkSGzh2avvptZe11amle1xi7KTt7lv60plMA28ibK
Unseal Key 3: N1pw9qV6q/1CMSCmELLf7Z3G+4LknDFMzxVkzbJWnEIH
Unseal Key 4: 8GsdEN9MvRbk9kWDNrqc+NyXyFyphsrP9tFiEbSi61Ni
Unseal Key 5: lFTcONbscOW74r3dfb3BoM1HRE9b09o/3fgl7khtozZJ

Initial Root Token: s.NrNPS5afGMHvvdUe6jHjOxFU


vault status -address=http://127.0.0.1:8200
vault operator init -address=http://127.0.0.1:8080
vault operator unseal -address=http://127.0.0.1:8200
vault operator unseal -address=http://127.0.0.1:8200
vault operator unseal -address=http://127.0.0.1:8200
vault login -address=http://127.0.0.1:8200 s.jXJm6TQsGZagjcBIYg3qHuaw
