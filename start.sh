#!/bin/bash
echo 'be-portal-app start '
git pull origin release
./mvnw clean package -Dmaven.test.skip=true
export APP_ENCRYPTION_PASSWORD=test
docker build -t be-portal-app .
docker stop be-portal-app
docker rm be-portal-app
docker run -it -d -p 8081:8081  \
    -e spring.profiles.active="dev"  \
    -e APP_ENCRYPTION_PASSWORD="test"  \
    --add-host=database:141.164.41.213 \
    --name be-portal-app \
    be-portal-app
