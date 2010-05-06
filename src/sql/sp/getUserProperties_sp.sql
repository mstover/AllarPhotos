set term !!;

CREATE OR ALTER PROCEDURE getUserProperties (id INTEGER)
returns (user_id INTEGER, 
         bill_address1 varchar(50),
        bill_address2 varchar(50),
        bill_zip varchar(12),
        bill_city_id INTEGER,
        bill_state_id INTEGER,
        bill_country_id INTEGER,
        ship_address1 varchar(50),
        ship_address2 varchar(50),
        ship_zip varchar(12),
        ship_city_id INTEGER,
        ship_state_id INTEGER,
        ship_country_id INTEGER,
        phone varchar(25),
        email varchar(80),
        fax varchar(25),
        first_name varchar(30),
        last_name varchar(30),
        middle_initial varchar(5),
        passwd varchar(80),
        username varchar(35),
        exp_date TIMESTAMP,
        referrer_id INTEGER,
        company_id INTEGER,
        key_name char(100),
        short_value char(100),
        long_value varchar(32000))
AS
BEGIN
	for execute statement 'select user_id,CAST(null as varchar(50)),CAST(null as varchar(50)),CAST(null as varchar(12)),' ||
        		'CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as varchar(50)),' ||
        		'CAST(null as varchar(50)),CAST(null as varchar(12)),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'CAST(null as INTEGER),CAST(null as varchar(25)),CAST(null as varchar(80)),CAST(null as varchar(25)),' ||
        		'CAST(null as varchar(30)),CAST(null as varchar(30)),CAST(null as varchar(5)),CAST(null as varchar(80)),' ||
        		'CAST(null as varchar(35)),CAST(null as TIMESTAMP),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'user_att.name,user_values.val_col,' ||
        		'CAST(null as varchar(32000)) from user_data inner join user_att on user_data.att_id=user_att.att_id ' ||
        			'inner join user_values on user_data.value_id=user_values.value_id where user_data.user_id=' || :id
        			INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
        		:key_name, :short_value, :long_value DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select user_id,CAST(null as varchar(50)),CAST(null as varchar(50)),CAST(null as varchar(12)),' ||
        		'CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as INTEGER),CAST(null as varchar(50)),' ||
        		'CAST(null as varchar(50)),CAST(null as varchar(12)),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'CAST(null as INTEGER),CAST(null as varchar(25)),CAST(null as varchar(80)),CAST(null as varchar(25)),' ||
        		'CAST(null as varchar(30)),CAST(null as varchar(30)),CAST(null as varchar(5)),CAST(null as varchar(80)),' ||
        		'CAST(null as varchar(35)),CAST(null as TIMESTAMP),CAST(null as INTEGER),CAST(null as INTEGER),' ||
        		'user_att.name,CAST(NULL as char(100)),' ||
                'user_long_values.long_value from user_long_data inner join user_att on user_long_data.att_id=user_att.att_id ' ||
        			'inner join user_long_values on user_long_data.long_value_id=user_long_values.long_value_id where user_long_data.user_id=' || :id
        			INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
        		 :key_name, :short_value, :long_value DO
                  BEGIN
                  SUSPEND;
                  END
END !!


set term ; !!

grant execute on procedure getUserProperties to damuser;
