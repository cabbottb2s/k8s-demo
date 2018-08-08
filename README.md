### Setup
```
kubectl create namespace demo
kubectl config set-context $(kubectl config current-context) --namespace=demo
docker login
```

### Deploy config-service 1.0 to production
```
git checkout 1.0.x

# Build and publish image
cd config-service
./gradlew clean build dockerPush

# Create Service
cd ..
kubectl apply -f k8s/config-service/lb.yml
kubectl get service -w

# Create Deployment
kubectl apply -f k8s/config-service/app.yml --record
kubectl get pod -w
kubectl logs -l app=config-service
kubectl rollout status deployment/config-service-production

# [Validation] Set up local port forwarding to test service endpoint
kubectl port-forward deployment/config-service-production 30080:8080 &
curl localhost:30080/motd

# [Validation] Confirm service is locatable via DNS
kubectl exec -it $(kubectl get pod -l app=config-service -o jsonpath="{.items[0].metadata.name}") -- wget -q -O - http://config-service-lb/motd
```


### Deploy gateway-service 1.0 to production
```
# Build and publish images
cd gateway-service
./gradlew clean build dockerPush

# Create Service
cd ..
kubectl apply -f k8s/gateway-service/lb.yml
kubectl get service -w

# Create Deployment
kubectl apply -f k8s/gateway-service/app.yml --record
kubectl get pod -w
kubectl logs -l app=gateway-service

# [Validation] Access service via public IP
curl http://$(kubectl get service gateway-service-lb -o jsonpath="{.status.loadBalancer.ingress[0].ip}")/toast

# [Validation] Set up local port forwarding to test service endpoint
kubectl port-forward deployment/gateway-service-production 40080:8080 &
curl localhost:40080/toast

# [Optional] Scale deployment up to 3 replicas
kubectl scale deployment gateway-service-production --replicas=3
```

### Upgrade gateway-service to 1.1.x
```
git checkout 1.1.x

# Build and publish images
cd gateway-service
./gradlew clean build dockerPush

# Update Deployment
cd ..
kubectl apply -f k8s/gateway-service/app.yml --record
kubectl rollout status deployment gateway-service-production

[Optional: pause/resume]
kubectl rollout pause deployment gateway-service-production
kubectl rollout resume deployment gateway-service-production

# [Validation] Access service via public IP
while true
do
  curl http://$(kubectl get service gateway-service-lb -o jsonpath="{.status.loadBalancer.ingress[0].ip}")/toast
  echo ""
  sleep 1
done
```

### Deploy session-service 1.1.x
```
git checkout 1.1.x

# Build and publish images
cd session-service
./gradlew clean build dockerPush

# Create Service
cd ..
kubectl apply -f k8s/session-service/lb.yml
kubectl get service -w

# Create Deployment
kubectl apply -f k8s/session-service/app.yml --record
kubectl get pod -w
kubectl logs -l app=session-service

# [Validation] Confirm service is locatable via DNS
while true
do
  kubectl exec -it $(kubectl get pod -l app=gateway-service -o jsonpath="{.items[0].metadata.name}") -- wget -q -O - http://session-service-lb/sessions/1
  echo ""
  sleep 1
done

# [Validation] Set up local port forwarding to test service endpoint
kubectl port-forward deployment/session-service-production 50080:8080 &
curl localhost:50080/sessions/1
```

### Deploy consul-server 0.9.1
```
# Create headless service
kubectl apply -f k8s/consul/service.yml

# Create stateful set
kubeclt apply -f k8s/consul/statefulset.yml

# [Validation] Check membership
for i in {0..2}; do kubectl exec consul-$i --namespace=consul -- sh -c 'consul members'; done

# [Validation] Check members are reachable via Service DNS
kubectl exec -it $(kubectl get pod -l app=gateway-service -o jsonpath="{.items[0].metadata.name}") -- wget -q -O - http://consul.demo.svc.cluster.local:8500/v1/agent/members

# [Validation] Set up local port forwarding to test service endpoint
kubectl port-forward statefulset/consul 38500:8500 &
browse to http://localhost:38500/ui
```
