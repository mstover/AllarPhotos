set term !!;

CREATE OR ALTER PROCEDURE findProductsByDate (table_name VARCHAR(32),keyword_value VARCHAR(32))
returns (prod_id INTEGER,family varchar(32))
AS
BEGIN
for execute statement 'select product_id, CAST(''' || :table_name || ''' AS varchar(32)) from ' || :table_name || ' where CAST(date_cataloged as DATE) >= CAST(''' || :keyword_value || ''' as DATE)'
          INTO :prod_id,:family DO
   BEGIN
        SUSPEND;
   END
END !!


set term ; !!

grant execute on procedure findProductsByDate to damuser;