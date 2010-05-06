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
