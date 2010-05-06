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
