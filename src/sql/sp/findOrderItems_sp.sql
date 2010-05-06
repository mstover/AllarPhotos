set term !!;

CREATE OR ALTER PROCEDURE findOrderItems (category_name VARCHAR(50),category_value VARCHAR(252),dateCol VARCHAR(32))
returns (item_id INTEGER,date_column TIMESTAMP, category VARCHAR(50),val VARCHAR(252))
AS
declare tempOrderNo VARCHAR(252);
declare tempId INTEGER;
BEGIN
for execute statement 'select li.item_id,li.date_column,cat.val,val.val from log_items li inner join log on li.item_id=log.item_id ' || 
		'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where CAST(li.date_column as DATE)>CAST(''' || :dateCol || ''' as DATE) and ' ||
		'cat.val=''orderNo'' and exists (' ||
		'select log.item_id from log inner join log_names ln on log.category_id=ln.category_id ' ||
		'inner join log_values lv on log.value_id=lv.value_id where log.item_id=li.item_id and ln.val=''' || :category_name || 
		''' and lv.val=''' || :category_value || ''')' INTO :item_id,:date_column,:category,:val DO
   BEGIN
   	tempOrderNo = :val;
   	tempId = :item_id;
   	SUSPEND;
        for execute statement 'SELECT ' || :item_id || ', CAST(null as TIMESTAMP),cat.val,val.val from log ' ||
        	'inner join log_names cat on log.category_id=cat.category_id ' ||
		'inner join log_values val on log.value_id=val.value_id ' ||
		'where log.item_id=' || :item_id INTO :item_id,:date_column,:category,:val DO
                BEGIN
                	SUSPEND;
                END
        if(category_name != 'orderNo') then
        BEGIN
        	for execute statement 'select li.item_id,li.date_column,cat.val,val.val from log_items li inner join log on li.item_id=log.item_id ' || 
			'inner join log_names cat on log.category_id=cat.category_id ' ||
			'inner join log_values val on log.value_id=val.value_id ' ||
			'where cat.val=''orderNo'' and val.val=''' || :tempOrderNo || ''' and li.item_id <> ' || :tempId
			 INTO :item_id,:date_column,:category,:val DO
			BEGIN
				SUSPEND;
        			for execute statement 'SELECT ' || :item_id || ', CAST(null as TIMESTAMP),cat.val,val.val from log ' ||
        				'inner join log_names cat on log.category_id=cat.category_id ' ||
					'inner join log_values val on log.value_id=val.value_id ' ||
					'where log.item_id=' || :item_id INTO :item_id,:date_column,:category,:val DO
                			BEGIN
                				SUSPEND;
                			END
                	END
         END
			
   END
END !!


set term ; !!

grant execute on procedure findOrderItems to damuser;
