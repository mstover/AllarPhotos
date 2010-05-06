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
