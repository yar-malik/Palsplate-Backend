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
  created_timestamp timestamp with time zone,
  last_modified_timestamp timestamp with time zone,
  CONSTRAINT pk_person_id PRIMARY KEY (id)
)

CREATE TABLE customer
(
  id serial NOT NULL,
  CONSTRAINT pk_customer_id PRIMARY KEY (id),
  person_id bigint NOT NULL,
  created_timestamp timestamp with time zone,
  last_modified_timestamp timestamp with time zone,
  CONSTRAINT fk_customer__person FOREIGN KEY (person_id)
      REFERENCES person (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE cook
(
  id serial NOT NULL,
  person_id bigint NOT NULL,
  created_timestamp timestamp with time zone,
  last_modified_timestamp timestamp with time zone,
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
  created_timestamp timestamp with time zone,
  last_modified_timestamp timestamp with time zone,
  CONSTRAINT pk__id PRIMARY KEY (id),
  CONSTRAINT fk_food__cook FOREIGN KEY (cook_id)
    REFERENCES cook (id) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE image
(
  id serial NOT NULL,
  filename character varying,
  cloudinary_public_id character varying,
  food_id serial,
  created_timestamp timestamp with time zone,
  last_modified_timestamp timestamp with time zone,
  CONSTRAINT pk_image_id PRIMARY KEY (id),
  CONSTRAINT fk_image__food FOREIGN KEY (food_id)
    REFERENCES food (id) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE review
(
  id serial NOT NULL,
  text character varying,
  rating serial,
  food_id serial,
  created_timestamp timestamp with time zone,
  last_modified_timestamp timestamp with time zone,
  CONSTRAINT pk_review_id PRIMARY KEY (id),
  CONSTRAINT fk_review__food FOREIGN KEY (food_id)
    REFERENCES food (id) MATCH SIMPLE
    ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE reservation
(
  id serial NOT NULL,
  is_active boolean NOT NULL,
  CONSTRAINT pk_customer_id PRIMARY KEY (id),
  food_id bigint NOT NULL,
  customer_id bigint NOT NULL,
  created_timestamp timestamp with time zone,
  last_modified_timestamp timestamp with time zone,
  CONSTRAINT fk_reservation__food FOREIGN KEY (food_id)
      REFERENCES food (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
  CONSTRAINT fk_reservation__customer FOREIGN KEY (customer_id)
      REFERENCES customer (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
