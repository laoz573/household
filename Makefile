# Makefile

IMAGE_NAME = ghcr.io/laoz573/household
TAG ?= latest
GH_USER = laoz573

.PHONY: war docker login push all

# Step 1: WARファイルのビルド
war:
	mvn clean package

# Step 2: Dockerイメージのビルド
docker: war
	docker build -t $(IMAGE_NAME):$(TAG) .

# Step 3: GitHub Packagesにログイン
login:
	echo $$GH_TOKEN | docker login ghcr.io -u $(GH_USER) --password-stdin

# Step 4: Dockerイメージをpush
push: docker login
	docker push $(IMAGE_NAME):$(TAG)

# Step 5: 一発実行
all: push