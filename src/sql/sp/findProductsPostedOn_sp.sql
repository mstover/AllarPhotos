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
