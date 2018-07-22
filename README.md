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
kubectl apply -f k8s/config-service/app.yml
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
kubectl apply -f k8s/gateway-service/app.yml
kubectl get pod -w
kubectl logs -l app=gateway-service

# [Validation] Access service via public IP
curl http://$(kubectl get service gateway-service-lb -o jsonpath="{.status.loadBalancer.ingress[0].ip}")/toast

# [Optional] Scale deployment up to 3 replicas
kubectl scale deployment gateway-service-production --replicas=3

```
