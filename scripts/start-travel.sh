#!/bin/bash
cd ../
cd topology/dev
docker-compose up -d

echo Docker dev topology is now running in background

cd ../../
cd microservices/travel/spring-server
# mvn clean install

mvn -D DB_HOST=$1 -D DB_PORT=3307 -D TOKEN_SECRET=secret -D DB_USERNAME=root -D DB_PASSWORD=admin -D HIBERNATE_MODE=validate spring-boot:run