CREATE TABLE hb_hanes_ultfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_hanes_ultfields_gen;
SET GENERATOR hb_hanes_ultfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_ultfields_trig FOR hb_hanes_ultfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_hanes_ultfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_ultfields to damuser;

commit;

CREATE TABLE hb_hanes_ult_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_hanes_ult_key_gen;
SET GENERATOR hb_hanes_ult_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_ult_key_trig FOR hb_hanes_ult_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_hanes_ult_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_ult_key to damuser;

commit;

CREATE TABLE hb_hanes_ult_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_hanes_ult_descript_gen;
SET GENERATOR hb_hanes_ult_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_ult_descript_trig FOR hb_hanes_ult_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_hanes_ult_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_ult_descript to damuser;

commit;

CREATE TABLE hb_hanes_ult_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_hanes_ult_paths_gen;
SET GENERATOR hb_hanes_ult_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_hanes_ult_paths_trig FOR hb_hanes_ult_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_hanes_ult_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_ult_paths to damuser;

COMMIT;

CREATE TABLE hb_hanes_ult(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_hanes_ult_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_hanes_ult_gen;
SET GENERATOR hb_hanes_ult_gen TO 1;

grant all on hb_hanes_ult to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_ult_trig FOR hb_hanes_ult BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_hanes_ult_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_hanes_ultcategory(
product_id INTEGER NOT NULL REFERENCES hb_hanes_ult(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_ultfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_hanes_ult_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_hanes_ultcategory to damuser;

commit;

CREATE TABLE hb_hanes_ultdescription(
product_id INTEGER NOT NULL REFERENCES hb_hanes_ult(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_ultfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_hanes_ult_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_hanes_ultdescription to damuser;

commit;

CREATE TABLE hb_hanes_ultstats(
product_id INTEGER NOT NULL REFERENCES hb_hanes_ult(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_ultfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_hanes_ultstats to damuser;

commit;

CREATE TABLE hb_hanes_ultprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_hanes_ultprice_keys_gen;
SET GENERATOR hb_hanes_ultprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_ultprice_keys_trig FOR hb_hanes_ultprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_hanes_ultprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_ultprice_keys to damuser;

commit;

CREATE TABLE hb_hanes_ultprice(
product_id INTEGER NOT NULL REFERENCES hb_hanes_ult(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hanes_ultprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_hanes_ultprice to damuser;

commit;

CREATE TABLE hb_hanes_ultprice_break(
product_id INTEGER NOT NULL REFERENCES hb_hanes_ult(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hanes_ultprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_hanes_ultprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_hanes_ult','Hanes Ultimate','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_hanes_ultfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('hb_hanes_ult','Hanes Ultimate','hb_hanes_ultorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_hanes_ultorders@lazerinc.com',1,0.0);

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_hanes_ult'),'hb_hanes_ult');


insert into resources (name,resource_type) values('hb_hanes_ult',3);
insert into resources (name,resource_type) values('hb_hanes_ult.expired',8);

COMMIT;

CREATE TABLE hb_hanes_sleepfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_hanes_sleepfields_gen;
SET GENERATOR hb_hanes_sleepfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_sleepfields_trig FOR hb_hanes_sleepfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_hanes_sleepfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_sleepfields to damuser;

commit;

CREATE TABLE hb_hanes_sleep_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_hanes_sleep_key_gen;
SET GENERATOR hb_hanes_sleep_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_sleep_key_trig FOR hb_hanes_sleep_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_hanes_sleep_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_sleep_key to damuser;

commit;

CREATE TABLE hb_hanes_sleep_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_hanes_sleep_descript_gen;
SET GENERATOR hb_hanes_sleep_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_sleep_descript_trig FOR hb_hanes_sleep_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_hanes_sleep_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_sleep_descript to damuser;

commit;

CREATE TABLE hb_hanes_sleep_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_hanes_sleep_paths_gen;
SET GENERATOR hb_hanes_sleep_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_hanes_sleep_paths_trig FOR hb_hanes_sleep_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_hanes_sleep_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_sleep_paths to damuser;

COMMIT;

CREATE TABLE hb_hanes_sleep(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_hanes_sleep_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_hanes_sleep_gen;
SET GENERATOR hb_hanes_sleep_gen TO 1;

grant all on hb_hanes_sleep to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_sleep_trig FOR hb_hanes_sleep BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_hanes_sleep_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_hanes_sleepcategory(
product_id INTEGER NOT NULL REFERENCES hb_hanes_sleep(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_sleepfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_hanes_sleep_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_hanes_sleepcategory to damuser;

commit;

CREATE TABLE hb_hanes_sleepdescription(
product_id INTEGER NOT NULL REFERENCES hb_hanes_sleep(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_sleepfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_hanes_sleep_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_hanes_sleepdescription to damuser;

commit;

CREATE TABLE hb_hanes_sleepstats(
product_id INTEGER NOT NULL REFERENCES hb_hanes_sleep(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hanes_sleepfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_hanes_sleepstats to damuser;

commit;

CREATE TABLE hb_hanes_sleepprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_hanes_sleepprice_keys_gen;
SET GENERATOR hb_hanes_sleepprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hanes_sleepprice_keys_trig FOR hb_hanes_sleepprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_hanes_sleepprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hanes_sleepprice_keys to damuser;

commit;

CREATE TABLE hb_hanes_sleepprice(
product_id INTEGER NOT NULL REFERENCES hb_hanes_sleep(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hanes_sleepprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_hanes_sleepprice to damuser;

commit;

CREATE TABLE hb_hanes_sleepprice_break(
product_id INTEGER NOT NULL REFERENCES hb_hanes_sleep(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hanes_sleepprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_hanes_sleepprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_hanes_sleep','Hanes Sleepwear','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_hanes_sleepfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('hb_hanes_sleep','Hanes Sleepwear','hb_hanes_sleeporders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_hanes_sleeporders@lazerinc.com',1,0.0);

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_hanes_sleep'),'hb_hanes_sleep');


insert into resources (name,resource_type) values('hb_hanes_sleep',3);
insert into resources (name,resource_type) values('hb_hanes_sleep.expired',8);

COMMIT;

