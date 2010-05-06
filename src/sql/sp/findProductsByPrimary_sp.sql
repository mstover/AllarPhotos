set term !!;

CREATE OR ALTER PROCEDURE findProductsByPrimary (table_name VARCHAR(25),primary_value VARCHAR(50))
returns (prod_id INTEGER,family varchar(25))
AS
BEGIN
for execute statement 'SELECT product_id, CAST(''' || :table_name || ''' AS varchar(25)) FROM ' || :table_name || ' where primary_label=''' || :primary_value || ''''
                INTO :prod_id,:family DO
        BEGIN
        	SUSPEND;
        END
END !!


set term ; !!

grant execute on procedure findProductsByPrimary to damuser;
