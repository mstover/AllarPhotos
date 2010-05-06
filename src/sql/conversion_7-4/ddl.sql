set term !!;

CREATE OR ALTER PROCEDURE findProductsByCreateDate (table_name VARCHAR(25),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || ' where is_active=1 and CAST(date_created as DATE) >= CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByCreateDate to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE m_findAllProducts (table_name VARCHAR(25))
returns (prod_id INTEGER,family varchar(25), primary_name VARCHAR(50),product_path VARCHAR(1024),name VARCHAR(40), val VARCHAR(32765),num_field VARCHAR(40),num_val FLOAT, date_cataloged TIMESTAMP,date_modified TIMESTAMP)
AS
declare variable temp_prod_id INTEGER;
BEGIN
for execute statement 'select product_id from ' || :table_name INTO :temp_prod_id DO
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

grant execute on procedure m_findAllProducts to damuser;

COMMIT;

set term !! ;

CREATE OR ALTER PROCEDURE m_findProducts (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value VARCHAR(100))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_key k on c.keyword_id=k.keyword_id where f.name=''' || :field_name || ''' and k.keyword=''' || :keyword_value || ''''
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END!!

set term ;!!

grant execute on procedure m_findProducts to damuser;

COMMIT;

set term !! ;

CREATE OR ALTER PROCEDURE m_findProducts (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value VARCHAR(100))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_key k on c.keyword_id=k.keyword_id where f.name=''' || :field_name || ''' and k.keyword=''' || :keyword_value || ''''
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END!!

set term ;!!

grant execute on procedure m_findProducts to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE m_findProductsByDescription (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_descript k on c.description_id=k.description_id where f.name=''' || :field_name || ''' and k.description LIKE ''%' || :keyword_value || '%'''
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure m_findProductsByDescription to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE m_findProductsByModDate (table_name VARCHAR(25),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || ' where and CAST(date_modified as DATE) >= CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure m_findProductsByModDate to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE m_findProductsByNumber (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value FLOAT)
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'stats c inner join ' || :table_name || 'fields f on c.field_id=f.field_id where f.name=''' || :field_name || ''' and c.val_col=' || :keyword_value
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure m_findProductsByNumber to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE m_findProductsByPrimaryPath (table_name VARCHAR(25),name VARCHAR(50),path VARCHAR(1024))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
if(path is not null) then
	BEGIN
	for execute statement 'SELECT product_id, CAST(''' || :table_name || ''' AS varchar(25)) FROM ' || :table_name || 
		' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  ' ||
		'where primary_label like ''' || :name || '.%'' and paths.path=''' || :path || ''''
                INTO :prod_id,:family DO
        	BEGIN
        		SUSPEND;
        	END
	END
ELSE
	BEGIN
	for execute statement 'SELECT product_id, CAST(''' || :table_name || ''' AS varchar(25)) FROM ' || :table_name || 
		' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  ' ||
		'where primary_label like ''' || :name || '.%'''
                INTO :prod_id,:family DO
        	BEGIN
        		SUSPEND;
        	END
	END
END !!


set term ; !!

grant execute on procedure m_findProductsByPrimaryPath to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE m_findProductsModifiedOn (table_name VARCHAR(25),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || ' where CAST(date_modified as DATE) = CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure m_findProductsModifiedOn to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE m_findProductsPostedOn (table_name VARCHAR(25),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || ' where CAST(date_cataloged as DATE) = CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure m_findProductsPostedOn to damuser;

COMMIT;

set term !!;

CREATE OR ALTER PROCEDURE m_searchProducts (table_name VARCHAR(25),search_value VARCHAR(100))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'description c inner join ' || :table_name || '_descript k on c.description_id=k.description_id where UPPER(k.description) LIKE UPPER(''%' || :search_value || '%'')' ||
          'union select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'category c inner join ' || :table_name || '_key k on c.keyword_id=k.keyword_id where UPPER(k.keyword) LIKE UPPER(''%' || :search_value || '%'')' ||
          'union SELECT product_id, CAST(''' || :table_name || ''' AS varchar(25)) FROM ' || :table_name || ' where UPPER(primary_label) LIKE UPPER(''%' || :search_value || '%'')'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure m_searchProducts to damuser;

COMMIT;

set term !! ;

CREATE OR ALTER PROCEDURE m_searchProductsWithCategory (table_name VARCHAR(25),field_name VARCHAR(40),keyword_value VARCHAR(100))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(25)) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
          :table_name || '_key k on c.keyword_id=k.keyword_id where f.name=''' || :field_name || ''' and UPPER(k.keyword) LIKE UPPER(''%' || :keyword_value || '%'')'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END!!

set term ;!!

grant execute on procedure m_searchProductsWithCategory to damuser;

COMMIT;
