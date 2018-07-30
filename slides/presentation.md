## APEX - Kubernetes evaluation

---

## What is Kubernetes

Control plane
* executes commands from kubectl
* scheduler

etcd
* consensus-based HA datastore

pods

nodes
* run pods
---

## (Very) brief history

Mention Borg project
CNCF

---

## Why Kubernetes

* Right level of abstraction
** still have visibility into environment, still have control to customize (vs PaaS: Heroku, Cloud Foundry)
** don't have to manage low-level details (patching VMs, allocating storage/CPU/memory)

* Applications are treated universally
** Docker image containing entire execution environment
** YAML descriptor

* Accelerated deployment
** self-service options for developers
** Infrastructure can change more dynamically
** Same full-stack execution environment as builds are promoted

* Infrastructure as code
** All entities (pods/deployments/service/volumes) are declaratively described
** Version controlled with standard SCM tools
** Configuration can be templatized for repeatability in CD pipeline

* Orchestration
** Scheduler manages where and how many containers to spin up
** Failed containers are replaced
** Managed deployments with pause/resume/rollback

---

## comparison of concepts

Datacenter <--> K8S
===================

VIP <--> Service
Release <--> Deployment
VM <--> Pod
Application <--> Container
Storage <--> PersistentVolume

---

## Goals


---

## APEX Requirements

Service Discovery
Leadership Election
Persistent Storage
Visibility
Security (single-use card)
CI/CD

---

## Operational Requirements

Monitoring and alerting
* Application
* Container
* Node
* Cluster

---

## Not in Scope (yet)

Not necessary to be entirely "cloud native", leverage it where it provides greatest benefit.  That means the APEX (micro)services

3rd party Components
* Database
* AMQ
* Fusion / Solr / Mongo



---

## Deployments

Implicitly creates a replica set
Defines a specific number of pods with specified template  
Failed pods will be automatically replaced


---

## Deployment strategies

Rolling upgrade
Full green / blue (hardware costs)
Canary

TODO: images

---

## Demo using GCE in GCP

Show session-service running locally
Make a small change, increment version
Build/push docker image
Update deployment version, rollout

---

## Next steps

CI/CD pipeline
Monitoring / Alerting (Prometheus?)
Affinity rules
multi-container pods
point-to-point encryption
Certificate authorities
Shared PersistentVolumes (maybe..)
Customized base Docker image (Bridge2 CA's, security hardening, Java agents, etc)
