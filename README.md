# java-ee-rxjava-kafka-avro
Enabling Kafka as Event Source using Avro as Event Serializer/Deserializer and RxJava in a Java EE 7 application

## Before using it

* Ensure you have Kafka 0.8.2.1 installed and running.

## How to use it

1. Clone repository

2. Open your IDE and run it on your favorite Java EE App Server (tested on WildFly 8.2)

3. Open SoapUI or any other REST/HTTP tester and call the following methods

### Methods

* GET **/sample/api/clients** to get Clients
* POST **/sample/api/clients** to add Clients
* GET **/sample/api/events** to check Events
