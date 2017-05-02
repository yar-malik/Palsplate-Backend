***PalsPlat Backend*** 

**Technologies:**

*Java 8: primary language
*Maven 3: package manager
*Spring Boot 1.1.9.RELEASE: smart stand-alone application builder
*Jersey 2.7: RESTful Webservices package
*Hibernate 4: ORM (object relations mapper) framework. Mapps java objects to tables in SQL database
*PostgreSQL: Main Production Database

- - - -
**Populate**, 
populate palsplate-demo database with some dummy data. Use the following queries: 
INSERT INTO member VALUES(1, 'Asfandyar', 'Malik', 'malikasfandyarashraf@gmail.com')
INSERT INTO product VALUES (1, 'Cheese', 'euro', 14, 12, 1);

- - - -
**Run**

mvn clean package && mvn spring-boot:run

mvn clean package && java -jar target/palsplate-backend-1.0-SNAPSHOT.jar

Go to: localhost:8080/products