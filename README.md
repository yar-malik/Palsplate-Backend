# PalsPlate Backend 

## Technology Stack:

* Java 8: primary language
* Maven 3: package manager
* Spring Boot 1.1.9.RELEASE: smart stand-alone application builder
* Jersey 2.7: RESTful Webservices package
* Hibernate 4: ORM (object relations mapper) framework. Mapps java objects to tables in SQL database
* PostgreSQL: Main Production Database


- - - -
### @Annotations

We implemented Jersey with Spring for REST support using JAX-RS API. Although, Spring has pretty good REST support.

Hence you will see difference in Annotations like @PATH being used rather than @RequestMapping

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

Go to: `localhost:8080/api/secure/products`


- - - -
### Security and Authorization

Authorization is provided by via spring security using the **oauth2.0** protocol. By design all the resources matching ``/api/secure/*`` are secured and require a login to retrieve.

There are 5 different grants to get access_token in oauth2, and in this project, `resource owner credentials grant` is being used. 

The authorization protocol follows the following mechanism.

1. Provide the username and password to retrieve the refresh token

`curl -u webclient:secret 'http://localhost:8080/api/oauth/token?username=admin&password=admin&grant_type=password'`

````{"access_token":"8789eae9-0863-4266-bff0-79e7799c910f","token_type":"bearer","refresh_token":"73f29da8-57c5-4ae3-ac4d-59a061d6c05b","expires_in":1799,"scope":"read write"}````


2. Use the access token to retrieve the desired resource

 `curl -i -H "Authorization: Bearer <access-token>" http://localhost:8080/api/secure/persons `


3. Provide the refresh token to the server to again retrieve the access token, if it gets expired

`curl -u webclient:secret 'http://localhost:8080/api/oauth/token?grant_type=refresh_token&refresh_token=<refresh-token>'  `

where <refresh-token> is received in the previous command

 ````{"access_token":"ef981a33-b431-44a9-86f3-ce4df31c6d5f","token_type":"bearer","refresh_token":"73f29da8-57c5-4ae3-ac4d-59a061d6c05b","expires_in":1799,"scope":"read write"} ````


- - - -
## REST REQUEST

Following is a list of some examples of rest requests one can use:

```
GET: http://localhost:8080/api/secure/cooks
GET: http://localhost:8080/api/secure/cooks/1
GET: http://localhost:8080/api/secure/foods
GET: http://localhost:8080/api/secure/cooks/1/foods
GET: http://localhost:8080/api/secure/logins
GET: http://localhost:8080/api/secure/customers/1
```

### Login
* Get one specific record
``GET http://localhost:8080/api/secure/logins/{id}``
* Update a specific record
``PUT http://localhost:8080/api/secure/logins/{id}``
* Delete a specific record
``DELETE http://localhost:8080/api/secure/logins/{id}``
* Create a new record with following payload
``POST http://localhost:8080/api/secure/logins``
```
{
  "userName":"Ronaldo",
  "password":"mypass"
}
```
where {id} is the unique id identifying a customer


### Person

 * Get one specific record
 ``GET http://localhost:8080/api/secure/persons/{id}``
 * Update a specific record
 ``PUT http://localhost:8080/api/secure/logins/{id}``
 * Delete a specific record
 ``DELETE http://localhost:8080/api/secure/logins/{id}``
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

`curl -X POST -d @curlJson.txt -H "Authorization: Bearer <access-token>" http://localhost:8080/api/secure/persons --header "Content-Type:application/json"`


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
 ``GET http://localhost:8080/api/secure/cooks/{id}``
 * Update a specific record
 ``PUT http://localhost:8080/api/secure/cooks/{id}``
 * Delete a specific record
 ``DELETE http://localhost:8080/api/secure/cooks/{id}``
 * Create a new record with a curl example


`curl -X POST -d @curlJsonCook.txt -H "Authorization: Bearer <access-token>" http://localhost:8080/cooks --header "Content-Type:application/json"`

where curlJsonCook.txt contains:
```
{
  "person_id": 7
}
```

### Customers

 * Get one specific record
 ``GET http://localhost:8080/api/secure/customers/{id}``
 * Update a specific record
 ``PUT http://localhost:8080/api/secure/customers/{id}``
 * Delete a specific record
 ``DELETE http://localhost:8080/api/secure/customers/{id}``
 * Create a new record with a curl example


`curl -X POST -d @curlJsonCustomer.txt -H "Authorization: Bearer <access-token>" http://localhost:8080/customers --header "Content-Type:application/json"`

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

 `curl -X POST -d @curlJsonFood.txt -H "Authorization: Bearer <access-token>" http://localhost:8080/foods --header "Content-Type:application/json"`


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

### Upload Images

`curl -H "Authorization: Bearer <access-token>" -F "file=@khaled.jpg"  -F "thumbnail=@khaled.jpg" -F metadata='{"file_size":879394}' http://localhost:8080/api/secure/images/upload`

### Useful Rest Requests

* Get all foods for a specific cook
`` GET http://localhost:8080/cooks/1/foods ``


- - - -
### Security and Authorization

Authorization is provided by via spring security using the **oauth2.0** protocol. By design all the resources matching ``/api/secure/*`` are secured and require a login to retrieve.

There are 5 different grants to get access_token in oauth2, and in this project, `resource owner credentials grant` is being used. 

The authorization protocol follows the following mechanism.

1. Provide the username and password to retrieve the refresh token

`curl -u webclient:secret 'http://localhost:8080/api/oauth/token?username=admin&password=admin&grant_type=password'`

````{"access_token":"8789eae9-0863-4266-bff0-79e7799c910f","token_type":"bearer","refresh_token":"73f29da8-57c5-4ae3-ac4d-59a061d6c05b","expires_in":1799,"scope":"read write"}````


2. Use the access token to retrieve the desired resource

 `curl -i -H "Authorization: Bearer <access-token>" http://localhost:8080/api/secure/persons `


3. Provide the refresh token to the server to again retrieve the access token, if it gets expired

`curl -u webclient:secret 'http://localhost:8080/api/oauth/token?grant_type=refresh_token&refresh_token=<refresh-token>'  `

where <refresh-token> is received in the previous command

 ````{"access_token":"ef981a33-b431-44a9-86f3-ce4df31c6d5f","token_type":"bearer","refresh_token":"73f29da8-57c5-4ae3-ac4d-59a061d6c05b","expires_in":1799,"scope":"read write"} ````
