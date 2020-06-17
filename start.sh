#!/bin/bash

./mvnw clean package
docker build -t -f Dockerfile-dev toyongyeon/portalapp .
docker run -p 8080:8080 toyongyeon/portalapp