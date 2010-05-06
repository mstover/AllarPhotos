CREATE TABLE <library>_paths (
path_id INTEGER NOT NULL PRIMARY KEY,
path VARCHAR(1024) NOT NULL);
CREATE GENERATOR <library>_paths_gen;
SET GENERATOR <library>_paths_gen TO 1;
SET TERM !! ;
CREATE TRIGGER <library>_paths_trig FOR <library>_paths BEFORE INSERT AS
BEGIN
	if(new.path_id is null) then
	BEGIN
		new.path_id=GEN_ID(<library>_paths_gen,1);
	END
END !!
SET TERM ;!!

COMMIT;

grant all on <library>_paths to damuser;

insert into <library>_paths(path) values('');

COMMIT;

ALTER TABLE <library> add path_id INTEGER DEFAULT 2 NOT NULL REFERENCES <library>_paths(path_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE <library> add primary_label2 varchar(50) DEFAULT '' NOT NULL;
update <library> set primary_label2=primary_label;
ALTER TABLE <library> drop primary_label;
ALTER TABLE <library> alter primary_label2 TO primary_label;
ALTER TABLE <library> add CONSTRAINT Unique_Image_<library> UNIQUE(primary_label,path_id);

insert into <library>fields(name,display_order,search_order,field_type) values('Color Type',0,0,1);

COMMIT;
