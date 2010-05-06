CREATE TABLE virtual_libraries (
virtual_library_id INTEGER NOT NULL PRIMARY KEY,
name varchar(35) UNIQUE,
user_id INTEGER REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE);

CREATE GENERATOR virtual_libraries_gen;
SET GENERATOR virtual_libraries_gen TO 1;
SET TERM !! ;
CREATE OR ALTER TRIGGER virtual_libraries_trig FOR virtual_libraries BEFORE INSERT AS
BEGIN
	if(new.virtual_library_id is null) then
	BEGIN
		new.virtual_library_id=GEN_ID(virtual_libraries_gen,1);
	END
END !!
SET TERM ; !!

grant all on virtual_libraries to damuser;

COMMIT;

CREATE TABLE virtual_products (
virtual_library_id INTEGER REFERENCES virtual_libraries(virtual_library_id) ON UPDATE CASCADE ON DELETE CASCADE,
product_table varchar(25),
product_id INTEGER NOT NULL,
UNIQUE(virtual_library_id,product_table,product_id));

grant all on virtual_products to damuser;

COMMIT;