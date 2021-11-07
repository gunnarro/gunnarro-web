![build](https://github.com/gunnarro/gunnarro-web/workflows/build/badge.svg)
![test](https://github.com/gunnarro/gunnarro-web/workflows/tests/badge.svg)

# gunnarro-web
[gunnarro:as](https://gunnarro-web.azurewebsites.net) web application is hosted at Azure

## Azure

### Azure DevOps
This web application use the CI/CD pipeline provided by [Azure DevOps](https://dev.azure.com)
#### Azure pipelines

### Deploy to Azure

ref: [deploy-spring-boot-java-app-on-linux](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/deploy-spring-boot-java-app-on-linux)
ref: [deploy-spring-boot-java-app-on-kubernetes](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/deploy-spring-boot-java-app-on-kubernetes)
ref: [spring-boot-for-azure](https://spring.io/guides/gs/spring-boot-for-azure/)
We are using [jib](https://cloud.google.com/java/getting-started/jib) to build docker image 
App service edit docker registry in Azure Admin GUI: App Services | gunnarro-web | Deployment Center

mvn azure-webapp:deploy

az acr login -n gunnarroregistry && mvn compile jib:build -P build-docker

## Code
- [GitHub](https://github.com) Code repository
  - [Docker Registry](https://registry.hub.docker.com)
- [SonarQube](https://sonarcloud.io/dashboard?id=gunnarro_gunnarro-web) Code Review 
- [snyk](https://app.snyk.io) Dependency Monitoring

# CD/CI

# PDF
## Convert html to pdf online
- zip html file: zip gr-cv.zip src/main/resources/templates/cv/gr-cv.html
- upload zip to: https://itextpdf.com/en/demos/convert-html-css-to-pdf-free-online


## Logger
curl -X POST http://localhost:8081/actuator/loggers/om.gunnarro.followup -H 'content-type: application/json' -d '{"configuredLevel":"DEBUG"}'
curl -X GET -i http://localhost:8081/actuator/loggers/om.gunnarro.followup