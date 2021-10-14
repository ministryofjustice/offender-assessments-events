# Offender Assessments Events

This service raises business events from OASys for offender assessments via an SNS topic

## Continuous Integration

[![CircleCI](https://circleci.com/gh/ministryofjustice/offender-assessments-events.svg?style=svg)](https://circleci.com/gh/ministryofjustice/offender-assessments-events)

### Prerequisites
* Java JDK 11+
* An editor/IDE
* Gradle
* Docker
* OAuth  [(running in a container)](#backing-services)
* LocalStack [(running in a container)](#backing-services)

### Backing services
In order to run the service locally, [Nomis OAuth Service](https://github.com/ministryofjustice/nomis-oauth2-server/) and [LocalStack](https://github.com/localstack/localstack) are required. This can be run locally using the provided [docker-compose.yaml](docker-compose.yaml) file which will pull down the latest version.

From the command line run:
```
 docker-compose up 
```

**If the LocalStack instance comes up but is missing SNS resources**

Run the script `./src/test/resources/localstack/setup-sns.sh` to create the AWS resources in LocalStack

### Build service and run tests

This service is built using Gradle. In order to build the project from the command line and run the tests, you require LocalStack to be running, and use:
```  
AWS_PROVIDER=localstack AWS_ACCESS_KEY=local AWS_SECRET_KEY=dev ./gradlew clean build 
```
The created JAR file will be named "`offender-assessments-events-<yyyy-mm-dd>.jar`", using the date that the build takes place in the format `yyyy-mm-dd`.


### Start the application with H2 database

The configuration can be changed for the api to use an in-memory H2 database by using the spring boot profile `dev`. On the command line run:
```  
SPRING_PROFILES_ACTIVE=dev 
java -jar build/libs/offender-assessments-events-<yyyy-mm-dd>.jar  
```

### Start the application with H2 database and LocalStack configuration

The application can be configured to use LocalStack and an in-memory H2 DB by using the spring boot profile `local`. Ensure you have LocalStack running, on the command line run:
```  
SPRING_PROFILES_ACTIVE=dev AWS_PROVIDER=localstack AWS_ACCESS_KEY=local AWS_SECRET_KEY=dev
java -jar build/libs/offender-assessments-events-<yyyy-mm-dd>.jar  
```  

### Additional configuration
The application is configurable with conventional Spring parameters.  
The Spring documentation can be found here: https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html

#### Default port
By default the application starts on port '8080'.   To override, set server.port (e.g. `SERVER_PORT=8099 java -jar build/libs/offender-assessments-api-<yyyy-mm-dd>.jar` )

### Documentation
The generated documentation for the api can be viewed at http://localhost:8080/swagger-ui.html

### Health

- `/health/ping`: will respond with the application status to all requests. This should be used by dependent systems to check connectivity to   
  this service, rather than calling the `/health` endpoint.
- `/health`: provides information about the application health and its dependencies.  This should only be used  
  by health monitoring (e.g. pager duty) and not other systems who wish to find out the   
  state of this service.
- `/info`: provides information about the version of deployed application.

#### Health and info Endpoints (curl)

##### Application ping
```  
curl -X GET http://localhost:8080/health/ping  
```

##### Application health
```  
curl -X GET http://localhost:8080/health  
```

##### Application info
```  
curl -X GET http://localhost:8080/info  
```

## Using the api

### Authentication using OAuth
In order to make queries to the api, first a client credential access token must be generated from OAuth. Send a POST request to the [OAuth container](#oauth-security) with the client name:secret, encoded in base64 (`sentence-plan-api-client:clientsecret`)

Firstly, to encode in base64: `echo -n 'sentence-plan-api-client:clientsecret' | openssl base64` to output `c2VudGVuY2UtcGxhbi1hcGktY2xpZW50OmNsaWVudHNlY3JldA==`.

Then, POST the request for the access token from OAuth, using the encoded secret as authorisation:
```
 curl --location --request POST 'http://localhost:9090/auth/oauth/token?grant_type=client_credentials' \
 --header 'Authorization: Basic c2VudGVuY2UtcGxhbi1hcGktY2xpZW50OmNsaWVudHNlY3JldA=='
```
