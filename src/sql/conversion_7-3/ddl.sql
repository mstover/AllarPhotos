ALTER TABLE irwin ADD date_created TIMESTAMP;

ALTER TABLE irwin ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE edu_demo ADD date_created TIMESTAMP;

ALTER TABLE edu_demo ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE ema_kpg ADD date_created TIMESTAMP;

ALTER TABLE ema_kpg ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_c9 ADD date_created TIMESTAMP;

ALTER TABLE hb_c9 ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_cas ADD date_created TIMESTAMP;

ALTER TABLE hb_cas ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_champ ADD date_created TIMESTAMP;

ALTER TABLE hb_champ ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_duo ADD date_created TIMESTAMP;

ALTER TABLE hb_duo ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_hanes_champ ADD date_created TIMESTAMP;

ALTER TABLE hb_hanes_champ ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_hanes_mag ADD date_created TIMESTAMP;

ALTER TABLE hb_hanes_mag ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_hanes_sleep ADD date_created TIMESTAMP;

ALTER TABLE hb_hanes_sleep ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_hanes_ult ADD date_created TIMESTAMP;

ALTER TABLE hb_hanes_ult ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_hos ADD date_created TIMESTAMP;

ALTER TABLE hb_hos ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_leggs ADD date_created TIMESTAMP;

ALTER TABLE hb_leggs ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_logos ADD date_created TIMESTAMP;

ALTER TABLE hb_logos ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_ob ADD date_created TIMESTAMP;

ALTER TABLE hb_ob ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_polo ADD date_created TIMESTAMP;

ALTER TABLE hb_polo ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_sales ADD date_created TIMESTAMP;

ALTER TABLE hb_sales ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_slush ADD date_created TIMESTAMP;

ALTER TABLE hb_slush ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_socks ADD date_created TIMESTAMP;

ALTER TABLE hb_socks ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE hb_uw ADD date_created TIMESTAMP;

ALTER TABLE hb_uw ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE ia_bali ADD date_created TIMESTAMP;

ALTER TABLE ia_bali ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE ia_bt ADD date_created TIMESTAMP;

ALTER TABLE ia_bt ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE ia_hanes ADD date_created TIMESTAMP;

ALTER TABLE ia_hanes ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE ia_jms ADD date_created TIMESTAMP;

ALTER TABLE ia_jms ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE ia_plytx ADD date_created TIMESTAMP;

ALTER TABLE ia_plytx ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE ia_wndbr ADD date_created TIMESTAMP;

ALTER TABLE ia_wndbr ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE kdk_begs ADD date_created TIMESTAMP;

ALTER TABLE kdk_begs ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE kdk_dai ADD date_created TIMESTAMP;

ALTER TABLE kdk_dai ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE kdk_ink ADD date_created TIMESTAMP;

ALTER TABLE kdk_ink ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE kdk_kids ADD date_created TIMESTAMP;

ALTER TABLE kdk_kids ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE kdk_rev ADD date_created TIMESTAMP;

ALTER TABLE kdk_rev ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE kdk_sec ADD date_created TIMESTAMP;

ALTER TABLE kdk_sec ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE kpro ADD date_created TIMESTAMP;

ALTER TABLE kpro ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE lw_demo ADD date_created TIMESTAMP;

ALTER TABLE lw_demo ADD is_active INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE welch ADD date_created TIMESTAMP;

