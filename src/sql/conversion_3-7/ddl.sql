CREATE TABLE user_requests (
request_id INTEGER NOT NULL PRIMARY KEY,
username varchar(35),
last_name varchar(30),
first_name varchar(30),
email varchar(80),
resource_id INTEGER REFERENCES resources(resource_id) ON UPDATE CASCADE ON DELETE CASCADE);

grant all on user_requests to damuser;

CREATE GENERATOR user_requests_gen;
SET GENERATOR user_requests_gen to 1;
SET TERM !!;
CREATE OR ALTER TRIGGER user_requests_trig FOR user_requests BEFORE INSERT AS
BEGIN
	if(new.request_id IS NULL) then
	BEGIN
		new.request_id=GEN_ID(user_requests_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

