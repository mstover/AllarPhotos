Create database "d:\lazerweb\lazerweb.gdb" USER "damuser" PASSWORD "r!VAld0" PAGE_SIZE 4096;
CREATE TABLE product_tables(
product_table_id INTEGER NOT NULL PRIMARY KEY,
table_name VARCHAR(25) NOT NULL UNIQUE,
descriptive_name VARCHAR(50) NOT NULL UNIQUE,
description VARCHAR(32765) NOT NULL,
primary_label VARCHAR(35) NOT NULL,
product_type INTEGER NOT NULL,
order_model VARCHAR(75) NOT NULL,
customer_name VARCHAR(50) NOT NULL,
remote_managed VARCHAR(8) NOT NULL);

CREATE GENERATOR product_tables_gen;
SET GENERATOR product_tables_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER product_tables_trig FOR product_tables BEFORE INSERT AS
BEGIN
	if(new.product_table_id is null) then
	BEGIN
		new.product_table_id=GEN_ID(product_tables_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE cities(
city_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE);
CREATE GENERATOR cities_gen;
SET GENERATOR cities_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER cities_trig FOR cities BEFORE INSERT AS
BEGIN
	if(new.city_id is null) then
	BEGIN
		new.city_id=GEN_ID(cities_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE states(
state_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE,
abbr VARCHAR(5) NOT NULL);
CREATE GENERATOR states_gen;
SET GENERATOR states_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER states_trig FOR states BEFORE INSERT AS
BEGIN
	if(new.state_id is null) then
	BEGIN
		new.state_id=GEN_ID(states_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE countries(
country_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE,
country_code VARCHAR(25) NOT NULL UNIQUE);
CREATE GENERATOR countries_gen;
SET GENERATOR countries_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER countries_trig FOR countries BEFORE INSERT AS
BEGIN
	if(new.country_id is null) then
	BEGIN
		new.country_id=GEN_ID(countries_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE industries(
industry_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE);
CREATE GENERATOR industries_gen;
SET GENERATOR industries_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER industries_trig FOR industries BEFORE INSERT AS
BEGIN
	if(new.industry_id is null) then
	BEGIN
		new.industry_id=GEN_ID(industries_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE companies(
company_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(60) NOT NULL UNIQUE,
industry_id INTEGER NOT NULL REFERENCES industries(industry_id) ON UPDATE CASCADE ON DELETE CASCADE);
CREATE GENERATOR companies_gen;
SET GENERATOR companies_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER companies_trig FOR companies BEFORE INSERT AS
BEGIN
	if(new.company_id is null) then
	BEGIN
		new.company_id=GEN_ID(companies_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE referrers(
referrer_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE);
CREATE GENERATOR referrers_gen;
SET GENERATOR referrers_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER referrers_trig FOR referrers BEFORE INSERT AS
BEGIN
	if(new.referrer_id is null) then
	BEGIN
		new.referrer_id=GEN_ID(referrers_gen,1);
	END
END !!
SET TERM ; !!

COMMIT;

CREATE TABLE users(
user_id INTEGER NOT NULL PRIMARY KEY,
username VARCHAR(35) NOT NULL UNIQUE,
passwd VARCHAR(80) NOT NULL,
email VARCHAR(80) NOT NULL,
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL,
middle_initial VARCHAR(5) NOT NULL,
phone VARCHAR(25) NOT NULL,
bill_address1 VARCHAR(50) NOT NULL,
ship_address1 VARCHAR(50) NOT NULL,
bill_address2 VARCHAR(50) NOT NULL,
ship_address2 VARCHAR(50) NOT NULL,
bill_city_id INTEGER NOT NULL REFERENCES cities(city_id) ON UPDATE CASCADE ON DELETE CASCADE,
ship_city_id INTEGER NOT NULL REFERENCES cities(city_id) ON UPDATE CASCADE ON DELETE CASCADE,
bill_state_id INTEGER NOT NULL REFERENCES states(state_id) ON UPDATE CASCADE ON DELETE CASCADE,
ship_state_id INTEGER NOT NULL REFERENCES states(state_id) ON UPDATE CASCADE ON DELETE CASCADE,
bill_zip VARCHAR(12) NOT NULL,
ship_zip VARCHAR(12) NOT NULL,
bill_country_id INTEGER NOT NULL REFERENCES countries(country_id) ON UPDATE CASCADE ON DELETE CASCADE,
ship_country_id INTEGER NOT NULL REFERENCES countries(country_id) ON UPDATE CASCADE ON DELETE CASCADE,
fax VARCHAR(25) NOT NULL,
company_id INTEGER NOT NULL REFERENCES companies(company_id) ON UPDATE CASCADE ON DELETE CASCADE,
referrer_id INTEGER NOT NULL REFERENCES referrers(referrer_id) ON UPDATE CASCADE ON DELETE CASCADE);
CREATE GENERATOR users_gen;
SET GENERATOR users_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER users_trig FOR users BEFORE INSERT AS
BEGIN
	if(new.user_id is null) then
	BEGIN
		new.user_id=GEN_ID(users_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE merchants(
merchant_id VARCHAR(50) NOT NULL PRIMARY KEY,
name VARCHAR(60) NOT NULL UNIQUE,
ordering_email VARCHAR(60) NOT NULL,
phone VARCHAR(25) NOT NULL,
fax VARCHAR(25) NOT NULL,
zip VARCHAR(15) NOT NULL,
address1 VARCHAR(50) NOT NULL,
address2 VARCHAR(50) NOT NULL,
city_id INTEGER NOT NULL REFERENCES cities(city_id) ON UPDATE CASCADE ON DELETE CASCADE,
state_id INTEGER NOT NULL REFERENCES states(state_id) ON UPDATE CASCADE ON DELETE CASCADE,
country_id INTEGER NOT NULL REFERENCES countries(country_id) ON UPDATE CASCADE ON DELETE CASCADE,
credit_cards VARCHAR(20) NOT NULL,
fulfillment_email VARCHAR(60) NOT NULL,
order_processing INTEGER NOT NULL,
sales_tax FLOAT NOT NULL);

CREATE TABLE merchant_tax_states(
merchant_id VARCHAR(50) NOT NULL REFERENCES merchants(merchant_id) ON UPDATE CASCADE ON DELETE CASCADE,
state_id INTEGER NOT NULL REFERENCES states(state_id) ON UPDATE CASCADE ON DELETE CASCADE);

CREATE TABLE groups(
group_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(35) NOT NULL UNIQUE,
description VARCHAR(32765));
CREATE GENERATOR groups_gen;
SET GENERATOR groups_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER groups_trig FOR groups BEFORE INSERT AS
BEGIN
	if(new.group_id is null) then
	BEGIN
		new.group_id=GEN_ID(groups_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE credit_cards(
credit_id INTEGER NOT NULL PRIMARY KEY,
cc_type INTEGER NOT NULL,
cc_number VARCHAR(60) NOT NULL UNIQUE,
exp_date TIMESTAMP NOT NULL,
first_name VARCHAR(30) NOT NULL,
last_name VARCHAR(30) NOT NULL,
middle_initial VARCHAR(5) NOT NULL);
CREATE GENERATOR credit_cards_gen;
SET GENERATOR credit_cards_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER credit_cards_trig FOR credit_cards BEFORE INSERT AS
BEGIN
	if(new.credit_id is null) then
	BEGIN
		new.credit_id=GEN_ID(credit_cards_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE transactions(
transaction_id INTEGER NOT NULL PRIMARY KEY,
date_entered TIMESTAMP NOT NULL,
date_fulfilled TIMESTAMP,
auth_code VARCHAR(20),
bill_code VARCHAR(20),
credit_id INTEGER NOT NULL REFERENCES credit_cards(credit_id) ON UPDATE CASCADE ON DELETE CASCADE,
reference_num INTEGER NOT NULL UNIQUE,
request_num VARCHAR(30) NOT NULL,
auth_msg VARCHAR(800),
bill_msg VARCHAR(800),
auth_amount FLOAT NOT NULL,
bill_amount FLOAT,
time_billed VARCHAR(20),
merchant_id VARCHAR(50) NOT NULL REFERENCES merchants(merchant_id) ON UPDATE CASCADE ON DELETE CASCADE,
user_id INTEGER NOT NULL REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
currency VARCHAR(10) NOT NULL);
CREATE GENERATOR transactions_gen;
SET GENERATOR transactions_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER transactions_trig FOR transactions BEFORE INSERT AS
BEGIN
	if(new.transaction_id is null) then
	BEGIN
		new.transaction_id=GEN_ID(transactions_gen,1);
	END
END !!
SET TERM ; !!
CREATE GENERATOR reference_num_gen;
SET GENERATOR reference_num_gen TO 13487;
SET TERM !! ;
CREATE OR ALTER TRIGGER reference_num_trig FOR transactions BEFORE INSERT AS
BEGIN
	if(new.reference_num is null) then
	BEGIN
		new.reference_num=GEN_ID(reference_num_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE user_group(
user_id INTEGER NOT NULL REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE,
group_id INTEGER NOT NULL REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(user_id,group_id));

CREATE TABLE resources(
resource_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(60) NOT NULL,
resource_type INTEGER NOT NULL,
UNIQUE(name,resource_type));
CREATE GENERATOR resources_gen;
SET GENERATOR resources_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER resources_trig FOR resources BEFORE INSERT AS
BEGIN
    if(new.resource_id is null) then
    BEGIN
		new.resource_id=GEN_ID(resources_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE rights(
right_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(60) NOT NULL UNIQUE);
CREATE GENERATOR rights_gen;
SET GENERATOR rights_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER rights_trig FOR rights BEFORE INSERT AS
BEGIN
    if(new.right_id is null) then
    BEGIN
		new.right_id=GEN_ID(rights_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE security(
group_id INTEGER NOT NULL REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE, 
resource_id INTEGER NOT NULL REFERENCES resources(resource_id) ON UPDATE CASCADE ON DELETE CASCADE,
right_id  INTEGER NOT NULL REFERENCES rights(right_id) ON UPDATE CASCADE ON DELETE CASCADE,
exp_date TIMESTAMP,
UNIQUE(group_id,resource_id,right_id));

CREATE TABLE orders(
transaction_id INTEGER NOT NULL REFERENCES transactions(transaction_id) ON UPDATE CASCADE ON DELETE CASCADE,
product_table_id INTEGER NOT NULL REFERENCES product_tables(product_table_id) ON UPDATE CASCADE ON DELETE CASCADE,
product_id INTEGER NOT NULL,
quantity INTEGER NOT NULL,
price FLOAT NOT NULL);

CREATE TABLE merchant_product_tables(
merchant_id VARCHAR(50) NOT NULL REFERENCES merchants(merchant_id) ON UPDATE CASCADE ON DELETE CASCADE,
product_table_id INTEGER NOT NULL REFERENCES product_tables(product_table_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(merchant_id,product_table_id));

CREATE TABLE log_items(
item_id INTEGER NOT NULL PRIMARY KEY,
date_column TIMESTAMP NOT NULL);

CREATE GENERATOR log_items_gen;
SET GENERATOR log_items_gen TO 1;
SET TERM !! ;
CREATE or ALTER TRIGGER log_items_insert FOR log_items BEFORE INSERT AS
BEGIN
    if (new.item_id is null) then
    BEGIN
		new.item_id=GEN_ID(log_items_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE log_names(
category_id INTEGER NOT NULL PRIMARY KEY,
val VARCHAR(50) NOT NULL UNIQUE);

CREATE GENERATOR log_names_gen;
SET GENERATOR log_names_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER log_names_insert FOR log_names BEFORE INSERT AS
BEGIN
	if(new.category_id is null) then
	BEGIN
		new.category_id=GEN_ID(log_names_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE log_values(
value_id INTEGER NOT NULL PRIMARY KEY,
val VARCHAR(252) NOT NULL UNIQUE);

CREATE GENERATOR log_values_gen;
SET GENERATOR log_values_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER log_values_insert FOR log_values BEFORE INSERT AS
BEGIN
	if(new.value_id is null) then
	BEGIN
		new.value_id=GEN_ID(log_values_gen,1);
	END
END !!
SET TERM ; !!

CREATE TABLE log(
item_id INTEGER NOT NULL REFERENCES log_items(item_id) ON UPDATE CASCADE ON DELETE CASCADE,
category_id INTEGER NOT NULL REFERENCES log_names(category_id) ON UPDATE CASCADE ON DELETE CASCADE,
value_id INTEGER NOT NULL REFERENCES log_values(value_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(item_id,category_id,value_id));

CREATE TABLE user_att(
att_id INTEGER NOT NULL PRIMARY KEY,
name CHAR(100) NOT NULL,
att_type INTEGER NOT NULL,
UNIQUE(name,att_type));

CREATE GENERATOR user_att_gen;
SET GENERATOR user_att_gen to 1;
SET TERM !!;
CREATE OR ALTER TRIGGER user_att_trig FOR user_att BEFORE INSERT AS
BEGIN
	if(new.att_id is null) then
	BEGIN
		new.att_id=GEN_ID(user_att_gen,1);
	END
END !!
SET TERM ;!! 

CREATE TABLE user_values(
value_id INTEGER NOT NULL PRIMARY KEY,
val_col CHAR(100) NOT NULL UNIQUE);

CREATE GENERATOR user_values_gen;
SET GENERATOR user_values_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER user_values_trig FOR user_values BEFORE INSERT AS
BEGIN
	if(new.value_id is null) then
	BEGIN
		new.value_id=GEN_ID(user_values_gen,1);
	END
END!!
SET TERM ;!!

CREATE TABLE user_long_values(
long_value_id INTEGER NOT NULL PRIMARY KEY,
long_value VARCHAR(32000) NOT NULL);

CREATE GENERATOR user_long_values_gen;
SET GENERATOR user_long_values_gen to 1;
SET TERM !!;
CREATE OR ALTER TRIGGER user_long_values_trig FOR user_long_values BEFORE INSERT AS
BEGIN
	if(new.long_value_id is null) then
	BEGIN
		new.long_value_id=GEN_ID(user_long_values_gen,1);
	END
END !!
SET TERM ;!!

CREATE TABLE user_data (
user_id INTEGER NOT NULL,
att_id INTEGER NOT NULL,
value_id INTEGER NOT NULL,
UNIQUE(user_id,att_id,value_id));

CREATE TABLE user_long_data (
user_id INTEGER NOT NULL,
att_id INTEGER NOT NULL,
long_value_id INTEGER NOT NULL,
UNIQUE(user_id,att_id,long_value_id));

CREATE TABLE address (
address_id INTEGER NOT NULL PRIMARY KEY,
company_id INTEGER REFERENCES companies(company_id) ON UPDATE CASCADE ON DELETE CASCADE,
attn varchar(64),
address_1 varchar(64) NOT NULL,
address_2 varchar(64),
city_id INTEGER NOT NULL REFERENCES cities(city_id) ON UPDATE CASCADE ON DELETE CASCADE,
state_id INTEGER NOT NULL REFERENCES states(state_id) ON UPDATE CASCADE ON DELETE CASCADE, 
country_id INTEGER NOT NULL REFERENCES countries(country_id) ON UPDATE CASCADE ON DELETE CASCADE,
phone varchar(32),
fax varchar(32),
zip varchar(16));

CREATE GENERATOR address_gen;
SET GENERATOR address_gen to 1;
SET TERM !!;
CREATE OR ALTER TRIGGER address_trig FOR address BEFORE INSERT AS
BEGIN
	if(new.address_id IS NULL) then
	BEGIN
		new.address_id=GEN_ID(address_gen,1);
	END
END !!
SET TERM ;!!

CREATE TABLE user_requests (
request_id INTEGER NOT NULL PRIMARY KEY,
username varchar(35),
last_name varchar(30),
first_name varchar(30),
email varchar(80),
company VARCHAR(50),
phone VARCHAR(20),
reason VARCHAR(32765),
address1 VARCHAR(50),
address2 VARCHAR(50),
city VARCHAR(50),
state VARCHAR(50),
country VARCHAR(50),
zip VARCHAR(12),
resource_id INTEGER REFERENCES resources(resource_id) ON UPDATE CASCADE ON DELETE CASCADE);
grant all on user_requests to damuser;

CREATE GENERATOR user_requests_gen;
SET GENERATOR user_requests_gen to 1;
SET TERM !!;
CREATE OR ALTER TRIGGER user_requests_trig FOR user_requests BEFORE INSERT AS
BEGIN
	if(new.request_id IS NULL) then
	BEGIN
		new.request_id=GEN_ID(user_requests_gen,1);
	END
END !!
SET TERM ;!!


EXIT;


