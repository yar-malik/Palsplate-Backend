# PalsPlate Backend 

## Technology Stack:

* Java 8: primary language
* Maven 3: package manager
* Spring Boot 1.1.9.RELEASE: smart stand-alone application builder
* Jersey 2.7: RESTful Webservices package
* Hibernate 4: ORM (object relations mapper) framework. Mapps java objects to tables in SQL database
* PostgreSQL: Main Production Database

- - - -
## Populate Database:

Populate palsplate-demo database with some dummy data.

Use the following queries: 

```
INSERT INTO member VALUES(1, 'Asfandyar', 'Malik', 'malikasfandyarashraf@gmail.com')

INSERT INTO product VALUES (1, 'Cheese', 'euro', 14, 12, 1);`
INSERT INTO product VALUES (3, 'Yogurt', 'euro', 2, 1, 1);

INSERT INTO login VALUES (1, 'Asfandyar', 'mypass');
INSERT INTO login VALUES (2, 'Giroud', 'giroud_pass');
INSERT INTO login VALUES (3, 'ramsey', 'ramsey_mypass');

INSERT INTO PERSON VALUES (1, 'asfandyar@gmail.com', 'Asfandyar', 'Malik', '01575117434', 'islamabad', 'best chef ever', true, 1)	
INSERT INTO PERSON VALUES (2, 'giroud@gmail.com', 'Oliver', 'Giroud', '+1575117434', 'paris', 'love food ever', true, 2)	
INSERT INTO PERSON VALUES (3, 'ramsey@gmail.com', 'Aaron', 'Ramsey', '+157511743434', 'wales', 'foodie', true, 3)	

INSERT INTO Customer VALUES (1, 1)

INSERT INTO Cook VALUES (1, 2)
INSERT INTO Cook VALUES (2, 3)

INSERT INTO FOOD VALUES(2, 'Omlette', '2011-05-16 15:36:38', '2011-06-16 15:36:38', 'French delight', 5, 3, 'vegetarian', 'French', 0.45, 0.56, true, 1)
INSERT INTO FOOD VALUES(3, 'burger', '2011-05-16 15:36:38', '2011-06-16 15:36:38', 'fast food delight', 5, 3, 'meat', 'fast_food', 0.45, 0.56, true, 2)
```

- - - -
## QUERY DATABASE

Following is a query which finds out all persons/users who are cooks and currently have food offerings

```
select * 
from cook c
inner join person p on c.person_id = p.id
inner join food f on c.id = f.cook_id
where f.is_active = 't';
limit 10;
```

- - - -
## ENVIRONMENT VARIABLES

The code only uses the following environment variables.
PALSPLATE_DB_URL

This value is equal to the db url from Heroku. 
(Remember that Heroku url is different from posgres url)
Heroku Postgres url: `postgres://<username>:<password>@<host>:<port>/<dbname>`
Java Posgres url: `jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>`


- - - -
## Run Complete System

```
mvn clean package && mvn spring-boot:run
```
```
mvn clean package && java -jar target/palsplate-backend-1.0-SNAPSHOT.jar
```

Go to: `localhost:8080/products`
