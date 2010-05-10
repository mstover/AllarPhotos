CREATE TABLE <library>fields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR <library>fields_gen;
SET GENERATOR <library>fields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER <library>fields_trig FOR <library>fields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(<library>fields_gen,1);
	END
END !!
SET TERM ;!!

grant all on <library>fields to damuser;

commit;

CREATE TABLE <library>_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR <library>_key_gen;
SET GENERATOR <library>_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER <library>_key_trig FOR <library>_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(<library>_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on <library>_key to damuser;

commit;

CREATE TABLE <library>_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR <library>_descript_gen;
SET GENERATOR <library>_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER <library>_descript_trig FOR <library>_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(<library>_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on <library>_descript to damuser;

commit;

CREATE TABLE <library>_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR <library>_paths_gen;
SET GENERATOR <library>_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER <library>_paths_trig FOR <library>_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(<library>_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on <library>_paths to damuser;

COMMIT;

CREATE TABLE <library>(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES <library>_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
date_modified TIMESTAMP,
date_created TIMESTAMP,
is_active INTEGER NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR <library>_gen;
SET GENERATOR <library>_gen TO 1;

grant all on <library> to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER <library>_trig FOR <library> BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(<library>_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE <library>category(
product_id INTEGER NOT NULL REFERENCES <library>(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES <library>fields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES <library>_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on <library>category to damuser;

commit;

CREATE TABLE <library>description(
product_id INTEGER NOT NULL REFERENCES <library>(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES <library>fields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES <library>_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on <library>description to damuser;

commit;

CREATE TABLE <library>stats(
product_id INTEGER NOT NULL REFERENCES <library>(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES <library>fields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on <library>stats to damuser;

commit;

CREATE TABLE <library>price_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR <library>price_keys_gen;
SET GENERATOR <library>price_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER <library>price_keys_trig FOR <library>price_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(<library>price_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on <library>price_keys to damuser;

commit;

CREATE TABLE <library>price(
product_id INTEGER NOT NULL REFERENCES <library>(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES <library>price_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on <library>price to damuser;

commit;

CREATE TABLE <library>price_break(
product_id INTEGER NOT NULL REFERENCES <library>(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES <library>price_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on <library>price_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model,customer_name,remote_managed) 
values('<library>','<descriptive_name>','On-Line Library','File Name',1,'com.allarphoto.ecommerce.LazerwebOrderModel','<customer_name>','false');

COMMIT;

insert into <library>fields(name,display_order,search_order,field_type) values('File Type',100,0,1);
insert into <library>fields(name,display_order,search_order,field_type) values('File Size',100,0,8);
insert into <library>fields(name,display_order,search_order,field_type) values('Height',100,0,8);
insert into <library>fields(name,display_order,search_order,field_type) values('Width',100,0,8);
insert into <library>fields(name,display_order,search_order,field_type) values('Resolution',100,0,8);
insert into <library>fields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('<library>','<descriptive_name>','<library>orders@lazerinc.com','(585) 247-6600','(585) 247-9647','14607','1150 University Ave',
'N/A',2,47,236,0,'<library>orders@lazerinc.com',1,0.0);

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='<library>'),'<library>');


insert into resources (name,resource_type) values('<library>',3);
insert into resources (name,resource_type) values('<library>.expired',8);

COMMIT;

