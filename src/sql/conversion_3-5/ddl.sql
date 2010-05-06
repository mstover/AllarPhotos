set term !!;

CREATE OR ALTER PROCEDURE findDownloadItemsDateRange (category_name VARCHAR(50),category_value VARCHAR(252),fromDate VARCHAR(32),toDate VARCHAR(32))
returns (item_id INTEGER,date_column TIMESTAMP, category VARCHAR(50),val VARCHAR(252))
AS
declare tempOrderNo VARCHAR(252);
declare tempId INTEGER;
BEGIN
for execute statement 'select li.item_id,li.date_column,cat.val,val.val from log_items li inner join log on li.item_id=log.item_id ' || 
		'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where CAST(li.date_column as DATE)>=CAST(''' || :fromDate || ''' as DATE) and ' ||
		'CAST(li.date_column as DATE)<CAST(''' || :toDate || ''' as DATE) and ' ||
		'cat.val=''action'' and val.val=''download'' and exists (' ||
		'select log.item_id from log inner join log_names ln on log.category_id=ln.category_id ' ||
		'inner join log_values lv on log.value_id=lv.value_id where log.item_id=li.item_id and ln.val=''' || :category_name || 
		''' and lv.val=''' || :category_value || ''')' INTO :item_id,:date_column,:category,:val DO
   BEGIN
   	tempOrderNo = :val;
   	tempId = :item_id;
   	SUSPEND;
        for execute statement 'SELECT ' || :item_id || ', CAST(null as TIMESTAMP),cat.val,val.val from log ' ||
        	'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where log.item_id=' || :item_id INTO :item_id,:date_column,:category,:val DO
                BEGIN
                	SUSPEND;
                END
			
   END
END !!


set term ; !!

grant execute on procedure findDownloadItemsDateRange to damuser;

set term !!;

CREATE OR ALTER PROCEDURE findDownloadItems (category_name VARCHAR(50),category_value VARCHAR(252),dateCol VARCHAR(32))
returns (item_id INTEGER,date_column TIMESTAMP, category VARCHAR(50),val VARCHAR(252))
AS
declare tempOrderNo VARCHAR(252);
declare tempId INTEGER;
BEGIN
for execute statement 'select li.item_id,li.date_column,cat.val,val.val from log_items li inner join log on li.item_id=log.item_id ' || 
		'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where CAST(li.date_column as DATE)>CAST(''' || :dateCol || ''' as DATE) and ' ||
		'cat.val=''action'' and val.val=''download'' and exists (' ||
		'select log.item_id from log inner join log_names ln on log.category_id=ln.category_id ' ||
		'inner join log_values lv on log.value_id=lv.value_id where log.item_id=li.item_id and ln.val=''' || :category_name || 
		''' and lv.val=''' || :category_value || ''')' INTO :item_id,:date_column,:category,:val DO
   BEGIN
   	tempOrderNo = :val;
   	tempId = :item_id;
   	SUSPEND;
        for execute statement 'SELECT ' || :item_id || ', CAST(null as TIMESTAMP),cat.val,val.val from log ' ||
        	'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where log.item_id=' || :item_id INTO :item_id,:date_column,:category,:val DO
                BEGIN
                	SUSPEND;
                END
			
   END
END !!


set term ; !!

grant execute on procedure findDownloadItems to damuser;

set term !!;

CREATE OR ALTER PROCEDURE findOrderItemsDateRange (category_name VARCHAR(50),category_value VARCHAR(252),fromDate VARCHAR(32),toDate VARCHAR(32))
returns (item_id INTEGER,date_column TIMESTAMP, category VARCHAR(50),val VARCHAR(252))
AS
declare tempOrderNo VARCHAR(252);
declare tempId INTEGER;
BEGIN
for execute statement 'select li.item_id,li.date_column,cat.val,val.val from log_items li inner join log on li.item_id=log.item_id ' || 
		'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where CAST(li.date_column as DATE)>=CAST(''' || :fromDate || ''' as DATE) and ' ||
		'CAST(li.date_column as DATE)<CAST(''' || :toDate || ''' as DATE) and ' ||
		'cat.val=''orderNo'' and exists (' ||
		'select log.item_id from log inner join log_names ln on log.category_id=ln.category_id ' ||
		'inner join log_values lv on log.value_id=lv.value_id where log.item_id=li.item_id and ln.val=''' || :category_name || 
		''' and lv.val=''' || :category_value || ''')' INTO :item_id,:date_column,:category,:val DO
   BEGIN
   	tempOrderNo = :val;
   	tempId = :item_id;
   	SUSPEND;
        for execute statement 'SELECT ' || :item_id || ', CAST(null as TIMESTAMP),cat.val,val.val from log ' ||
        	'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where log.item_id=' || :item_id INTO :item_id,:date_column,:category,:val DO
                BEGIN
                	SUSPEND;
                END
        if(category_name != 'orderNo') then
        BEGIN
        	for execute statement 'select li.item_id,li.date_column,cat.val,val.val from log_items li inner join log on li.item_id=log.item_id ' || 
			'inner join log_names cat on log.category_id=cat.category_id ' ||
			'inner join log_values val on log.value_id=val.value_id ' ||
			'where cat.val=''orderNo'' and val.val=''' || :tempOrderNo || ''' and li.item_id <> ' || :tempId
			 INTO :item_id,:date_column,:category,:val DO
			BEGIN
				SUSPEND;
        			for execute statement 'SELECT ' || :item_id || ', CAST(null as TIMESTAMP),cat.val,val.val from log ' ||
        				'inner join log_names cat on log.category_id=cat.category_id ' ||
					'inner join log_values val on log.value_id=val.value_id ' ||
					'where log.item_id=' || :item_id INTO :item_id,:date_column,:category,:val DO
                			BEGIN
                				SUSPEND;
                			END
                	END
         END
			
   END
END !!


set term ; !!

grant execute on procedure findOrderItemsDateRange to damuser;

set term !!;

CREATE OR ALTER PROCEDURE findOrderItems (category_name VARCHAR(50),category_value VARCHAR(252),dateCol VARCHAR(32))
returns (item_id INTEGER,date_column TIMESTAMP, category VARCHAR(50),val VARCHAR(252))
AS
declare tempOrderNo VARCHAR(252);
declare tempId INTEGER;
BEGIN
for execute statement 'select li.item_id,li.date_column,cat.val,val.val from log_items li inner join log on li.item_id=log.item_id ' || 
		'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where CAST(li.date_column as DATE)>CAST(''' || :dateCol || ''' as DATE) and ' ||
		'cat.val=''orderNo'' and exists (' ||
		'select log.item_id from log inner join log_names ln on log.category_id=ln.category_id ' ||
		'inner join log_values lv on log.value_id=lv.value_id where log.item_id=li.item_id and ln.val=''' || :category_name || 
		''' and lv.val=''' || :category_value || ''')' INTO :item_id,:date_column,:category,:val DO
   BEGIN
   	tempOrderNo = :val;
   	tempId = :item_id;
   	SUSPEND;
        for execute statement 'SELECT ' || :item_id || ', CAST(null as TIMESTAMP),cat.val,val.val from log ' ||
        	'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where log.item_id=' || :item_id INTO :item_id,:date_column,:category,:val DO
                BEGIN
                	SUSPEND;
                END
        if(category_name != 'orderNo') then
        BEGIN
        	for execute statement 'select li.item_id,li.date_column,cat.val,val.val from log_items li inner join log on li.item_id=log.item_id ' || 
			'inner join log_names cat on log.category_id=cat.category_id ' ||
			'inner join log_values val on log.value_id=val.value_id ' ||
			'where cat.val=''orderNo'' and val.val=''' || :tempOrderNo || ''' and li.item_id <> ' || :tempId
			 INTO :item_id,:date_column,:category,:val DO
			BEGIN
				SUSPEND;
        			for execute statement 'SELECT ' || :item_id || ', CAST(null as TIMESTAMP),cat.val,val.val from log ' ||
        				'inner join log_names cat on log.category_id=cat.category_id ' ||
					'inner join log_values val on log.value_id=val.value_id ' ||
					'where log.item_id=' || :item_id INTO :item_id,:date_column,:category,:val DO
                			BEGIN
                				SUSPEND;
                			END
                	END
         END
			
   END
END !!


set term ; !!

grant execute on procedure findOrderItems to damuser;

set term !!;

CREATE OR ALTER PROCEDURE findAllProducts (table_name VARCHAR(32))
returns (prod_id INTEGER,family varchar(32), primary_name VARCHAR(50),product_path VARCHAR(1024),name VARCHAR(32), val VARCHAR(32765),num_field VARCHAR(32),num_val FLOAT, date_cataloged TIMESTAMP)
AS
declare variable temp_prod_id INTEGER;
BEGIN
for execute statement 'select product_id from ' || :table_name INTO :temp_prod_id DO
   BEGIN
        execute statement 'SELECT product_id,CAST(''' || :table_name || ''' AS varchar(32)),primary_label, paths.path, CAST(null as varchar(32)) label,CAST(null as varchar(32765)),CAST(null as varchar(32)),CAST(null as FLOAT),DATE_CATALOGED FROM ' || :table_name || ' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  where product_id=' || :temp_prod_id
                INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged;
        SUSPEND;
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)),CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,CAST(k.keyword as varchar(32765)),CAST(null as varchar(32)),CAST(null as FLOAT),CAST(null AS TIMESTAMP) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                  :table_name || '_key k on c.keyword_id=k.keyword_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)), CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,k.description,CAST(null as varchar(32)),CAST(null as FLOAT),CAST(null AS TIMESTAMP) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                 :table_name || '_descript k on c.description_id=k.description_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged DO
                 BEGIN
                  SUSPEND;
                 END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)),CAST(null as varchar(50)),CAST(null as varchar(1024)), CAST(null as varchar(32)),CAST(null as varchar(32765)),f.name,c.val_col,CAST(null AS TIMESTAMP) from ' || :table_name || 'stats c inner join ' || :table_name || 
                    'fields f on c.field_id=f.field_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged DO
                  BEGIN
                  SUSPEND;
                  END
   END
END !!


set term ; !!

grant execute on procedure findAllProducts to damuser;
set term !! ;

CREATE OR ALTER PROCEDURE findProducts (table_name VARCHAR(32),field_name VARCHAR(32),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_key k on c.keyword_id=k.keyword_id where f.name=''' || :field_name || ''' and k.keyword=''' || :keyword_value || ''''
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END!!

set term ;!!

grant execute on procedure findProducts to damuser;
set term !!;

CREATE OR ALTER PROCEDURE findProductsByDate (table_name VARCHAR(32),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || ' where CAST(date_cataloged as DATE)>CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByDate to damuser;
set term !!;

CREATE OR ALTER PROCEDURE findProductsByDescription (table_name VARCHAR(32),field_name VARCHAR(32),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_descript k on c.description_id=k.description_id where f.name=''' || :field_name || ''' and k.description LIKE ''%' || :keyword_value || '%'''
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByDescription to damuser;
set term !!;

