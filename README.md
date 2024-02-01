Here is the api Documentation.

It showcases the endpoints, how requests and reponses should look like.
[OpenApi/Swagger Api Documentation](https://morenos96.github.io/StarSystemTravelTimeService/)

To run this App you need to do these steps:
Preparation:
  
  install docker on your machine: [docker documentation](https://docs.docker.com/) 
  please follow the guide to install docker.
  
  install git-cli: [download here](https://cli.github.com/)

(make sure you can use docker cli and git cli)

execute inside a terminal:

docker -v

git -v

the terminal should display your installed versions

execute the following commands:

cd path/to/your/preferred/directory # please adjust

git clone https://github.com/morenos96/StarSystemTravelTimeService.git

cd StarSystemTravelTimeService

./mvnw clean install -DskipTests # remove -DskipTests if you want to execute the tests

docker-compose up -d


the application is now running at http://localhost:8080/

and you can call the api for example with [postman](https://www.postman.com/downloads/)

