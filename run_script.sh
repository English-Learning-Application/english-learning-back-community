aws ecr get-login-password --region ap-southeast-2 | docker login --username AWS --password-stdin 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com
docker build -t community-microservice .
docker tag community-microservice:latest 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com/community-microservice:latest
docker push 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com/community-microservice:latest
kubectl delete deployment community-service-deployment
kubectl apply -f deployment.yaml