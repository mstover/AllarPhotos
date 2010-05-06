set term !! ;
CREATE OR ALTER PROCEDURE grantall (username varchar(32))
AS
declare variable tablename char(31);
BEGIN
for execute statement 'select RDB$Relation_Name from RDB$Relations where RDB$FLAGS=1 and RDB$OWNER_NAME=''SYSDBA''' INTO :tablename DO
   BEGIN
        execute statement 'grant all on ' || :tablename || ' to ' || :username;
   END
END !!
set term ; !!
