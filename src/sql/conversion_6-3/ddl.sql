set term !!;

CREATE OR ALTER PROCEDURE searchUsers (search_value VARCHAR(128))
returns (user_id INTEGER)
AS
BEGIN
	for execute statement 'select users.user_id from users where UPPER(username) like UPPER(''%' || :search_value || '%'') or ' ||
	                      'UPPER(last_name) like UPPER(''%' || :search_value || '%'') or UPPER(first_name) like ' ||
	                      'UPPER(''%' || :search_value || '%'') or UPPER(email) like UPPER(''%' || :search_value || '%'') ' ||
	                      'union select user_id from user_data inner join user_values on user_data.value_id=user_values.value_id ' ||
	                      'where UPPER(user_values.val_col) like UPPER(''%' || :search_value || '%'')' into :user_id DO                          
		   BEGIN
		        SUSPEND;
		   END
END !!


set term ; !!

grant execute on procedure searchUsers to damuser;
