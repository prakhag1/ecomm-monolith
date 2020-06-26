#!/bin/bash
set -eo pipefail

export PROJECT=$(gcloud config get-value project)
export REGION="us-central1"
export ZONE="us-central1-a"
export CLUSTER="test-cluster-monolith"
export DB="ecommerce"
export DB_INSTANCE="test-instance-monolith"
export NETWORK="test-network-monolith"

bold=$(tput bold)
normal=$(tput sgr0)

spinner() {
    local i sp n
    sp='/-\|'
    n=${#sp}
    while sleep 0.1; do
        printf "%s\b" "${sp:i++%n:1}"
    done
}

# Create network
createNetwork() {
 echo ${bold}"Creating network..."${normal}
 spinner &

 gcloud compute networks create $NETWORK \
  --verbosity error --no-user-output-enabled

 gcloud compute networks subnets update $NETWORK \
  --region=$REGION \
  --enable-private-ip-google-access \
  --verbosity error --no-user-output-enabled

 echo ${bold}"Setting up VPC peering for private CloudSQL connection..."${normal}
 gcloud compute addresses create google-managed-services-$NETWORK \
  --global \
  --purpose=VPC_PEERING \
  --prefix-length=20 \
  --description="peering range for Google" \
  --network=$NETWORK --project=$PROJECT \
  --verbosity error --no-user-output-enabled
 
 gcloud services vpc-peerings connect \
  --service=servicenetworking.googleapis.com \
  --ranges=google-managed-services-$NETWORK \
  --network=$NETWORK --project=$PROJECT \
  --verbosity error --no-user-output-enabled

 kill "$!" # kill the spinner
}

# Create GKE cluster
createCluster() {
 echo ${bold}"Creating cluster..."${normal}
 spinner &

 gcloud container clusters create $CLUSTER \
   --zone $ZONE --enable-ip-alias \
   --network $NETWORK \
   --verbosity error --no-user-output-enabled
 
 kill "$!" # kill the spinner
}

# Create CloudSQL database
createDB() {
 echo ${bold}"Creating database..."${normal}
 spinner &

 gcloud beta sql instances create $DB_INSTANCE \
   --tier=db-n1-standard-1 --region=$REGION \
   --network=$NETWORK \
   --no-assign-ip \
   --verbosity error --no-user-output-enabled

 gcloud sql users set-password root \
   --host=% --instance $DB_INSTANCE --password password \
   --verbosity error --no-user-output-enabled
 
 gcloud sql databases create $DB \
   --instance=$DB_INSTANCE \
   --verbosity error --no-user-output-enabled
 
 kill "$!" # kill the spinner

 DB_IP=$(gcloud sql instances describe $DB_INSTANCE | grep "ipAddress:" | awk -F ":" '{print $NF}')
 echo ${bold}"Database provisioned at $DB_IP"${normal}
}

# Create GKE secrets
createSecrets(){
 echo ${bold}"Generating GKE secrets for database access..."${normal}
 kubectl create secret generic dbsecret \
   --from-literal=username=root \
   --from-literal=password=password
}

# Build application container image
buildImage() {
 echo ${bold}"Building container image for the application..."${normal}
 gcloud builds submit --tag gcr.io/$PROJECT/hipster
}

# Deploy application
deploy() {
 echo ${bold}"Deploying application..."${normal}
 sed -i -e "s/\[PROJECT_ID\]/$PROJECT/g" deploy.yaml
 sed -i -e "s/\[DB_IP\]/$DB_IP/g" deploy.yaml
 kubectl apply -f deploy.yaml 
}

# Check and report status
checkStatus() {
 echo ${bold}"Checking for successful deployments..."${normal}
 spinner &

 kubectl rollout status deployment/hipster

 echo ${bold}"Checking for load balancer to be provisioned..."${normal}
 sleep 30
 ip=$(kubectl get svc hipster \
    -o jsonpath="{.status.loadBalancer.ingress[*].ip}")

 kill "$!" # kill the spinner

 echo ${bold}"Application deployed at http://"${ip}${normal}
 echo "Note: It may take a few minutes for GKE to set up forwarding rules until the load balancer is ready to serve your application."
}

createNetwork
createCluster
createDB
buildImage
createSecrets
deploy
checkStatus
