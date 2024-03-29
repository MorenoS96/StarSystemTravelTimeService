Here is the api Documentation. It showcases the endpoints, how requests and reponses should look like.

[OpenApi/Swagger Api Documentation](https://morenos96.github.io/StarSystemTravelTimeService/)

To run this App you need to do these steps:

**Preparation**:
  
  install docker on your machine: [docker documentation](https://docs.docker.com/) 
  please follow the guide to install docker.
  
  install git-cli: [download here](https://cli.github.com/)

  install java jdk17 or higher
  
(make sure you can use docker cli and git cli, and that docker is running)
---
execute inside a terminal:

docker -v

git -v

the terminal should display your installed versions

execute the following commands:
---
 please adjust.  on windows you might need to use cd /d path if you want to save the code on another harddrive.

cd path/to/your/preferred/directory


git clone https://github.com/morenos96/StarSystemTravelTimeService.git

cd StarSystemTravelTimeService

(remove -DskipTests if you want to execute the tests - may take awhile)

mvnw clean install -DskipTests 

**make sure no other service block the necessary ports: 8080,7474,7687** 

docker-compose build

docker-compose up 

---

wait a couple of seconds


**the application is now running at http://localhost:8080/**

Again the api endpoints are specified here

[OpenApi/Swagger Api Documentation](https://morenos96.github.io/StarSystemTravelTimeService/)

and you can call the api for example with [postman](https://www.postman.com/downloads/)

