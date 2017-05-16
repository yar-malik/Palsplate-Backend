CREATE TABLE member
(
  id serial NOT NULL,
  first_name character varying NOT NULL,
  last_name character varying NOT NULL,
  email character varying NOT NULL,
  CONSTRAINT pk_user_id PRIMARY KEY (id)
)

CREATE TABLE product
(
  id serial NOT NULL,
  name character varying NOT NULL,
  currency character varying NOT NULL,
  regular_price numeric NOT NULL,
  discount_price numeric NOT NULL,
  member_id bigint NOT NULL,
  CONSTRAINT pk_product_id PRIMARY KEY (id),
  CONSTRAINT fk_product__member FOREIGN KEY (member_id)
      REFERENCES member (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE login
(
  id serial NOT NULL,
  username character varying NOT NULL,
  password character varying NOT NULL,
  CONSTRAINT pk_login_id PRIMARY KEY (id)
)

CREATE TABLE person
(
  id serial NOT NULL,
  email character varying NOT NULL,
  first_name character varying NOT NULL,
  last_name character varying NOT NULL,
  phone_number character varying NOT NULL,
  address character varying NOT NULL,
  description character varying NOT NULL,
  is_photo_public boolean NOT NULL,
  login_id bigint NOT NULL,
  CONSTRAINT pk_person_id PRIMARY KEY (id),
  CONSTRAINT fk_person__login FOREIGN KEY (login_id)
    REFERENCES login (id) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE customer
(
  id serial NOT NULL,
  CONSTRAINT pk_customer_id PRIMARY KEY (id),
  person_id bigint NOT NULL,
  CONSTRAINT fk_customer__person FOREIGN KEY (person_id)
      REFERENCES person (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE cook
(
  id serial NOT NULL,
  person_id bigint NOT NULL,
  CONSTRAINT pk_cook_id PRIMARY KEY (id),
  CONSTRAINT fk_cook__person FOREIGN KEY (person_id)
    REFERENCES person (id) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE food
(
  id serial NOT NULL,
  name character varying NOT NULL,
  offer_start timestamp NOT NULL,
  offer_stop timestamp NOT NULL,
  description character varying NOT NULL,
  price numeric NOT NULL,
  portion numeric NOT NULL,
  food_type character varying NOT NULL,
  cuisine_type character varying NOT NULL,
  lat numeric NOT NULL,
  lon numeric NOT NULL,
  is_active boolean NOT NULL,
  cook_id bigint NOT NULL,
  CONSTRAINT pk__id PRIMARY KEY (id),
  CONSTRAINT fk_food__cook FOREIGN KEY (cook_id)
    REFERENCES cook (id) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE
)