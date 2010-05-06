set term !!;

CREATE OR ALTER PROCEDURE findProductsModifiedOn (table_name VARCHAR(32),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || ' where CAST(date_modified as DATE) = CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsModifiedOn to damuser;

set term !!;

CREATE OR ALTER PROCEDURE findProductsPostedOn (table_name VARCHAR(32),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || ' where CAST(date_cataloged as DATE) = CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsPostedOn to damuser;
