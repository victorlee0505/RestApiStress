# Rest Api Stress Test
Simple load test on Rest Api service.

## Setup
JDK 1.8, SpringBoot 2.3.8, maven 3+

## Auth0
Default: disabled
modify application.properties for your needs

Auth0 classes are implementated and act as example to plug token in Api Call.

## Test Cases
located test/.../service/ApiStressTest.java
- Test Single Api Call
- Test Sequencial Api Calls
- Test Concurrent Api Calls
- Test Concurrent Api Calls w Randomized query param
