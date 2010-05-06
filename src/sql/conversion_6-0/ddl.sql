insert into rights(name) values('download_orig');

COMMIT;

ALTER TABLE address add in_use varchar(5);

COMMIT;

CREATE TABLE irwinfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR irwinfields_gen;
SET GENERATOR irwinfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER irwinfields_trig FOR irwinfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(irwinfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on irwinfields to damuser;

commit;

CREATE TABLE irwin_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR irwin_key_gen;
SET GENERATOR irwin_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER irwin_key_trig FOR irwin_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(irwin_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on irwin_key to damuser;

commit;

CREATE TABLE irwin_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR irwin_descript_gen;
SET GENERATOR irwin_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER irwin_descript_trig FOR irwin_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(irwin_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on irwin_descript to damuser;

commit;

CREATE TABLE irwin_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR irwin_paths_gen;
SET GENERATOR irwin_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER irwin_paths_trig FOR irwin_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(irwin_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on irwin_paths to damuser;

COMMIT;

CREATE TABLE irwin(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES irwin_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
date_modified TIMESTAMP,
UNIQUE(primary_label,path_id));
CREATE GENERATOR irwin_gen;
SET GENERATOR irwin_gen TO 1;

grant all on irwin to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER irwin_trig FOR irwin BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(irwin_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE irwincategory(
product_id INTEGER NOT NULL REFERENCES irwin(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES irwinfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES irwin_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on irwincategory to damuser;

commit;

CREATE TABLE irwindescription(
product_id INTEGER NOT NULL REFERENCES irwin(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES irwinfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES irwin_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on irwindescription to damuser;

commit;

CREATE TABLE irwinstats(
product_id INTEGER NOT NULL REFERENCES irwin(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES irwinfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on irwinstats to damuser;

commit;

CREATE TABLE irwinprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR irwinprice_keys_gen;
SET GENERATOR irwinprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER irwinprice_keys_trig FOR irwinprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(irwinprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on irwinprice_keys to damuser;

commit;

CREATE TABLE irwinprice(
product_id INTEGER NOT NULL REFERENCES irwin(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES irwinprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on irwinprice to damuser;

commit;

CREATE TABLE irwinprice_break(
product_id INTEGER NOT NULL REFERENCES irwin(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES irwinprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on irwinprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model,customer_name,remote_managed) 
values('irwin','Irwin Tools On-Line Image Library','On-Line Library','File Name',1,'com.lazerinc.ecommerce.LazerwebOrderModel','Irwin Tools','false');

COMMIT;

insert into irwinfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into irwinfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into irwinfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into irwinfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into irwinfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into irwinfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('irwin','Irwin Tools On-Line Image Library','irwinorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'irwinorders@lazerinc.com',1,0.0);

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='irwin'),'irwin');


insert into resources (name,resource_type) values('irwin',3);
insert into resources (name,resource_type) values('irwin.expired',8);

COMMIT;


