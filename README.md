# grouch-trash-holiday-schedule-service
Serverless microservice for CRUD operations trash schedule for holidays and stores them in a DynamoDB table.

## Build
This project is built using maven. The following command can be used to build.
```bash
./mvnw clean verify
```

## Running locally
The application = can be ran locally by running the following command
```bash
./mvnw spring-boot:run
```
You can also run from your IDE by running [`net.mporter.grouch.holiday.Application`](src/main/java/net/mporter/grouch/holiday/Application.java)

### Local DynamoDB
You can run the application connecting to a local DynamoDB by starting the database using docker and using the `local` profile
```bash
docker-compose up -d
./mvnw spring-boot:run -Dspring.profiles.active=local
```

## Tests
Unit Tests are ran as part of the maven build.

### Cucumber Test
This project uses Cucumber to automate testing and to describe behavior. To run the tests locally run the following command.
 
 ```bash
./mvnw -P cucumber verify 
```

To run the same tests against the production lambda function.
First use the `aws configure` command to setup credentials for aws then run.
```bash
./mvnw -P cucumber verify -Dspring.profiles.active=prod
```

You can also run from your IDE by running [`net.mporter.grouch.holiday.cucumber.CucumberRunner`](src/test/java/net/mporter/grouch/holiday/cucumber/CucumberRunner.java)

## Deploy
The code will automatically be built and deployed with a [github action.](.github/workflows/build.yml)

to deploy the application first use the `aws configure` comand to setup credentials for aws, then run
```bash
sam build
sam deploy
```

## Code Scanning

### Snyk OSS Scanning
OSS scanning is done using Snyk as part of the deployment pipeline and results can be viewed on the github action logs.
You can run a scan locally by running this command.
```bash
snyk monitor
```

### Sonar Scanning
Sonar quality scans are done as part of the deployment pipeline and results can be viwed on the github action logs.
Results can also be found on [Sonarcloud.](https://sonarcloud.io/dashboard?id=grouch-trash-holiday-schedule-service)
You can run a scan locally by running this command.

```bash
 ./mvnw -Dsonar.login=${SONAR_TOKEN} verify sonar:sonar 
```
