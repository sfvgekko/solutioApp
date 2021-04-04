# solutioApp
Solutio App is a microservice for Twitter API testing purposes. 

## Tech Stack
Java 11 - Spring Boot 2.4.4 - Maven 3.6.3

## Start Application
1. Install application: ```mvn clean install```
2. Run jar file: ```java -jar ./target/solutio-0.0.1.jar```

## Functionality
The application consumes tweets to the user timeline and store them according to certain criteria.
The user Oauth credentials must be declared in the file **twitter4j.properties**.

Also exposed an API with the next operations:
* Get a list with all the tweets stored.
* Mark a tweet as validated.
* Get a list of validated tweets for an specific user.
* Get a list with the most used Hashtags within the stored tweets.

## Security
Basic authentication is required for used the API endpoints.
The application create by deafault 10 users for testing purposes: **user1** to **user10** as username.
The password is **solutio** for all the users.

## Swagger and H2
Swagger UI is included in path ```/swagger-UI.html```

H2 console is included in path ```/h2-console/```

## Postman
Postman requests collection is allowed to test the application in the next link:

https://www.getpostman.com/collections/df911da734594f105879

## Properties Files
The application has 2 properties files:
### application.properties
rules.allowedLanguages=es, fr, it //allowed languages indicating the language code separated by comma

rules.minFollowers=1500 //minimum number of followers required for store a tweet

rules.maxTextLength=280 //maximum length of text that will be stored (trimmed if it is higher)

rules.itemsInHashtagRank=10 //number of items returned for the hashtag rank list
## twitter4j.properties
oauth.consumerKey= //user consumer key **REQUIRED**

oauth.consumerSecret= //user consumer secret **REQUIRED**

oauth.accessToken= //app token 

oauth.accessTokenSecret= //app secret
