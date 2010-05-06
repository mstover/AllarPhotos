#create a procedure for category searches
SET TERM !!;
CREATE PROCEDURE <library>_cat_values(cat_name varchar(100))
RETURNS (product_id INTEGER, keyword VARCHAR(100))
AS
DECLARE VARIABLE key_id INTEGER;
BEGIN
FOR
   SELECT DISTINCT keyword_id, product_id
   FROM <library>category, <library>fields
   WHERE <library>category.field_id=<library>fields.field_id AND name = :cat_name
   INTO :key_id, :product_id
DO
   BEGIN
           SELECT keyword FROM <library>_key
           WHERE keyword_id = :key_id
           INTO :keyword;
           SUSPEND;
   END
END!!
SET TERM ;!!