CREATE OR ALTER PROCEDURE findProductsById (table_name VARCHAR(32),id INTEGER)
returns (prod_id INTEGER,family varchar(32), primary_name VARCHAR(50),product_path VARCHAR(1024),name VARCHAR(32), val VARCHAR(32765),num_field VARCHAR(32),num_val FLOAT, date_cataloged TIMESTAMP)
AS
declare variable temp_prod_id INTEGER;
BEGIN
execute statement 'SELECT product_id,CAST(''' || :table_name || ''' AS varchar(32)),primary_label, paths.path, CAST(null as varchar(32)) label,CAST(null as varchar(32765)),CAST(null as varchar(32)),CAST(null as FLOAT),DATE_CATALOGED FROM ' || :table_name || ' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  where product_id =' || id
                INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged;
        SUSPEND;
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)),CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,CAST(k.keyword as varchar(32765)),CAST(null as varchar(32)),CAST(null as FLOAT),CAST(null AS TIMESTAMP) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                  :table_name || '_key k on c.keyword_id=k.keyword_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)), CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,k.description,CAST(null as varchar(32)),CAST(null as FLOAT),CAST(null AS TIMESTAMP) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                 :table_name || '_descript k on c.description_id=k.description_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged DO
                 BEGIN
                  SUSPEND;
                 END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)),CAST(null as varchar(50)),CAST(null as varchar(1024)), CAST(null as varchar(32)),CAST(null as varchar(32765)),f.name,c.val_col,CAST(null AS TIMESTAMP) from ' || :table_name || 'stats c inner join ' || :table_name || 
                    'fields f on c.field_id=f.field_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged DO
                  BEGIN
                  SUSPEND;
                  END
END !!


set term ; !!

grant execute on procedure findProductsById to damuser;
set term !!;

CREATE OR ALTER PROCEDURE findProductsByNumber (table_name VARCHAR(32),field_name VARCHAR(32),keyword_value FLOAT)
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || 'stats c inner join ' || :table_name || 'fields f on c.field_id=f.field_id where f.name=''' || :field_name || ''' and c.val_col=' || :keyword_value
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByNumber to damuser;
set term !!;

CREATE OR ALTER PROCEDURE findProductsByPrimary (table_name VARCHAR(32),primary_value VARCHAR(50))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'SELECT product_id, CAST(''' || :table_name || ''' AS varchar(32)) FROM ' || :table_name || ' where primary_label=''' || :primary_value || ''''
                INTO :prod_id,:family DO
        BEGIN
        	SUSPEND;
        END
END !!


set term ; !!

grant execute on procedure findProductsByPrimary to damuser;
set term !!;

CREATE OR ALTER PROCEDURE findProductsByPrimaryPath (table_name VARCHAR(32),name VARCHAR(50),path VARCHAR(1024))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
if(path is not null) then
	BEGIN
	for execute statement 'SELECT product_id, CAST(''' || :table_name || ''' AS varchar(32)) FROM ' || :table_name || 
		' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  ' ||
		'where primary_label like ''' || :name || '.%'' and paths.path=''' || :path || ''''
                INTO :prod_id,:family DO
        	BEGIN
        		SUSPEND;
        	END
	END
ELSE
	BEGIN
	for execute statement 'SELECT product_id, CAST(''' || :table_name || ''' AS varchar(32)) FROM ' || :table_name || 
		' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  ' ||
		'where primary_label like ''' || :name || '.%'''
                INTO :prod_id,:family DO
        	BEGIN
        		SUSPEND;
        	END
	END
END !!


set term ; !!

grant execute on procedure findProductsByPrimaryPath to damuser;

set term !!;

CREATE OR ALTER PROCEDURE findUsers (id INTEGER,in_username VARCHAR(35))
returns (user_id INTEGER, 
         bill_address1 varchar(50),
        bill_address2 varchar(50),
        bill_zip varchar(12),
        bill_city_id INTEGER,
        bill_state_id INTEGER,
        bill_country_id INTEGER,
        ship_address1 varchar(50),
        ship_address2 varchar(50),
        ship_zip varchar(12),
        ship_city_id INTEGER,
        ship_state_id INTEGER,
        ship_country_id INTEGER,
        phone varchar(25),
        email varchar(80),
        fax varchar(25),
        first_name varchar(30),
        last_name varchar(30),
        middle_initial varchar(5),
        passwd varchar(80),
        username varchar(35),
        exp_date TIMESTAMP,
        referrer_id INTEGER,
        company_id INTEGER,
        key_name char(100),
        short_value char(100),
        long_value varchar(32000))
