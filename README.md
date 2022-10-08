![build and test](https://github.com/gunnarro/gunnarro-web/actions/workflows/build.yml/badge.svg)
![analyze code](https://github.com/gunnarro/gunnarro-web/actions/workflows/analyze.yml/badge.svg)
![build and deploy image to docker.hub](https://github.com/gunnarro/gunnarro-web/actions/workflows/deploy-docker-hub.yml/badge.svg)
[![Known Vulnerabilities](https://snyk.io/test/github/gunnarro/gunnarro-web/badge.svg)](https://snyk.io/test/github/gunnarro/gunnarro-web)


# gunnarro-web
Web application hosted at Azure, [gunnarro:as](https://gunnarro-web.azurewebsites.net), which is mainly based on open source products

az webapp log download --log-file *.zip  --resource-group gunnarro-resource-group --name gunnarro-web


echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Docker
Run and test application locally from docker by pull the image from docker first
- install docker
- sudo docker login
- sudo docker run -d --network=host --env-file docker.env gunnarro/gunnarro-web:latest
  - d: as background process
  - network: in order to access through localhost:port

#### docker stop
docker container stop

#### docker restart
docker container restart 

#### docker list containers
docker container ls

#### docker status
docker ps -a
