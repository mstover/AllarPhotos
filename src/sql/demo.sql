
CREATE TABLE demofields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR demofields_gen;
SET GENERATOR demofields_gen TO 0;
SET TERM !!;
CREATE TRIGGER demofields_trig FOR demofields BEFORE INSERT AS
BEGIN
	new.field_id=GEN_ID(demofields_gen,1);
END !!
SET TERM ;!!

CREATE TABLE demo_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR demo_key_gen;
SET GENERATOR demo_key_gen TO 0;
SET TERM !!;
CREATE TRIGGER demo_key_trig FOR demo_key BEFORE INSERT AS
BEGIN
	new.keyword_id=GEN_ID(demo_key_gen,1);
END !!
SET TERM ;!!

CREATE TABLE demo_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR demo_descript_gen;
SET GENERATOR demo_descript_gen TO 0;
SET TERM !!;
CREATE TRIGGER demo_descript_trig FOR demo_descript BEFORE INSERT AS
BEGIN
	new.description_id=GEN_ID(demo_descript_gen,1);
END !!
SET TERM ;!!

CREATE TABLE demo(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL UNIQUE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL);
CREATE GENERATOR demo_gen;
SET GENERATOR demo_gen TO 0;
SET TERM !!;
CREATE TRIGGER demo_trig FOR demo BEFORE INSERT AS
BEGIN
	new.product_id=GEN_ID(demo_gen,1);
END !!
SET TERM ;!!

CREATE TABLE democategory(
product_id INTEGER NOT NULL REFERENCES demo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES demofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES demo_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

CREATE TABLE demodescription(
product_id INTEGER NOT NULL REFERENCES demo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES demofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES demo_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

CREATE TABLE demostats(
product_id INTEGER NOT NULL REFERENCES demo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES demofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

CREATE TABLE demoprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR demoprice_keys_gen;
SET GENERATOR demoprice_keys_gen TO 0;
SET TERM !!;
CREATE TRIGGER demoprice_keys_trig FOR demoprice_keys BEFORE INSERT AS
BEGIN
	new.price_key_id=GEN_ID(demoprice_keys_gen,1);
END !!
SET TERM ;!!

CREATE TABLE demoprice(
product_id INTEGER NOT NULL REFERENCES demo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES demoprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

CREATE TABLE demoprice_break(
product_id INTEGER NOT NULL REFERENCES demo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES demoprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

COMMIT;
