#!/bin/bash

cd k8s
sleep 20

minikube delete
sleep 20

sudo minikube start \
  --driver=none \
  --container-runtime=docker \
  --cri-socket=/var/run/cri-dockerd.sock
sleep 20

# 念のため再度 start（不要なら削除可）
minikube start \
  --driver=none \
  --container-runtime=docker \
  --cri-socket=/var/run/cri-dockerd.sock
sleep 20

kubectl apply -f pvc.yaml
sleep 20

kubectl apply -f mysql.yaml
sleep 20

kubectl apply -f Household.yaml
sleep 20

kubectl apply -f nginx.yaml
