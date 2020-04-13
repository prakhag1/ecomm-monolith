#!/bin/sh

export PROJECT=$(gcloud config get-value project)
export REGION="us-central1"
export ZONE="us-central1-a"
export CLUSTER="test-cluster"
export DB="ecommerce"
export DB_INSTANCE="test-instance"
export SERVICE_ACCOUNT="test-sa"

# Delete cluster
echo "Deleting cluster..."
gcloud container clusters delete $CLUSTER --zone $ZONE --async

# Delete DB
echo "Deleting database..."
gcloud sql databases delete $DB --instance=$DB_INSTANCE
gcloud sql instances delete $DB_INSTANCE


# Delete service account and credentials
echo "Deleting service account and locally downloaded credentails file..."
SA_EMAIL=${SERVICE_ACCOUNT}@${PROJECT}.iam.gserviceaccount.com
gcloud iam service-accounts delete $SA_EMAIL
\rm -rf credentials.json

# Delete container image
echo "Deleting container image from the registry..."
gcloud container images list-tags \
gcr.io/$PROJECT/connect \
    	--format 'value(digest)' | \
    	xargs -I {} gcloud container images delete \
    	--force-delete-tags --quiet \
    	gcr.io/${PROJECT}/hipster@sha256:{}
