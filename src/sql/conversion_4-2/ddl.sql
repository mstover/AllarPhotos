CREATE TABLE hb_logosfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_logosfields_gen;
SET GENERATOR hb_logosfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_logosfields_trig FOR hb_logosfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_logosfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_logosfields to damuser;

commit;

CREATE TABLE hb_logos_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_logos_key_gen;
SET GENERATOR hb_logos_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_logos_key_trig FOR hb_logos_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_logos_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_logos_key to damuser;

commit;

CREATE TABLE hb_logos_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_logos_descript_gen;
SET GENERATOR hb_logos_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_logos_descript_trig FOR hb_logos_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_logos_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_logos_descript to damuser;

commit;

CREATE TABLE hb_logos_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_logos_paths_gen;
SET GENERATOR hb_logos_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_logos_paths_trig FOR hb_logos_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_logos_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_logos_paths to damuser;

COMMIT;

CREATE TABLE hb_logos(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_logos_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_logos_gen;
SET GENERATOR hb_logos_gen TO 1;

grant all on hb_logos to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_logos_trig FOR hb_logos BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_logos_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_logoscategory(
product_id INTEGER NOT NULL REFERENCES hb_logos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_logosfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_logos_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_logoscategory to damuser;

commit;

CREATE TABLE hb_logosdescription(
product_id INTEGER NOT NULL REFERENCES hb_logos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_logosfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_logos_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_logosdescription to damuser;

commit;

CREATE TABLE hb_logosstats(
product_id INTEGER NOT NULL REFERENCES hb_logos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_logosfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_logosstats to damuser;

commit;

CREATE TABLE hb_logosprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_logosprice_keys_gen;
SET GENERATOR hb_logosprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_logosprice_keys_trig FOR hb_logosprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_logosprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_logosprice_keys to damuser;

commit;

CREATE TABLE hb_logosprice(
product_id INTEGER NOT NULL REFERENCES hb_logos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_logosprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_logosprice to damuser;

commit;

CREATE TABLE hb_logosprice_break(
product_id INTEGER NOT NULL REFERENCES hb_logos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_logosprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_logosprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_logos','Hanesbrand Logos','On-Line Library','File Name',1,'com.allarphoto.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_logosfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_logosfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_logosfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_logosfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_logosfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_logosfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_logosfields(name,display_order,search_order,field_type) values('File Name',1,0,6);
insert into hb_logosfields(name,display_order,search_order,field_type) values('Library Name',1100,500,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('Archive',1105,100,-1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('Brand',1107,95,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('Image Type',1110,90,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('Sub-Brand',1115,85,1);

insert into hb_logosfields(name,display_order,search_order,field_type) values('_thumb',0,0,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('_originals',0,0,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('_web',0,0,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('_jpg',0,0,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('_eps',0,0,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('_pdf',0,0,1);
insert into hb_logosfields(name,display_order,search_order,field_type) values('_png',0,0,1);


COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('hb_logos','Hanesbrand Logos','hb_logosorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_logosorders@lazerinc.com',1,0.0);

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_logos'),'hb_logos');


insert into resources (name,resource_type) values('hb_logos',3);
insert into resources (name,resource_type) values('hb_logos.expired',8);

COMMIT;

