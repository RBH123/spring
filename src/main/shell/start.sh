#!/bin/bash -il
name=rocky-docker
docker ps -a | grep $name &>/dev/null
if [ $? -eq 0 ]; then
  echo $name" is up,we will stop and remove it!!!"
  docker stop $name
  docker rm $name
else
  echo $name" is not up!!!"
fi

docker images | grep $name &>/dev/null
if [ $? -eq 0 ]; then
  echo $name" image is existed,we will remove it!!!"
  docker rmi $(docker images | grep $name | awk "{print $3}")
else
  echo $name" image is not existed!!!"
fi
mvn clean package -Dmaven.skip.test=true
docker run -d -p 8083:8082 --name rocky-docker rocky-docker:latest
