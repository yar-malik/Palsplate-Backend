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
INSERT INTO login VALUES (1, 'Asfandyar', 'mypass');
INSERT INTO login VALUES (2, 'Giroud', 'giroud_pass');
INSERT INTO login VALUES (3, 'ramsey', 'ramsey_mypass');

INSERT INTO PERSON VALUES (1, 'asfandyar@gmail.com', 'Asfandyar', 'Malik', '01575117434', 'islamabad', 'best chef ever', true, 1)	
INSERT INTO PERSON VALUES (2, 'giroud@gmail.com', 'Oliver', 'Giroud', '+1575117434', 'paris', 'love food ever', true, 2)	
INSERT INTO PERSON VALUES (3, 'ramsey@gmail.com', 'Aaron', 'Ramsey', '+157511743434', 'wales', 'foodie', true, 3)	

INSERT INTO Customer VALUES (1, 1)

INSERT INTO Cook VALUES (1, 2)
INSERT INTO Cook VALUES (2, 3)

INSERT INTO FOOD VALUES(1, 'Biryani', '2011-05-16 15:36:38', '2011-06-16 15:36:38', 'Indian delight', 5, 3, 'vegetarian', 'Indian', 0.45, 0.56, true, 1)
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
where f.is_active = 't'
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


- - - -
## REST REQUEST

Following is a list of some examples of rest requests one can use:

```
GET: http://localhost:8080/cooks
GET: http://localhost:8080/cooks/1
GET: http://localhost:8080/foods
GET: http://localhost:8080/cooks/1/foods
GET: http://localhost:8080/logins
GET: http://localhost:8080/customers/1
```

### Login
* Get one specific record
``GET http://localhost:8080/logins/{id}``
* Update a specific record
``PUT http://localhost:8080/logins/{id}``
* Delete a specific record
``DELETE http://localhost:8080/logins/{id}``
* Create a new record with following payload
``POST http://localhost:8080/logins``
```
{
  "userName":"Ronaldo",
  "password":"mypass"
}
```
where {id} is the unique id identifying a customer


### Person

 * Get one specific record 
 ``GET http://localhost:8080/persons/{id}``
 * Update a specific record
 ``PUT http://localhost:8080/logins/{id}``
 * Delete a specific record
 ``DELETE http://localhost:8080/logins/{id}``
 * Create a new record with following payload

`POST http://localhost:8080/persons`
```
{
"email": "wishere@gmail.com",
"firstName": "Jack",
"lastName": "wishere",
"phoneNumber": "+157511743434",
"address": "uk",
"description": "foodie",
"isPhotoPublic": "true",
"login_id": 4
}
```

`curl -X POST -d @curlJson.txt http://localhost:8080/persons --header "Content-Type:application/json"`

where curlJson.txt contains:
```
{
"email": "Saad@gmail.com",
"firstName": "Saad",
"lastName": "Saeed",
"phoneNumber": "+157511743434",
"address": "Germany",
"description": "foodie",
"isPhotoPublic": "true",
"login_id": 2
}

```


### Cook

 * Get one specific record 
 ``GET http://localhost:8080/cooks/{id}``
 * Update a specific record
 ``PUT http://localhost:8080/cooks/{id}``
 * Delete a specific record
 ``DELETE http://localhost:8080/cooks/{id}``
 * Create a new record with a curl example


`curl -X POST -d @curlJsonCook.txt http://localhost:8080/cooks --header "Content-Type:application/json"`

where curlJsonCook.txt contains:
```
{
  "person_id": 7
}
```



### Customers

 * Get one specific record 
 ``GET http://localhost:8080/customers/{id}``
 * Update a specific record
 ``PUT http://localhost:8080/customers/{id}``
 * Delete a specific record
 ``DELETE http://localhost:8080/customers/{id}``
 * Create a new record with a curl example


`curl -X POST -d @curlJsonCustomer.txt http://localhost:8080/customers --header "Content-Type:application/json"`

where curlJsonCustomer.txt contains:
```
{
  "person_id": 7
}
```

### Foods

 * Get one specific record 
 ``GET http://localhost:8080/foods/{id}``
 * Update a specific record
 ``PUT http://localhost:8080/foods/{id}``
 * Delete a specific record
 ``DELETE http://localhost:8080/foods/{id}``
 * Create a new record with a curl example
 
 `curl -X POST -d @curlJsonFood.txt http://localhost:8080/foods --header "Content-Type:application/json"`

 ````
 {
 "name": "Veggie special",
 "offer_start": 1305552998000,
 "offer_stop": 1308231398000,
 "description": "vegetarian delight",
 "price": 5,
 "portion": 2,
 "food_type": "veggie",
 "cuisine_type": "fast_food",
 "lat": 0.45,
 "lon": 0.56,
 "is_active": true,
 "cook_id": 3
 }
 ````


### Useful Rest Requests

* Get all foods for a specific cook
`` GET http://localhost:8080/cooks/1/foods ``


### @Annotations

We implemented Jersey with Spring for REST support using JAX-RS API. Although, Spring has pretty good REST support.

Hence you will see difference in Annotations like @PATH being used rather than @RequestMapping


### Security

Basic Authentication has been set up. In order to make api calls, Base64 encode your username and password and send it
in header. Roles are assigned to user with varying priviledges. 
