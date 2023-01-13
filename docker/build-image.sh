#! /bin/bash

war_file_name=library.war
docker_directory=docker

# Building a .war file
cd ../
mvn clean package

# Copying the file to docker's directory
cp target/${war_file_name} ${docker_directory}

# Returning to the docker's directory or exit if fails
cd ${docker_directory} || exit

# Building docker image
sudo docker build --build-arg war_file=${war_file_name} -t library .

rm ${war_file_name}