ALTER TABLE welch ADD is_active INTEGER DEFAULT 1 NOT NULL;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findAllProducts (table_name VARCHAR(25))
returns (prod_id INTEGER,family varchar(25), primary_name VARCHAR(50),product_path VARCHAR(1024),name VARCHAR(40), val VARCHAR(32765),num_field VARCHAR(40),num_val FLOAT, date_cataloged TIMESTAMP,date_modified TIMESTAMP)
AS
declare variable temp_prod_id INTEGER;
BEGIN
for execute statement 'select product_id from ' || :table_name || ' where is_active=1' INTO :temp_prod_id DO
   BEGIN
        execute statement 'SELECT product_id,CAST(''' || :table_name || ''' AS varchar(25)),primary_label, paths.path, CAST(null as varchar(40)) label,CAST(null as varchar(32765)),CAST(null as varchar(40)),CAST(null as FLOAT),DATE_CATALOGED,DATE_MODIFIED FROM ' || :table_name || ' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  where product_id=' || :temp_prod_id
                INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified;
        SUSPEND;
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(25)),CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,CAST(k.keyword as varchar(32765)),CAST(null as varchar(40)),CAST(null as FLOAT),CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                  :table_name || '_key k on c.keyword_id=k.keyword_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(25)), CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,k.description,CAST(null as varchar(40)),CAST(null as FLOAT),CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                 :table_name || '_descript k on c.description_id=k.description_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                 BEGIN
                  SUSPEND;
                 END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(25)),CAST(null as varchar(50)),CAST(null as varchar(1024)), CAST(null as varchar(40)),CAST(null as varchar(32765)),f.name,c.val_col,CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'stats c inner join ' || :table_name || 
                    'fields f on c.field_id=f.field_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                  BEGIN
                  SUSPEND;
                  END
   END
END !!


set term ; !!

grant execute on procedure findAllProducts to damuser;

COMMIT;

set term !! ;

CREATE OR ALTER PROCEDURE findProducts (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value VARCHAR(100))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select p.product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_key k on c.keyword_id=k.keyword_id inner join ' || :table_name || ' p on p.product_id=c.product_id where p.is_active=1 and f.name=''' || :field_name || ''' and k.keyword=''' || :keyword_value || ''''
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END!!

set term ;!!

grant execute on procedure findProducts to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findProductsByDate (table_name VARCHAR(25),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || ' where is_active=1 and CAST(date_cataloged as DATE) >= CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByDate to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findProductsByDescription (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select p.product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_descript k on c.description_id=k.description_id inner join ' || :table_name || ' p on p.product_id=c.product_id where p.is_active=1 and f.name=''' || :field_name || ''' and k.description LIKE ''%' || :keyword_value || '%'''
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByDescription to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findProductsById (table_name VARCHAR(25),id INTEGER)
returns (prod_id INTEGER,family varchar(25), primary_name VARCHAR(50),product_path VARCHAR(1024),name VARCHAR(40), val VARCHAR(32765),num_field VARCHAR(40),num_val FLOAT, date_cataloged TIMESTAMP, date_modified TIMESTAMP)
AS
declare variable temp_prod_id INTEGER;
BEGIN
execute statement 'SELECT product_id,CAST(''' || :table_name || ''' AS varchar(25)),primary_label, paths.path, CAST(null as varchar(40)) label,CAST(null as varchar(32765)),CAST(null as varchar(40)),CAST(null as FLOAT),DATE_CATALOGED,DATE_MODIFIED FROM ' || :table_name || ' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  where product_id =' || id
                INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified;
        SUSPEND;
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(25)),CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,CAST(k.keyword as varchar(32765)),CAST(null as varchar(40)),CAST(null as FLOAT),CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                  :table_name || '_key k on c.keyword_id=k.keyword_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(25)), CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,k.description,CAST(null as varchar(40)),CAST(null as FLOAT),CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                 :table_name || '_descript k on c.description_id=k.description_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                 BEGIN
                  SUSPEND;
                 END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(25)),CAST(null as varchar(50)),CAST(null as varchar(1024)), CAST(null as varchar(40)),CAST(null as varchar(32765)),f.name,c.val_col,CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'stats c inner join ' || :table_name || 
                    'fields f on c.field_id=f.field_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                  BEGIN
                  SUSPEND;
                  END
END !!


set term ; !!

grant execute on procedure findProductsById to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findProductsByModDate (table_name VARCHAR(25),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || ' where is_active=1 and CAST(date_modified as DATE) >= CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByModDate to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findProductsByNumber (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value FLOAT)
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select p.product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'stats c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' || :table_name || ' p on p.product_id=c.product_id where p.is_active=1 and f.name=''' || :field_name || ''' and c.val_col=' || :keyword_value
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByNumber to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findProductsByPrimaryPath (table_name VARCHAR(25),name VARCHAR(50),path VARCHAR(1024))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
if(path is not null) then
	BEGIN
	for execute statement 'SELECT product_id, CAST(''' || :table_name || ''' AS varchar(25)) FROM ' || :table_name || 
		' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  ' ||
		'where is_active=1 and primary_label like ''' || :name || '.%'' and paths.path=''' || :path || ''''
                INTO :prod_id,:family DO
        	BEGIN
        		SUSPEND;
        	END
	END
ELSE
	BEGIN
	for execute statement 'SELECT product_id, CAST(''' || :table_name || ''' AS varchar(25)) FROM ' || :table_name || 
		' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  ' ||
		'where is_active=1 and primary_label like ''' || :name || '.%'''
                INTO :prod_id,:family DO
        	BEGIN
        		SUSPEND;
        	END
	END
END !!


set term ; !!

grant execute on procedure findProductsByPrimaryPath to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findProductsModifiedOn (table_name VARCHAR(25),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || ' where is_active=1 and CAST(date_modified as DATE) = CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsModifiedOn to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE findProductsPostedOn (table_name VARCHAR(25),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || ' where is_active=1 and CAST(date_cataloged as DATE) = CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsPostedOn to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE searchProducts (table_name VARCHAR(25),search_value VARCHAR(100))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select p.product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'description c inner join ' || :table_name || '_descript k on c.description_id=k.description_id inner join ' || :table_name || ' p on p.product_id=c.product_id where p.is_active=1 and UPPER(k.description) LIKE UPPER(''%' || :search_value || '%'')' ||
          'union select p.product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'category c inner join ' || :table_name || '_key k on c.keyword_id=k.keyword_id inner join ' || :table_name || ' p on p.product_id=c.product_id where p.is_active=1 and UPPER(k.keyword) LIKE UPPER(''%' || :search_value || '%'')' ||
          'union SELECT product_id, CAST(''' || :table_name || ''' AS varchar(25)) FROM ' || :table_name || ' where is_active=1 and UPPER(primary_label) LIKE UPPER(''%' || :search_value || '%'')'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure searchProducts to damuser;

COMMIT;

set term !! ;

CREATE OR ALTER PROCEDURE searchProductsWithCategory (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value VARCHAR(100))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select p.product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_key k on c.keyword_id=k.keyword_id inner join ' || :table_name || ' p on p.product_id=c.product_id where p.is_active=1 and f.name=''' || :field_name || ''' and UPPER(k.keyword) LIKE UPPER(''%' || :keyword_value || '%'')'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END!!

set term ;!!

grant execute on procedure searchProductsWithCategory to damuser;

COMMIT;