set term !!;

CREATE OR ALTER PROCEDURE findUsers (id INTEGER,in_username VARCHAR(35))
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
	if(id is not null) then
		BEGIN
			for execute statement 'select user_id,bill_address1,bill_address2,bill_zip,bill_city_id,bill_state_id,bill_country_id,' ||
		                      'ship_address1,ship_address2,ship_zip,ship_city_id,ship_state_id,ship_country_id,phone,email,' ||
		                      'fax,first_name,last_name,middle_initial,passwd,username,exp_date,referrer_id,company_id,' ||
		                      'CAST(null as char(100)),'  ||
		                      'CAST(null as char(100)),CAST(null as varchar(32000)) from users where user_id=' || :id
		          INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id, 
		        		:key_name, :short_value, :long_value DO
		   BEGIN
		        SUSPEND;
		        execute procedure getUserProperties(:user_id) returning_values(:user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
		        		:key_name, :short_value, :long_value); 
		        SUSPEND;
		   END
		END
	ELSE
		BEGIN
			for execute statement 'select user_id,bill_address1,bill_address2,bill_zip,bill_city_id,bill_state_id,bill_country_id,' ||
		                      'ship_address1,ship_address2,ship_zip,ship_city_id,ship_state_id,ship_country_id,phone,email,' ||
		                      'fax,first_name,last_name,middle_initial,passwd,username,exp_date,referrer_id,company_id,' ||
		                      'CAST(null as char(100)),'  ||
		                      'CAST(null as char(100)),CAST(null as varchar(32000)) from users where username=''' || :in_username || ''''
		          INTO :user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
		        		:key_name, :short_value, :long_value DO
		   BEGIN
		        SUSPEND;
		        execute procedure getUserProperties(:user_id) returning_values(:user_id, :bill_address1, :bill_address2, :bill_zip, :bill_city_id, :bill_state_id, :bill_country_id, :ship_address1,
		        		:ship_address2, :ship_zip, :ship_city_id, :ship_state_id, :ship_country_id, :phone, :email, :fax, :first_name,
		        		:last_name, :middle_initial, :passwd, :username, :exp_date, :referrer_id, :company_id,
		        		:key_name, :short_value, :long_value); 
		        SUSPEND;
		   END
		END
END !!


set term ; !!

grant execute on procedure findUsers to damuser;
