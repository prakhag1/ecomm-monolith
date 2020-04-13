#!/bin/sh

export PROJECT=$(gcloud config get-value project)
export REGION="us-central1"
export ZONE="us-central1-a"
export CLUSTER="test-cluster"
export DB="ecommerce"
export DB_INSTANCE="test-instance"
export SERVICE_ACCOUNT="test-sa"

normal='\033[00m'
bold='\033[1;37m'
green='\033[0;32m'

# Create GKE cluster
createCluster() {
 echo ${bold}${green}"Creating cluster..."${normal}
 gcloud container clusters create $CLUSTER --zone $ZONE
}

# Create CloudSQL database
createDB() {
 echo "\n"
 echo ${bold}${green}"Creating database..."${normal}
 gcloud sql instances create $DB_INSTANCE \
   --tier=db-n1-standard-1 --region=$REGION
 gcloud sql users set-password root \
   --host=% --instance $DB_INSTANCE --password password
 gcloud sql databases create $DB \
   --instance=$DB_INSTANCE
}

# Create GKE secrets
createSecrets(){
 echo "\n"
 echo ${bold}${green}"Generating GKE secrets from service account credentails..."${normal}
 gcloud iam service-accounts create $SERVICE_ACCOUNT \
   --display-name $SERVICE_ACCOUNT
 SA_EMAIL=${SERVICE_ACCOUNT}@${PROJECT}.iam.gserviceaccount.com
 gcloud projects add-iam-policy-binding $PROJECT \
   --member serviceAccount:$SA_EMAIL \
   --role roles/cloudsql.admin
 gcloud iam service-accounts keys create credentials.json \
   --iam-account=$SA_EMAIL
 kubectl create secret generic cloudsql-proxy-claim0 \
   --from-file credentials.json
 kubectl create secret generic dbsecret \
   --from-literal=username=root \
   --from-literal=password=password
}

# Build application container image
buildImage() {
 echo "\n"
 echo ${bold}${green}"Building container image for the application..."${normal}
 gcloud builds submit --tag gcr.io/$PROJECT/hipster
}

# Deploy application
deploy() {
 echo "\n"
 echo ${bold}${green}"Deploying application..."${normal}
 sed -i -e "s/\[PROJECT_ID\]/$PROJECT/g" deploy.yaml
 kubectl apply -f deploy.yaml 
}

# Check and report status
checkStatus() {
 echo "\n"
 echo ${bold}${green}"Checking for successful deployments..."${normal}
 kubectl rollout status deployment/hipster

 echo "\n" 
 echo ${bold}${green}"Checking for load balancer to be provisioned..."${normal}
 sleep 30
 ip=$(kubectl get svc hipster \
    -o jsonpath="{.status.loadBalancer.ingress[*].ip}")
 echo ${bold}"Application deployed at http://"${ip}${normal}
 echo "Note: It may take a few minutes for GKE to set up forwarding rules until the load balancer is ready to serve your application."
}

createCluster
createDB
buildImage
createSecrets
deploy
checkStatus
