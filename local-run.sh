docker build -t community-microservice .
minikube image load community-microservice:latest
kubectl delete secret community-service-secret
kubectl create secret generic community-service-secret --from-env-file=local.env
kubectl delete deployment community-service-deployment
kubectl apply -f local-deployment.yaml