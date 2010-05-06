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
