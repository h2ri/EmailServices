
# emailservices schema

# --- !Ups

CREATE TABLE emailservices (
    id SERIAL PRIMARY KEY,
    template_name varchar(255) NOT NULL,
    template_description varchar(255) NOT NULL
);
INSERT INTO emailservices (template_name,template_description) Values('login','Use this After a customer has logged in');
INSERT INTO emailservices (template_name,template_description) Values('registration','Use this after a user is registered first time');
INSERT INTO emailservices (template_name,template_description) Values('order_placed','Email Template while the order is placed');

# --- !Downs

DROP TABLE emailservices;