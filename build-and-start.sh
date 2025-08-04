#!/bin/bash

if [ "$EUID" -eq 0 ]; then
  echo "Doesn't run this script like root."
  echo "When we build project we cannot be root."
  echo "In the docker execution we will need your root powers"
  exit 1
fi

echo "Building projects"

mvn clean install -N
mvn clean install

echo "Projects already builded"
echo "I need to sudo permission to execute docker images"

sudo docker-compose up --build