AS
BEGIN
	if(id is not null) then
		BEGIN
			for execute statement 'select user_id,bill_address1,bill_address2,bill_zip,bill_city_id,bill_state_id,bill_country_id,' ||
		                      'ship_address1,ship_address2,ship_zip,ship_city_id,ship_state_id,ship_country_id,phone,email,' ||
		                      'fax,first_name,last_name,middle_initial,passwd,username,exp_date,referrer_id,company_id,' ||
		                      'CAST(null as char(100)),'  ||
		                      'CAST(null as char(100)),CAST(null as varchar(32000)) from users where user_id=' || :id
		          INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id, 
		        		:key_name, :short_value, :long_value DO
		   BEGIN
		        SUSPEND;
		        execute procedure getUserProperties(:user_id) returning_values(:user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
		        		:key_name, :short_value, :long_value); 
		        SUSPEND;
		   END
		END
	ELSE
		BEGIN
			for execute statement 'select user_id,bill_address1,bill_address2,bill_zip,bill_city_id,bill_state_id,bill_country_id,' ||
		                      'ship_address1,ship_address2,ship_zip,ship_city_id,ship_state_id,ship_country_id,phone,email,' ||
		                      'fax,first_name,last_name,middle_initial,passwd,username,exp_date,referrer_id,company_id,' ||
		                      'CAST(null as char(100)),'  ||
		                      'CAST(null as char(100)),CAST(null as varchar(32000)) from users where username=''' || :in_username || ''''
		          INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
		        		:key_name, :short_value, :long_value DO
		   BEGIN
		        SUSPEND;
		        execute procedure getUserProperties(:user_id) returning_values(:user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
		        		:key_name, :short_value, :long_value); 
		        SUSPEND;
		   END
		END
END !!


set term ; !!

grant execute on procedure findUsers to damuser;
set term !!;

CREATE OR ALTER PROCEDURE findUsersByGroup (group_id INTEGER)
returns (user_id INTEGER, 
         bill_address1 varchar(50),
        bill_address2 varchar(50),
        bill_zip varchar(12),
        bill_city_id INTEGER,
        bill_state_id INTEGER,
        bill_country_id INTEGER,
        ship_address1 varchar(50),
        ship_address2 varchar(50),
        ship_zip varchar(12),
        ship_city_id INTEGER,
        ship_state_id INTEGER,
        ship_country_id INTEGER,
        phone varchar(25),
        email varchar(80),
        fax varchar(25),
        first_name varchar(30),
        last_name varchar(30),
        middle_initial varchar(5),
        passwd varchar(80),
        username varchar(35),
        exp_date TIMESTAMP,
        referrer_id INTEGER,
        company_id INTEGER,
        key_name char(100),
        short_value char(100),
        long_value varchar(32000))
AS
declare id INTEGER;
BEGIN
	for execute statement 'select users.user_id,bill_address1,bill_address2,bill_zip,bill_city_id,bill_state_id,bill_country_id,' ||
		                      'ship_address1,ship_address2,ship_zip,ship_city_id,ship_state_id,ship_country_id,phone,email,' ||
		                      'fax,first_name,last_name,middle_initial,passwd,username,exp_date,referrer_id,company_id,' ||
		                      'CAST(null as char(100)),'  ||
		                      'CAST(null as char(100)),CAST(null as varchar(32000)) from users inner join user_group on users.user_id=user_group.user_id ' ||
		                      'where group_id=' || :group_id
		          INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
		        		:key_name, :short_value, :long_value DO
		   BEGIN
		        SUSPEND;
		        id = user_id;
		        for execute statement 'select user_id,CAST(null as varchar(50)),CAST(null as varchar(50)),CAST(null as varchar(12)),' ||
        		'CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as varchar(50)),' ||
        		'CAST(null as varchar(50)),CAST(null as varchar(12)),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'CAST(null as INTEGER),CAST(null as varchar(25)),CAST(null as varchar(80)),CAST(null as varchar(25)),' ||
        		'CAST(null as varchar(30)),CAST(null as varchar(30)),CAST(null as varchar(5)),CAST(null as varchar(80)),' ||
        		'CAST(null as varchar(35)),CAST(null as TIMESTAMP),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'user_att.name,user_values.val_col,' ||
        		'CAST(null as varchar(32000)) from user_data inner join user_att on user_data.att_id=user_att.att_id ' ||
        			'inner join user_values on user_data.value_id=user_values.value_id where user_data.user_id=' || :id
        			INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
        		:key_name, :short_value, :long_value DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select user_id,CAST(null as varchar(50)),CAST(null as varchar(50)),CAST(null as varchar(12)),' ||
        		'CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as varchar(50)),' ||
        		'CAST(null as varchar(50)),CAST(null as varchar(12)),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'CAST(null as INTEGER),CAST(null as varchar(25)),CAST(null as varchar(80)),CAST(null as varchar(25)),' ||
        		'CAST(null as varchar(30)),CAST(null as varchar(30)),CAST(null as varchar(5)),CAST(null as varchar(80)),' ||
        		'CAST(null as varchar(35)),CAST(null as TIMESTAMP),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'user_att.name,CAST(NULL as char(100)),' ||
                'user_long_values.long_value from user_long_data inner join user_att on user_long_data.att_id=user_att.att_id ' ||
        			'inner join user_long_values on user_long_data.long_value_id=user_long_values.long_value_id where user_long_data.user_id=' || :id
        			INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
        		 :key_name, :short_value, :long_value DO
                  BEGIN
                  SUSPEND;
                  END
		   END
END !!


set term ; !!

grant execute on procedure findUsersByGroup to damuser;
set term !!;

CREATE OR ALTER PROCEDURE getUserProperties (id INTEGER)
returns (user_id INTEGER, 
         bill_address1 varchar(50),
        bill_address2 varchar(50),
        bill_zip varchar(12),
        bill_city_id INTEGER,
        bill_state_id INTEGER,
        bill_country_id INTEGER,
        ship_address1 varchar(50),
        ship_address2 varchar(50),
        ship_zip varchar(12),
        ship_city_id INTEGER,
        ship_state_id INTEGER,
        ship_country_id INTEGER,
        phone varchar(25),
        email varchar(80),
        fax varchar(25),
        first_name varchar(30),
        last_name varchar(30),
        middle_initial varchar(5),
        passwd varchar(80),
        username varchar(35),
        exp_date TIMESTAMP,
        referrer_id INTEGER,
        company_id INTEGER,
        key_name char(100),
        short_value char(100),
        long_value varchar(32000))
AS
BEGIN
	for execute statement 'select user_id,CAST(null as varchar(50)),CAST(null as varchar(50)),CAST(null as varchar(12)),' ||
        		'CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as varchar(50)),' ||
        		'CAST(null as varchar(50)),CAST(null as varchar(12)),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'CAST(null as INTEGER),CAST(null as varchar(25)),CAST(null as varchar(80)),CAST(null as varchar(25)),' ||
        		'CAST(null as varchar(30)),CAST(null as varchar(30)),CAST(null as varchar(5)),CAST(null as varchar(80)),' ||
        		'CAST(null as varchar(35)),CAST(null as TIMESTAMP),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'user_att.name,user_values.val_col,' ||
        		'CAST(null as varchar(32000)) from user_data inner join user_att on user_data.att_id=user_att.att_id ' ||
        			'inner join user_values on user_data.value_id=user_values.value_id where user_data.user_id=' || :id
        			INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
        		:key_name, :short_value, :long_value DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select user_id,CAST(null as varchar(50)),CAST(null as varchar(50)),CAST(null as varchar(12)),' ||
        		'CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as varchar(50)),' ||
        		'CAST(null as varchar(50)),CAST(null as varchar(12)),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'CAST(null as INTEGER),CAST(null as varchar(25)),CAST(null as varchar(80)),CAST(null as varchar(25)),' ||
        		'CAST(null as varchar(30)),CAST(null as varchar(30)),CAST(null as varchar(5)),CAST(null as varchar(80)),' ||
        		'CAST(null as varchar(35)),CAST(null as TIMESTAMP),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'user_att.name,CAST(NULL as char(100)),' ||
                'user_long_values.long_value from user_long_data inner join user_att on user_long_data.att_id=user_att.att_id ' ||
        			'inner join user_long_values on user_long_data.long_value_id=user_long_values.long_value_id where user_long_data.user_id=' || :id
        			INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
        		 :key_name, :short_value, :long_value DO
                  BEGIN
                  SUSPEND;
                  END
END !!


set term ; !!

grant execute on procedure getUserProperties to damuser;
set term !!;

CREATE OR ALTER PROCEDURE searchProducts (table_name VARCHAR(32),search_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || 'description c inner join ' || :table_name || '_descript k on c.description_id=k.description_id where UPPER(k.description) LIKE UPPER(''%' || :search_value || '%'')' ||
          'union select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || 'category c inner join ' || :table_name || '_key k on c.keyword_id=k.keyword_id where UPPER(k.keyword) LIKE UPPER(''%' || :search_value || '%'')' ||
          'union SELECT product_id, CAST(''' || :table_name || ''' AS varchar(32)) FROM ' || :table_name || ' where UPPER(primary_label) LIKE UPPER(''%' || :search_value || '%'')'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure searchProducts to damuser;
set term !!;

CREATE OR ALTER PROCEDURE searchUsers (search_value VARCHAR(32))
returns (user_id INTEGER)
AS
BEGIN
	for execute statement 'select users.user_id from users where UPPER(username) like UPPER(''%' || :search_value || '%'') or ' ||
	                      'UPPER(last_name) like UPPER(''%' || :search_value || '%'') or UPPER(first_name) like ' ||
	                      'UPPER(''%' || :search_value || '%'') or UPPER(email) like UPPER(''%' || :search_value || '%'') ' ||
	                      'union select user_id from user_data inner join user_values on user_data.value_id=user_values.value_id ' ||
	                      'where UPPER(user_values.val_col) like UPPER(''%' || :search_value || '%'')' into :user_id DO                          
		   BEGIN
		        SUSPEND;
		   END
END !!


set term ; !!

grant execute on procedure searchUsers to damuser;

CREATE TABLE abi_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR abi_paths_gen;
SET GENERATOR abi_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER abi_paths_trig FOR abi_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(abi_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on abi_paths to damuser;

insert into abi_paths(path) values('');

COMMIT;

ALTER TABLE abi add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES abi_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE abi add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update abi set primary_label2=primary_label;
ALTER TABLE abi drop primary_label;
ALTER TABLE abi alter primary_label2 TO primary_label;
ALTER TABLE abi add CONSTRAINT Unique_Image_abi UNIQUE(primary_label,path_id);

COMMIT;

insert into abifields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

CREATE TABLE ia_bali_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR ia_bali_paths_gen;
SET GENERATOR ia_bali_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER ia_bali_paths_trig FOR ia_bali_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_bali_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on ia_bali_paths to damuser;

insert into ia_bali_paths(path) values('');

COMMIT;

ALTER TABLE ia_bali add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES ia_bali_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ia_bali add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update ia_bali set primary_label2=primary_label;
ALTER TABLE ia_bali drop primary_label;
ALTER TABLE ia_bali alter primary_label2 TO primary_label;
ALTER TABLE ia_bali add CONSTRAINT Unique_Image_ia_bali UNIQUE(primary_label,path_id);

COMMIT;

insert into ia_balifields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

CREATE TABLE ia_bt_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR ia_bt_paths_gen;
SET GENERATOR ia_bt_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER ia_bt_paths_trig FOR ia_bt_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_bt_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on ia_bt_paths to damuser;

insert into ia_bt_paths(path) values('');

COMMIT;

ALTER TABLE ia_bt add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES ia_bt_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ia_bt add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update ia_bt set primary_label2=primary_label;
ALTER TABLE ia_bt drop primary_label;
ALTER TABLE ia_bt alter primary_label2 TO primary_label;
ALTER TABLE ia_bt add CONSTRAINT Unique_Image_ia_bt UNIQUE(primary_label,path_id);

COMMIT;

insert into ia_btfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

CREATE TABLE ia_hanes_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR ia_hanes_paths_gen;
SET GENERATOR ia_hanes_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER ia_hanes_paths_trig FOR ia_hanes_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_hanes_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on ia_hanes_paths to damuser;

insert into ia_hanes_paths(path) values('');

COMMIT;

ALTER TABLE ia_hanes add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES ia_hanes_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ia_hanes add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update ia_hanes set primary_label2=primary_label;
ALTER TABLE ia_hanes drop primary_label;
ALTER TABLE ia_hanes alter primary_label2 TO primary_label;
ALTER TABLE ia_hanes add CONSTRAINT Unique_Image_ia_hanes UNIQUE(primary_label,path_id);

COMMIT;

insert into ia_hanesfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

CREATE TABLE ia_jms_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR ia_jms_paths_gen;
SET GENERATOR ia_jms_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER ia_jms_paths_trig FOR ia_jms_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_jms_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on ia_jms_paths to damuser;

insert into ia_jms_paths(path) values('');

COMMIT;

ALTER TABLE ia_jms add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES ia_jms_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ia_jms add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update ia_jms set primary_label2=primary_label;
ALTER TABLE ia_jms drop primary_label;
ALTER TABLE ia_jms alter primary_label2 TO primary_label;
ALTER TABLE ia_jms add CONSTRAINT Unique_Image_ia_jms UNIQUE(primary_label,path_id);

COMMIT;

insert into ia_jmsfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

CREATE TABLE ia_plytx_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR ia_plytx_paths_gen;
SET GENERATOR ia_plytx_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER ia_plytx_paths_trig FOR ia_plytx_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_plytx_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on ia_plytx_paths to damuser;

insert into ia_plytx_paths(path) values('');

COMMIT;

ALTER TABLE ia_plytx add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES ia_plytx_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ia_plytx add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update ia_plytx set primary_label2=primary_label;
ALTER TABLE ia_plytx drop primary_label;
ALTER TABLE ia_plytx alter primary_label2 TO primary_label;
ALTER TABLE ia_plytx add CONSTRAINT Unique_Image_ia_plytx UNIQUE(primary_label,path_id);

COMMIT;

insert into ia_plytxfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

CREATE TABLE ia_wndbr_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR ia_wndbr_paths_gen;
SET GENERATOR ia_wndbr_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER ia_wndbr_paths_trig FOR ia_wndbr_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_wndbr_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on ia_wndbr_paths to damuser;

insert into ia_wndbr_paths(path) values('');

COMMIT;

ALTER TABLE ia_wndbr add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES ia_wndbr_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ia_wndbr add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update ia_wndbr set primary_label2=primary_label;
ALTER TABLE ia_wndbr drop primary_label;
ALTER TABLE ia_wndbr alter primary_label2 TO primary_label;
ALTER TABLE ia_wndbr add CONSTRAINT Unique_Image_ia_wndbr UNIQUE(primary_label,path_id);

COMMIT;

insert into ia_wndbrfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

CREATE TABLE kdk_dai_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR kdk_dai_paths_gen;
SET GENERATOR kdk_dai_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER kdk_dai_paths_trig FOR kdk_dai_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kdk_dai_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on kdk_dai_paths to damuser;

insert into kdk_dai_paths(path) values('');

COMMIT;

ALTER TABLE kdk_dai add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES kdk_dai_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE kdk_dai add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update kdk_dai set primary_label2=primary_label;
ALTER TABLE kdk_dai drop primary_label;
ALTER TABLE kdk_dai alter primary_label2 TO primary_label;
ALTER TABLE kdk_dai add CONSTRAINT Unique_Image_kdk_dai UNIQUE(primary_label,path_id);

COMMIT;

insert into kdk_daifields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

CREATE TABLE kdk_begs_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR kdk_begs_paths_gen;
SET GENERATOR kdk_begs_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER kdk_begs_paths_trig FOR kdk_begs_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kdk_begs_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on kdk_begs_paths to damuser;

insert into kdk_begs_paths(path) values('');

COMMIT;

ALTER TABLE kdk_begs add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES kdk_begs_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE kdk_begs add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update kdk_begs set primary_label2=primary_label;
ALTER TABLE kdk_begs drop primary_label;
ALTER TABLE kdk_begs alter primary_label2 TO primary_label;
ALTER TABLE kdk_begs add CONSTRAINT Unique_Image_kdk_begs UNIQUE(primary_label,path_id);

insert into kdk_begsfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

COMMIT;

CREATE TABLE kdk_ink_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR kdk_ink_paths_gen;
SET GENERATOR kdk_ink_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER kdk_ink_paths_trig FOR kdk_ink_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kdk_ink_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on kdk_ink_paths to damuser;

insert into kdk_ink_paths(path) values('');

COMMIT;

ALTER TABLE kdk_ink add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES kdk_ink_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE kdk_ink add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update kdk_ink set primary_label2=primary_label;
ALTER TABLE kdk_ink drop primary_label;
ALTER TABLE kdk_ink alter primary_label2 TO primary_label;
ALTER TABLE kdk_ink add CONSTRAINT Unique_Image_kdk_ink UNIQUE(primary_label,path_id);

insert into kdk_inkfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

COMMIT;

CREATE TABLE kdk_sec_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR kdk_sec_paths_gen;
SET GENERATOR kdk_sec_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER kdk_sec_paths_trig FOR kdk_sec_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kdk_sec_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on kdk_sec_paths to damuser;

insert into kdk_sec_paths(path) values('');

COMMIT;

ALTER TABLE kdk_sec add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES kdk_sec_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE kdk_sec add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update kdk_sec set primary_label2=primary_label;
ALTER TABLE kdk_sec drop primary_label;
ALTER TABLE kdk_sec alter primary_label2 TO primary_label;
ALTER TABLE kdk_sec add CONSTRAINT Unique_Image_kdk_sec UNIQUE(primary_label,path_id);

insert into kdk_secfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

COMMIT;

CREATE TABLE kpro_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR kpro_paths_gen;
SET GENERATOR kpro_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER kpro_paths_trig FOR kpro_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kpro_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on kpro_paths to damuser;

insert into kpro_paths(path) values('');

COMMIT;

ALTER TABLE kpro add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES kpro_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE kpro add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update kpro set primary_label2=primary_label;
ALTER TABLE kpro drop primary_label;
ALTER TABLE kpro alter primary_label2 TO primary_label;
ALTER TABLE kpro add CONSTRAINT Unique_Image_kpro UNIQUE(primary_label,path_id);

insert into kprofields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

COMMIT;

CREATE TABLE lw_demo_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR lw_demo_paths_gen;
SET GENERATOR lw_demo_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER lw_demo_paths_trig FOR lw_demo_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(lw_demo_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on lw_demo_paths to damuser;

insert into lw_demo_paths(path) values('');

COMMIT;

ALTER TABLE lw_demo add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES lw_demo_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE lw_demo add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update lw_demo set primary_label2=primary_label;
ALTER TABLE lw_demo drop primary_label;
ALTER TABLE lw_demo alter primary_label2 TO primary_label;
ALTER TABLE lw_demo add CONSTRAINT Unique_Image_lw_demo UNIQUE(primary_label,path_id);

insert into lw_demofields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

COMMIT;

CREATE TABLE welch_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR welch_paths_gen;
SET GENERATOR welch_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER welch_paths_trig FOR welch_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(welch_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on welch_paths to damuser;

insert into welch_paths(path) values('');

COMMIT;

ALTER TABLE welch add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES welch_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE welch add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update welch set primary_label2=primary_label;
ALTER TABLE welch drop primary_label;
ALTER TABLE welch alter primary_label2 TO primary_label;
ALTER TABLE welch add CONSTRAINT Unique_Image_welch UNIQUE(primary_label,path_id);

insert into welchfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

COMMIT;

CREATE TABLE worldkit_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR worldkit_paths_gen;
SET GENERATOR worldkit_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER worldkit_paths_trig FOR worldkit_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(worldkit_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on worldkit_paths to damuser;

insert into worldkit_paths(path) values('');

COMMIT;

ALTER TABLE worldkit add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES worldkit_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE worldkit add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update worldkit set primary_label2=primary_label;
ALTER TABLE worldkit drop primary_label;
ALTER TABLE worldkit alter primary_label2 TO primary_label;
ALTER TABLE worldkit add CONSTRAINT Unique_Image_worldkit UNIQUE(primary_label,path_id);

insert into worldkitfields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

COMMIT;

SET TERM !!;
CREATE OR ALTER TRIGGER ia_balifields_trig FOR ia_balifields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(ia_balifields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_bali_key_trig FOR ia_bali_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(ia_bali_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_bali_descript_trig FOR ia_bali_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(ia_bali_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER ia_bali_paths_trig FOR ia_bali_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_bali_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_bali_trig FOR ia_bali BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(ia_bali_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_baliprice_keys_trig FOR ia_baliprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(ia_baliprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_btfields_trig FOR ia_btfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(ia_btfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_bt_key_trig FOR ia_bt_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(ia_bt_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_bt_descript_trig FOR ia_bt_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(ia_bt_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER ia_bt_paths_trig FOR ia_bt_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_bt_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_bt_trig FOR ia_bt BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(ia_bt_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_btprice_keys_trig FOR ia_btprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(ia_btprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_hanesfields_trig FOR ia_hanesfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(ia_hanesfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_hanes_key_trig FOR ia_hanes_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(ia_hanes_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_hanes_descript_trig FOR ia_hanes_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(ia_hanes_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER ia_hanes_paths_trig FOR ia_hanes_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_hanes_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_hanes_trig FOR ia_hanes BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(ia_hanes_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_hanesprice_keys_trig FOR ia_hanesprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(ia_hanesprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_jmsfields_trig FOR ia_jmsfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(ia_jmsfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_jms_key_trig FOR ia_jms_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(ia_jms_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_jms_descript_trig FOR ia_jms_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(ia_jms_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER ia_jms_paths_trig FOR ia_jms_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_jms_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_jms_trig FOR ia_jms BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(ia_jms_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_jmsprice_keys_trig FOR ia_jmsprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(ia_jmsprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_plytxfields_trig FOR ia_plytxfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(ia_plytxfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_plytx_key_trig FOR ia_plytx_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(ia_plytx_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_plytx_descript_trig FOR ia_plytx_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(ia_plytx_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER ia_plytx_paths_trig FOR ia_plytx_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_plytx_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_plytx_trig FOR ia_plytx BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(ia_plytx_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_plytxprice_keys_trig FOR ia_plytxprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(ia_plytxprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_wndbrfields_trig FOR ia_wndbrfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(ia_wndbrfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_wndbr_key_trig FOR ia_wndbr_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(ia_wndbr_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_wndbr_descript_trig FOR ia_wndbr_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(ia_wndbr_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER ia_wndbr_paths_trig FOR ia_wndbr_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(ia_wndbr_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_wndbr_trig FOR ia_wndbr BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(ia_wndbr_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER ia_wndbrprice_keys_trig FOR ia_wndbrprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(ia_wndbrprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER abifields_trig FOR abifields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(abifields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER abi_key_trig FOR abi_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(abi_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER abi_descript_trig FOR abi_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(abi_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER abi_paths_trig FOR abi_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(abi_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER abi_trig FOR abi BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(abi_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER abiprice_keys_trig FOR abiprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(abiprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER worldkitfields_trig FOR worldkitfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(worldkitfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER worldkit_key_trig FOR worldkit_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(worldkit_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER worldkit_descript_trig FOR worldkit_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(worldkit_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER worldkit_paths_trig FOR worldkit_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(worldkit_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER worldkit_trig FOR worldkit BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(worldkit_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER worldkitprice_keys_trig FOR worldkitprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(worldkitprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_daifields_trig FOR kdk_daifields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(kdk_daifields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_dai_key_trig FOR kdk_dai_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(kdk_dai_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_dai_descript_trig FOR kdk_dai_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(kdk_dai_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER kdk_dai_paths_trig FOR kdk_dai_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kdk_dai_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_dai_trig FOR kdk_dai BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(kdk_dai_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_daiprice_keys_trig FOR kdk_daiprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(kdk_daiprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_begsfields_trig FOR kdk_begsfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(kdk_begsfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_begs_key_trig FOR kdk_begs_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(kdk_begs_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_begs_descript_trig FOR kdk_begs_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(kdk_begs_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER kdk_begs_paths_trig FOR kdk_begs_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kdk_begs_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_begs_trig FOR kdk_begs BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(kdk_begs_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_begsprice_keys_trig FOR kdk_begsprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(kdk_begsprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_inkfields_trig FOR kdk_inkfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(kdk_inkfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_ink_key_trig FOR kdk_ink_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(kdk_ink_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_ink_descript_trig FOR kdk_ink_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(kdk_ink_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER kdk_ink_paths_trig FOR kdk_ink_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kdk_ink_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_ink_trig FOR kdk_ink BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(kdk_ink_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_inkprice_keys_trig FOR kdk_inkprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(kdk_inkprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_secfields_trig FOR kdk_secfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(kdk_secfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_sec_key_trig FOR kdk_sec_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(kdk_sec_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_sec_descript_trig FOR kdk_sec_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(kdk_sec_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER kdk_sec_paths_trig FOR kdk_sec_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kdk_sec_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_sec_trig FOR kdk_sec BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(kdk_sec_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kdk_secprice_keys_trig FOR kdk_secprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(kdk_secprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kprofields_trig FOR kprofields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(kprofields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kpro_key_trig FOR kpro_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(kpro_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kpro_descript_trig FOR kpro_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(kpro_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER kpro_paths_trig FOR kpro_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(kpro_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kpro_trig FOR kpro BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(kpro_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER kproprice_keys_trig FOR kproprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(kproprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER welchfields_trig FOR welchfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(welchfields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER welch_key_trig FOR welch_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(welch_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER welch_descript_trig FOR welch_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(welch_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER welch_paths_trig FOR welch_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(welch_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER welch_trig FOR welch BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(welch_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER welchprice_keys_trig FOR welchprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(welchprice_keys_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER lw_demofields_trig FOR lw_demofields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(lw_demofields_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER lw_demo_key_trig FOR lw_demo_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(lw_demo_key_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER lw_demo_descript_trig FOR lw_demo_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(lw_demo_descript_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !! ;
CREATE OR ALTER TRIGGER lw_demo_paths_trig FOR lw_demo_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(lw_demo_paths_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER lw_demo_trig FOR lw_demo BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(lw_demo_gen,1);
	END
END !!
SET TERM ;!!

SET TERM !!;
CREATE OR ALTER TRIGGER lw_demoprice_keys_trig FOR lw_demoprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(lw_demoprice_keys_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_c9fields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_c9fields_gen;
SET GENERATOR hb_c9fields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_c9fields_trig FOR hb_c9fields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_c9fields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_c9fields to damuser;

commit;

CREATE TABLE hb_c9_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_c9_key_gen;
SET GENERATOR hb_c9_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_c9_key_trig FOR hb_c9_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_c9_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_c9_key to damuser;

commit;

CREATE TABLE hb_c9_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_c9_descript_gen;
SET GENERATOR hb_c9_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_c9_descript_trig FOR hb_c9_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_c9_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_c9_descript to damuser;

commit;

CREATE TABLE hb_c9_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_c9_paths_gen;
SET GENERATOR hb_c9_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_c9_paths_trig FOR hb_c9_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_c9_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_c9_paths to damuser;

COMMIT;

CREATE TABLE hb_c9(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_c9_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_c9_gen;
SET GENERATOR hb_c9_gen TO 1;

grant all on hb_c9 to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_c9_trig FOR hb_c9 BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_c9_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_c9category(
product_id INTEGER NOT NULL REFERENCES hb_c9(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_c9fields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_c9_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_c9category to damuser;

commit;

CREATE TABLE hb_c9description(
product_id INTEGER NOT NULL REFERENCES hb_c9(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_c9fields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_c9_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_c9description to damuser;

commit;

CREATE TABLE hb_c9stats(
product_id INTEGER NOT NULL REFERENCES hb_c9(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_c9fields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_c9stats to damuser;

commit;

CREATE TABLE hb_c9price_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_c9price_keys_gen;
SET GENERATOR hb_c9price_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_c9price_keys_trig FOR hb_c9price_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_c9price_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_c9price_keys to damuser;

commit;

CREATE TABLE hb_c9price(
product_id INTEGER NOT NULL REFERENCES hb_c9(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_c9price_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_c9price to damuser;

commit;

CREATE TABLE hb_c9price_break(
product_id INTEGER NOT NULL REFERENCES hb_c9(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_c9price_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_c9price_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_c9','Champion','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_c9fields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_c9fields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_c9fields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_c9fields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_c9fields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_c9fields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_c9fields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_c9fields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_c9fields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_c9fields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

CREATE TABLE hb_casfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_casfields_gen;
SET GENERATOR hb_casfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_casfields_trig FOR hb_casfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_casfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_casfields to damuser;

commit;

CREATE TABLE hb_cas_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_cas_key_gen;
SET GENERATOR hb_cas_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_cas_key_trig FOR hb_cas_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_cas_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_cas_key to damuser;

commit;

CREATE TABLE hb_cas_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_cas_descript_gen;
SET GENERATOR hb_cas_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_cas_descript_trig FOR hb_cas_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_cas_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_cas_descript to damuser;

commit;

CREATE TABLE hb_cas_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_cas_paths_gen;
SET GENERATOR hb_cas_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_cas_paths_trig FOR hb_cas_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_cas_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_cas_paths to damuser;

COMMIT;

CREATE TABLE hb_cas(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_cas_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_cas_gen;
SET GENERATOR hb_cas_gen TO 1;

grant all on hb_cas to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_cas_trig FOR hb_cas BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_cas_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_cascategory(
product_id INTEGER NOT NULL REFERENCES hb_cas(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_casfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_cas_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_cascategory to damuser;

commit;

CREATE TABLE hb_casdescription(
product_id INTEGER NOT NULL REFERENCES hb_cas(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_casfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_cas_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_casdescription to damuser;

commit;

CREATE TABLE hb_casstats(
product_id INTEGER NOT NULL REFERENCES hb_cas(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_casfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_casstats to damuser;

commit;

CREATE TABLE hb_casprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_casprice_keys_gen;
SET GENERATOR hb_casprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_casprice_keys_trig FOR hb_casprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_casprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_casprice_keys to damuser;

commit;

CREATE TABLE hb_casprice(
product_id INTEGER NOT NULL REFERENCES hb_cas(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_casprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_casprice to damuser;

commit;

CREATE TABLE hb_casprice_break(
product_id INTEGER NOT NULL REFERENCES hb_cas(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_casprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_casprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_cas','Casualwear','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_casfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_casfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_casfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_casfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_casfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_casfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_casfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_casfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_casfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_casfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

CREATE TABLE hb_champfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_champfields_gen;
SET GENERATOR hb_champfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_champfields_trig FOR hb_champfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_champfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_champfields to damuser;

commit;

CREATE TABLE hb_champ_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_champ_key_gen;
SET GENERATOR hb_champ_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_champ_key_trig FOR hb_champ_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_champ_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_champ_key to damuser;

commit;

CREATE TABLE hb_champ_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_champ_descript_gen;
SET GENERATOR hb_champ_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_champ_descript_trig FOR hb_champ_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_champ_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_champ_descript to damuser;

commit;

CREATE TABLE hb_champ_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_champ_paths_gen;
SET GENERATOR hb_champ_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_champ_paths_trig FOR hb_champ_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_champ_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_champ_paths to damuser;

COMMIT;

CREATE TABLE hb_champ(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_champ_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_champ_gen;
SET GENERATOR hb_champ_gen TO 1;

grant all on hb_champ to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_champ_trig FOR hb_champ BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_champ_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_champcategory(
product_id INTEGER NOT NULL REFERENCES hb_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_champfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_champ_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_champcategory to damuser;

commit;

CREATE TABLE hb_champdescription(
product_id INTEGER NOT NULL REFERENCES hb_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_champfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_champ_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_champdescription to damuser;

commit;

CREATE TABLE hb_champstats(
product_id INTEGER NOT NULL REFERENCES hb_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_champfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_champstats to damuser;

commit;

CREATE TABLE hb_champprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_champprice_keys_gen;
SET GENERATOR hb_champprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_champprice_keys_trig FOR hb_champprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_champprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_champprice_keys to damuser;

commit;

CREATE TABLE hb_champprice(
product_id INTEGER NOT NULL REFERENCES hb_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_champprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_champprice to damuser;

commit;

CREATE TABLE hb_champprice_break(
product_id INTEGER NOT NULL REFERENCES hb_champ(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_champprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_champprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_champ','Champion USA','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_champfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_champfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_champfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_champfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_champfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_champfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_champfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_champfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_champfields(name,display_order,search_order,field_type) values('Business Unit',1103,98,1);

insert into hb_champfields(name,display_order,search_order,field_type) values('Image Type',1104,95,1);

insert into hb_champfields(name,display_order,search_order,field_type) values('Sub-Brand',1105,90,1);

COMMIT;

CREATE TABLE hb_duofields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_duofields_gen;
SET GENERATOR hb_duofields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_duofields_trig FOR hb_duofields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_duofields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_duofields to damuser;

commit;

CREATE TABLE hb_duo_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_duo_key_gen;
SET GENERATOR hb_duo_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_duo_key_trig FOR hb_duo_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_duo_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_duo_key to damuser;

commit;

CREATE TABLE hb_duo_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_duo_descript_gen;
SET GENERATOR hb_duo_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_duo_descript_trig FOR hb_duo_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_duo_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_duo_descript to damuser;

commit;

CREATE TABLE hb_duo_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_duo_paths_gen;
SET GENERATOR hb_duo_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_duo_paths_trig FOR hb_duo_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_duo_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_duo_paths to damuser;

COMMIT;

CREATE TABLE hb_duo(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_duo_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_duo_gen;
SET GENERATOR hb_duo_gen TO 1;

grant all on hb_duo to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_duo_trig FOR hb_duo BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_duo_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_duocategory(
product_id INTEGER NOT NULL REFERENCES hb_duo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_duofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_duo_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_duocategory to damuser;

commit;

CREATE TABLE hb_duodescription(
product_id INTEGER NOT NULL REFERENCES hb_duo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_duofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_duo_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_duodescription to damuser;

commit;

CREATE TABLE hb_duostats(
product_id INTEGER NOT NULL REFERENCES hb_duo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_duofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_duostats to damuser;

commit;

CREATE TABLE hb_duoprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_duoprice_keys_gen;
SET GENERATOR hb_duoprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_duoprice_keys_trig FOR hb_duoprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_duoprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_duoprice_keys to damuser;

commit;

CREATE TABLE hb_duoprice(
product_id INTEGER NOT NULL REFERENCES hb_duo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_duoprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_duoprice to damuser;

commit;

CREATE TABLE hb_duoprice_break(
product_id INTEGER NOT NULL REFERENCES hb_duo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_duoprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_duoprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_duo','Duofold','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_duofields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_duofields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_duofields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_duofields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_duofields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_duofields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_duofields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_duofields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_duofields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_duofields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

CREATE TABLE hb_hosfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_hosfields_gen;
SET GENERATOR hb_hosfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hosfields_trig FOR hb_hosfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_hosfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hosfields to damuser;

commit;

CREATE TABLE hb_hos_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_hos_key_gen;
SET GENERATOR hb_hos_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hos_key_trig FOR hb_hos_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_hos_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hos_key to damuser;

commit;

CREATE TABLE hb_hos_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_hos_descript_gen;
SET GENERATOR hb_hos_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hos_descript_trig FOR hb_hos_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_hos_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hos_descript to damuser;

commit;

CREATE TABLE hb_hos_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_hos_paths_gen;
SET GENERATOR hb_hos_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_hos_paths_trig FOR hb_hos_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_hos_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hos_paths to damuser;

COMMIT;

CREATE TABLE hb_hos(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_hos_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_hos_gen;
SET GENERATOR hb_hos_gen TO 1;

grant all on hb_hos to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_hos_trig FOR hb_hos BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_hos_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_hoscategory(
product_id INTEGER NOT NULL REFERENCES hb_hos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hosfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_hos_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_hoscategory to damuser;

commit;

CREATE TABLE hb_hosdescription(
product_id INTEGER NOT NULL REFERENCES hb_hos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hosfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_hos_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_hosdescription to damuser;

commit;

CREATE TABLE hb_hosstats(
product_id INTEGER NOT NULL REFERENCES hb_hos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_hosfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_hosstats to damuser;

commit;

CREATE TABLE hb_hosprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_hosprice_keys_gen;
SET GENERATOR hb_hosprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_hosprice_keys_trig FOR hb_hosprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_hosprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_hosprice_keys to damuser;

commit;

CREATE TABLE hb_hosprice(
product_id INTEGER NOT NULL REFERENCES hb_hos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hosprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_hosprice to damuser;

commit;

CREATE TABLE hb_hosprice_break(
product_id INTEGER NOT NULL REFERENCES hb_hos(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_hosprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_hosprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_hos','Hanes Hosiery','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_hosfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_hosfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_hosfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_hosfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_hosfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_hosfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_hosfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_hosfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_hosfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_hosfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

CREATE TABLE hb_leggsfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_leggsfields_gen;
SET GENERATOR hb_leggsfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_leggsfields_trig FOR hb_leggsfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_leggsfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_leggsfields to damuser;

commit;

CREATE TABLE hb_leggs_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_leggs_key_gen;
SET GENERATOR hb_leggs_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_leggs_key_trig FOR hb_leggs_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_leggs_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_leggs_key to damuser;

commit;

CREATE TABLE hb_leggs_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_leggs_descript_gen;
SET GENERATOR hb_leggs_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_leggs_descript_trig FOR hb_leggs_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_leggs_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_leggs_descript to damuser;

commit;

CREATE TABLE hb_leggs_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_leggs_paths_gen;
SET GENERATOR hb_leggs_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_leggs_paths_trig FOR hb_leggs_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_leggs_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_leggs_paths to damuser;

COMMIT;

CREATE TABLE hb_leggs(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_leggs_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_leggs_gen;
SET GENERATOR hb_leggs_gen TO 1;

grant all on hb_leggs to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_leggs_trig FOR hb_leggs BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_leggs_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_leggscategory(
product_id INTEGER NOT NULL REFERENCES hb_leggs(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_leggsfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_leggs_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_leggscategory to damuser;

commit;

CREATE TABLE hb_leggsdescription(
product_id INTEGER NOT NULL REFERENCES hb_leggs(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_leggsfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_leggs_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_leggsdescription to damuser;

commit;

CREATE TABLE hb_leggsstats(
product_id INTEGER NOT NULL REFERENCES hb_leggs(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_leggsfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_leggsstats to damuser;

commit;

CREATE TABLE hb_leggsprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_leggsprice_keys_gen;
SET GENERATOR hb_leggsprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_leggsprice_keys_trig FOR hb_leggsprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_leggsprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_leggsprice_keys to damuser;

commit;

CREATE TABLE hb_leggsprice(
product_id INTEGER NOT NULL REFERENCES hb_leggs(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_leggsprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_leggsprice to damuser;

commit;

CREATE TABLE hb_leggsprice_break(
product_id INTEGER NOT NULL REFERENCES hb_leggs(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_leggsprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_leggsprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_leggs','Leggs','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_leggsfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_leggsfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

CREATE TABLE hb_obfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_obfields_gen;
SET GENERATOR hb_obfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_obfields_trig FOR hb_obfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_obfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_obfields to damuser;

commit;

CREATE TABLE hb_ob_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_ob_key_gen;
SET GENERATOR hb_ob_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_ob_key_trig FOR hb_ob_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_ob_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_ob_key to damuser;

commit;

CREATE TABLE hb_ob_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_ob_descript_gen;
SET GENERATOR hb_ob_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_ob_descript_trig FOR hb_ob_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_ob_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_ob_descript to damuser;

commit;

CREATE TABLE hb_ob_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_ob_paths_gen;
SET GENERATOR hb_ob_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_ob_paths_trig FOR hb_ob_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_ob_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_ob_paths to damuser;

COMMIT;

CREATE TABLE hb_ob(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_ob_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_ob_gen;
SET GENERATOR hb_ob_gen TO 1;

grant all on hb_ob to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_ob_trig FOR hb_ob BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_ob_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_obcategory(
product_id INTEGER NOT NULL REFERENCES hb_ob(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_obfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_ob_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_obcategory to damuser;

commit;

CREATE TABLE hb_obdescription(
product_id INTEGER NOT NULL REFERENCES hb_ob(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_obfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_ob_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_obdescription to damuser;

commit;

CREATE TABLE hb_obstats(
product_id INTEGER NOT NULL REFERENCES hb_ob(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_obfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_obstats to damuser;

commit;

CREATE TABLE hb_obprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_obprice_keys_gen;
SET GENERATOR hb_obprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_obprice_keys_trig FOR hb_obprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_obprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_obprice_keys to damuser;

commit;

CREATE TABLE hb_obprice(
product_id INTEGER NOT NULL REFERENCES hb_ob(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_obprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_obprice to damuser;

commit;

CREATE TABLE hb_obprice_break(
product_id INTEGER NOT NULL REFERENCES hb_ob(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_obprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_obprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_ob','Hanes Printables','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_obfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_obfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_obfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_obfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_obfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_obfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_obfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_obfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_obfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_obfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

CREATE TABLE hb_polofields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_polofields_gen;
SET GENERATOR hb_polofields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_polofields_trig FOR hb_polofields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_polofields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_polofields to damuser;

commit;

CREATE TABLE hb_polo_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_polo_key_gen;
SET GENERATOR hb_polo_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_polo_key_trig FOR hb_polo_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_polo_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_polo_key to damuser;

commit;

CREATE TABLE hb_polo_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_polo_descript_gen;
SET GENERATOR hb_polo_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_polo_descript_trig FOR hb_polo_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_polo_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_polo_descript to damuser;

commit;

CREATE TABLE hb_polo_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_polo_paths_gen;
SET GENERATOR hb_polo_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_polo_paths_trig FOR hb_polo_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_polo_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_polo_paths to damuser;

COMMIT;

CREATE TABLE hb_polo(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_polo_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_polo_gen;
SET GENERATOR hb_polo_gen TO 1;

grant all on hb_polo to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_polo_trig FOR hb_polo BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_polo_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_polocategory(
product_id INTEGER NOT NULL REFERENCES hb_polo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_polofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_polo_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_polocategory to damuser;

commit;

CREATE TABLE hb_polodescription(
product_id INTEGER NOT NULL REFERENCES hb_polo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_polofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_polo_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_polodescription to damuser;

commit;

CREATE TABLE hb_polostats(
product_id INTEGER NOT NULL REFERENCES hb_polo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_polofields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_polostats to damuser;

commit;

CREATE TABLE hb_poloprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_poloprice_keys_gen;
SET GENERATOR hb_poloprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_poloprice_keys_trig FOR hb_poloprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_poloprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_poloprice_keys to damuser;

commit;

CREATE TABLE hb_poloprice(
product_id INTEGER NOT NULL REFERENCES hb_polo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_poloprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_poloprice to damuser;

commit;

CREATE TABLE hb_poloprice_break(
product_id INTEGER NOT NULL REFERENCES hb_polo(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_poloprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_poloprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_polo','Polo - Ralph Lauren','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_polofields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_polofields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_polofields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_polofields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_polofields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_polofields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_polofields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_polofields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_polofields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_polofields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

CREATE TABLE hb_socksfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_socksfields_gen;
SET GENERATOR hb_socksfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_socksfields_trig FOR hb_socksfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_socksfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_socksfields to damuser;

commit;

CREATE TABLE hb_socks_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_socks_key_gen;
SET GENERATOR hb_socks_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_socks_key_trig FOR hb_socks_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_socks_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_socks_key to damuser;

commit;

CREATE TABLE hb_socks_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_socks_descript_gen;
SET GENERATOR hb_socks_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_socks_descript_trig FOR hb_socks_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_socks_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_socks_descript to damuser;

commit;

CREATE TABLE hb_socks_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_socks_paths_gen;
SET GENERATOR hb_socks_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_socks_paths_trig FOR hb_socks_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_socks_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_socks_paths to damuser;

COMMIT;

CREATE TABLE hb_socks(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_socks_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_socks_gen;
SET GENERATOR hb_socks_gen TO 1;

grant all on hb_socks to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_socks_trig FOR hb_socks BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_socks_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_sockscategory(
product_id INTEGER NOT NULL REFERENCES hb_socks(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_socksfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_socks_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_sockscategory to damuser;

commit;

CREATE TABLE hb_socksdescription(
product_id INTEGER NOT NULL REFERENCES hb_socks(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_socksfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_socks_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_socksdescription to damuser;

commit;

CREATE TABLE hb_socksstats(
product_id INTEGER NOT NULL REFERENCES hb_socks(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_socksfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_socksstats to damuser;

commit;

CREATE TABLE hb_socksprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_socksprice_keys_gen;
SET GENERATOR hb_socksprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_socksprice_keys_trig FOR hb_socksprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_socksprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_socksprice_keys to damuser;

commit;

CREATE TABLE hb_socksprice(
product_id INTEGER NOT NULL REFERENCES hb_socks(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_socksprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_socksprice to damuser;

commit;

CREATE TABLE hb_socksprice_break(
product_id INTEGER NOT NULL REFERENCES hb_socks(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_socksprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_socksprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_socks','Hanes Socks','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_socksfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_socksfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_socksfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_socksfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_socksfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_socksfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_socksfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_socksfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_socksfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_socksfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

CREATE TABLE hb_uwfields(
field_id INTEGER NOT NULL PRIMARY KEY,
name VARCHAR(40) NOT NULL UNIQUE,
display_order INTEGER NOT NULL,
search_order INTEGER NOT NULL,
field_type INTEGER NOT NULL);
CREATE GENERATOR hb_uwfields_gen;
SET GENERATOR hb_uwfields_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_uwfields_trig FOR hb_uwfields BEFORE INSERT AS
BEGIN
	if(new.field_id is null) then
	BEGIN
		new.field_id=GEN_ID(hb_uwfields_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_uwfields to damuser;

commit;

CREATE TABLE hb_uw_key(
keyword_id INTEGER NOT NULL PRIMARY KEY,
keyword VARCHAR(100) NOT NULL UNIQUE);
CREATE GENERATOR hb_uw_key_gen;
SET GENERATOR hb_uw_key_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_uw_key_trig FOR hb_uw_key BEFORE INSERT AS
BEGIN
	if(new.keyword_id is null) then
	BEGIN
		new.keyword_id=GEN_ID(hb_uw_key_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_uw_key to damuser;

commit;

CREATE TABLE hb_uw_descript(
description_id INTEGER NOT NULL PRIMARY KEY,
description VARCHAR(32765) NOT NULL);
CREATE GENERATOR hb_uw_descript_gen;
SET GENERATOR hb_uw_descript_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_uw_descript_trig FOR hb_uw_descript BEFORE INSERT AS
BEGIN
	if(new.description_id is null) then
	BEGIN
		new.description_id=GEN_ID(hb_uw_descript_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_uw_descript to damuser;

commit;

CREATE TABLE hb_uw_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR hb_uw_paths_gen;
SET GENERATOR hb_uw_paths_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER hb_uw_paths_trig FOR hb_uw_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(hb_uw_paths_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_uw_paths to damuser;

COMMIT;

CREATE TABLE hb_uw(
product_id INTEGER NOT NULL PRIMARY KEY,
primary_label VARCHAR(50) NOT NULL,
path_id INTEGER NOT NULL REFERENCES hb_uw_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE,
inventory INTEGER NOT NULL,
date_cataloged TIMESTAMP NOT NULL,
UNIQUE(primary_label,path_id));
CREATE GENERATOR hb_uw_gen;
SET GENERATOR hb_uw_gen TO 1;

grant all on hb_uw to damuser;

SET TERM !!;
CREATE OR ALTER TRIGGER hb_uw_trig FOR hb_uw BEFORE INSERT AS
BEGIN
	if(new.product_id is null) then
	BEGIN
		new.product_id=GEN_ID(hb_uw_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

CREATE TABLE hb_uwcategory(
product_id INTEGER NOT NULL REFERENCES hb_uw(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_uwfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
keyword_id INTEGER NOT NULL REFERENCES hb_uw_key(keyword_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id,keyword_id));

grant all on hb_uwcategory to damuser;

commit;

CREATE TABLE hb_uwdescription(
product_id INTEGER NOT NULL REFERENCES hb_uw(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_uwfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
description_id INTEGER NOT NULL REFERENCES hb_uw_descript(description_id) ON UPDATE CASCADE ON DELETE CASCADE,
UNIQUE(product_id,field_id));

grant all on hb_uwdescription to damuser;

commit;

CREATE TABLE hb_uwstats(
product_id INTEGER NOT NULL REFERENCES hb_uw(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
field_id INTEGER NOT NULL REFERENCES hb_uwfields(field_id) ON UPDATE CASCADE ON DELETE CASCADE,
val_col FLOAT NOT NULL,
UNIQUE(product_id,field_id,val_col));

grant all on hb_uwstats to damuser;

commit;

CREATE TABLE hb_uwprice_keys(
price_key VARCHAR(40) NOT NULL UNIQUE,
price_key_id INTEGER NOT NULL PRIMARY KEY);
CREATE GENERATOR hb_uwprice_keys_gen;
SET GENERATOR hb_uwprice_keys_gen TO 1;
SET TERM !!;
CREATE OR ALTER TRIGGER hb_uwprice_keys_trig FOR hb_uwprice_keys BEFORE INSERT AS
BEGIN
	if(new.price_key_id is null) then
	BEGIN
		new.price_key_id=GEN_ID(hb_uwprice_keys_gen,1);
	END
END !!
SET TERM ;!!

grant all on hb_uwprice_keys to damuser;

commit;

CREATE TABLE hb_uwprice(
product_id INTEGER NOT NULL REFERENCES hb_uw(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_uwprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
price FLOAT not null,
UNIQUE(product_id,price_key_id));

grant all on hb_uwprice to damuser;

commit;

CREATE TABLE hb_uwprice_break(
product_id INTEGER NOT NULL REFERENCES hb_uw(product_id) ON UPDATE CASCADE ON DELETE CASCADE,
price_key_id INTEGER NOT NULL REFERENCES hb_uwprice_keys(price_key_id) ON UPDATE CASCADE ON DELETE CASCADE,
break_point INTEGER NOT NULL,
price FLOAT NOT NULL,
UNIQUE(product_id,price_key_id,break_point));

grant all on hb_uwprice_break to damuser;

commit;

insert into product_tables(table_name,descriptive_name,description,primary_label,product_type,order_model) 
values('hb_uw','Hanes Underwear','On-Line Library','File Name',1,'com.lazerinc.ecommerce.impl.LazerwebOrderModel');

COMMIT;

insert into hb_uwfields(name,display_order,search_order,field_type) values('File Type',100,0,1);

insert into hb_uwfields(name,display_order,search_order,field_type) values('File Size',100,0,8);

insert into hb_uwfields(name,display_order,search_order,field_type) values('Height',100,0,8);

insert into hb_uwfields(name,display_order,search_order,field_type) values('Width',100,0,8);

insert into hb_uwfields(name,display_order,search_order,field_type) values('Resolution',100,0,8);

insert into hb_uwfields(name,display_order,search_order,field_type) values('Color Type',100,0,1);

insert into hb_uwfields(name,display_order,search_order,field_type) values('Archive',1101,100,1);

insert into hb_uwfields(name,display_order,search_order,field_type) values('Library Name',1102,110,1);

insert into hb_uwfields(name,display_order,search_order,field_type) values('Image Type',1103,95,1);

insert into hb_uwfields(name,display_order,search_order,field_type) values('Sub-Brand',1104,90,1);

COMMIT;

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-C9','Champion','hb_c9orders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_c9orders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-Cas','CasualWear','hb_casorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_casorders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-Champ','Champion USA','hb_champorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_champorders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-Duo','Duofold','hb_duoorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_duoorders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-Hos','Hanes Hosiery','hb_hosorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_hosorders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-Socks','Hanes Socks','hb_socksorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_socksorders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-UW','Hanes Underwear','hb_uworders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_uworders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-Leggs','Leggs','hb_leggsorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_leggsorders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-Ob','Hanes Printables','hb_oborders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_oborders@lazerinc.com',1,0.0);

insert into merchants(merchant_id,name,ordering_email,phone,fax,zip,address1,address2,city_id,state_id,country_id,
credit_cards,fulfillment_email,order_processing,sales_tax)
values('HBI-Polo','Ralph Lauren Polo','hb_poloorders@lazerinc.com','(585) 247-6600','(585) 247-9647','14624','70 Bermar Pk',
'N/A',2,47,236,0,'hb_poloorders@lazerinc.com',1,0.0);

COMMIT;

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_c9'),'HBI-C9');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_cas'),'HBI-Cas');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_champ'),'HBI-Champ');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_duo'),'HBI-Duo');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_hos'),'HBI-Hos');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_socks'),'HBI-Socks');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_uw'),'HBI-UW');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_leggs'),'HBI-Leggs');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_ob'),'HBI-Ob');

insert into merchant_product_tables(product_table_id,merchant_id)
values((select product_table_id from product_tables where table_name='hb_polo'),'HBI-Polo');

COMMIT;

insert into resources (name,resource_type) values('hb_c9',3);
insert into resources (name,resource_type) values('hb_cas',3);
insert into resources (name,resource_type) values('hb_champ',3);
insert into resources (name,resource_type) values('hb_ob',3);
insert into resources (name,resource_type) values('hb_polo',3);
insert into resources (name,resource_type) values('hb_hos',3);
insert into resources (name,resource_type) values('hb_socks',3);
insert into resources (name,resource_type) values('hb_uw',3);
insert into resources (name,resource_type) values('hb_leggs',3);
insert into resources (name,resource_type) values('hb_duo',3);

insert into resources (name,resource_type) values('hb_c9.expired',8);
insert into resources (name,resource_type) values('hb_cas.expired',8);
insert into resources (name,resource_type) values('hb_champ.expired',8);
insert into resources (name,resource_type) values('hb_ob.expired',8);
insert into resources (name,resource_type) values('hb_polo.expired',8);
insert into resources (name,resource_type) values('hb_hos.expired',8);
insert into resources (name,resource_type) values('hb_socks.expired',8);
insert into resources (name,resource_type) values('hb_uw.expired',8);
insert into resources (name,resource_type) values('hb_leggs.expired',8);
insert into resources (name,resource_type) values('hb_duo.expired',8);

COMMIT;
