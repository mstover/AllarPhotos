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
