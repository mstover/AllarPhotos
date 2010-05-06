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
