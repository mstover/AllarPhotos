CREATE TABLE hb_hanes_champfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_hanes_champfields_gen;
SET GENERATOR hb_hanes_champfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_champfields_trig FOR hb_hanes_champfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_hanes_champfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_champfields to damuser;

commit;

CREATE TABLE hb_hanes_champ_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_hanes_champ_key_gen;
SET GENERATOR hb_hanes_champ_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_champ_key_trig FOR hb_hanes_champ_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_hanes_champ_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_champ_key to damuser;

commit;

CREATE TABLE hb_hanes_champ_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_hanes_champ_descript_gen;
SET GENERATOR hb_hanes_champ_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_champ_descript_trig FOR hb_hanes_champ_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_hanes_champ_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_champ_descript to damuser;

commit;

CREATE TABLE hb_hanes_champ_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_hanes_champ_paths_gen;
SET GENERATOR hb_hanes_champ_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_hanes_champ_paths_trig FOR hb_hanes_champ_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_hanes_champ_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_champ_paths to damuser;

COMMIT;

CREATE TABLE hb_hanes_champ(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_hanes_champ_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_hanes_champ_gen;
SET GENERATOR hb_hanes_champ_gen TO 1;

grant all on hb_hanes_champ to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_champ_trig FOR hb_hanes_champ BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_hanes_champ_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_hanes_champcategory(
product_id INTEGER NOT NULL REFERENCES hb_hanes_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_champfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_hanes_champ_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_hanes_champcategory to damuser;

commit;

CREATE TABLE hb_hanes_champdescription(
product_id INTEGER NOT NULL REFERENCES hb_hanes_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_champfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_hanes_champ_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_hanes_champdescription to damuser;

commit;

CREATE TABLE hb_hanes_champstats(
product_id INTEGER NOT NULL REFERENCES hb_hanes_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_champfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_hanes_champstats to damuser;

commit;

CREATE TABLE hb_hanes_champprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_hanes_champprice_keys_gen;
SET GENERATOR hb_hanes_champprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_champprice_keys_trig FOR hb_hanes_champprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_hanes_champprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_champprice_keys to damuser;

commit;

CREATE TABLE hb_hanes_champprice(
product_id INTEGER NOT NULL REFERENCES hb_hanes_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hanes_champprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_hanes_champprice to damuser;

commit;

CREATE TABLE hb_hanes_champprice_break(
product_id INTEGER NOT NULL REFERENCES hb_hanes_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hanes_champprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_hanes_champprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_hanes_champ','Hanes Brand Champion','On-Line Library','File Name',1,'com.allarphoto.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_hanes_champfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('hb_hanes_champ','Hanes Brand Champion','hb_hanes_champorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_hanes_champorders@lazerinc.com',1,0.0);

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_hanes_champ'),'hb_hanes_champ');
insert into resources (name,resource_type) values('hb_hanes_champ',3);
insert into resources (name,resource_type) values('hb_hanes_champ.expired',8);

COMMIT;

CREATE TABLE hb_hanes_magfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_hanes_magfields_gen;
SET GENERATOR hb_hanes_magfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_magfields_trig FOR hb_hanes_magfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_hanes_magfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_magfields to damuser;

commit;

CREATE TABLE hb_hanes_mag_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_hanes_mag_key_gen;
SET GENERATOR hb_hanes_mag_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_mag_key_trig FOR hb_hanes_mag_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_hanes_mag_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_mag_key to damuser;

commit;

CREATE TABLE hb_hanes_mag_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_hanes_mag_descript_gen;
SET GENERATOR hb_hanes_mag_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_mag_descript_trig FOR hb_hanes_mag_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_hanes_mag_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_mag_descript to damuser;

commit;

CREATE TABLE hb_hanes_mag_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_hanes_mag_paths_gen;
SET GENERATOR hb_hanes_mag_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_hanes_mag_paths_trig FOR hb_hanes_mag_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_hanes_mag_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_mag_paths to damuser;

COMMIT;

CREATE TABLE hb_hanes_mag(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_hanes_mag_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_hanes_mag_gen;
SET GENERATOR hb_hanes_mag_gen TO 1;

grant all on hb_hanes_mag to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_mag_trig FOR hb_hanes_mag BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_hanes_mag_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_hanes_magcategory(
product_id INTEGER NOT NULL REFERENCES hb_hanes_mag(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_magfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_hanes_mag_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_hanes_magcategory to damuser;

commit;

CREATE TABLE hb_hanes_magdescription(
product_id INTEGER NOT NULL REFERENCES hb_hanes_mag(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_magfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_hanes_mag_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_hanes_magdescription to damuser;

commit;

CREATE TABLE hb_hanes_magstats(
product_id INTEGER NOT NULL REFERENCES hb_hanes_mag(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_magfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_hanes_magstats to damuser;

commit;

CREATE TABLE hb_hanes_magprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_hanes_magprice_keys_gen;
SET GENERATOR hb_hanes_magprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_magprice_keys_trig FOR hb_hanes_magprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_hanes_magprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_magprice_keys to damuser;

commit;

CREATE TABLE hb_hanes_magprice(
product_id INTEGER NOT NULL REFERENCES hb_hanes_mag(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hanes_magprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_hanes_magprice to damuser;

commit;

CREATE TABLE hb_hanes_magprice_break(
product_id INTEGER NOT NULL REFERENCES hb_hanes_mag(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hanes_magprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_hanes_magprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_hanes_mag','Hanes Magalog','On-Line Library','File Name',1,'com.allarphoto.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_hanes_magfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('hb_hanes_mag','Hanes Magalog','hb_hanes_magorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_hanes_magorders@lazerinc.com',1,0.0);

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_hanes_mag'),'hb_hanes_mag');
insert into resources (name,resource_type) values('hb_hanes_mag',3);
insert into resources (name,resource_type) values('hb_hanes_mag.expired',8);

COMMIT;

CREATE TABLE hb_slushfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_slushfields_gen;
SET GENERATOR hb_slushfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_slushfields_trig FOR hb_slushfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_slushfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_slushfields to damuser;

commit;

CREATE TABLE hb_slush_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_slush_key_gen;
SET GENERATOR hb_slush_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_slush_key_trig FOR hb_slush_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_slush_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_slush_key to damuser;

commit;

CREATE TABLE hb_slush_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_slush_descript_gen;
SET GENERATOR hb_slush_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_slush_descript_trig FOR hb_slush_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_slush_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_slush_descript to damuser;

commit;

CREATE TABLE hb_slush_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_slush_paths_gen;
SET GENERATOR hb_slush_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_slush_paths_trig FOR hb_slush_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_slush_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_slush_paths to damuser;

COMMIT;

CREATE TABLE hb_slush(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_slush_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_slush_gen;
SET GENERATOR hb_slush_gen TO 1;

grant all on hb_slush to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_slush_trig FOR hb_slush BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_slush_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_slushcategory(
product_id INTEGER NOT NULL REFERENCES hb_slush(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_slushfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_slush_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_slushcategory to damuser;

commit;

CREATE TABLE hb_slushdescription(
product_id INTEGER NOT NULL REFERENCES hb_slush(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_slushfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_slush_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_slushdescription to damuser;

commit;

CREATE TABLE hb_slushstats(
product_id INTEGER NOT NULL REFERENCES hb_slush(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_slushfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_slushstats to damuser;

commit;

CREATE TABLE hb_slushprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_slushprice_keys_gen;
SET GENERATOR hb_slushprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_slushprice_keys_trig FOR hb_slushprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_slushprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_slushprice_keys to damuser;

commit;

CREATE TABLE hb_slushprice(
product_id INTEGER NOT NULL REFERENCES hb_slush(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_slushprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_slushprice to damuser;

commit;

CREATE TABLE hb_slushprice_break(
product_id INTEGER NOT NULL REFERENCES hb_slush(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_slushprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_slushprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_slush','Hanes Slush Library','On-Line Library','File Name',1,'com.allarphoto.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_slushfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_slushfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_slushfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_slushfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_slushfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_slushfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_slushfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_slushfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_slushfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('hb_slush','Hanes Slush Library','hb_slushorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_slushorders@lazerinc.com',1,0.0);

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_slush'),'hb_slush');
insert into resources (name,resource_type) values('hb_slush',3);
insert into resources (name,resource_type) values('hb_slush.expired',8);

COMMIT;

