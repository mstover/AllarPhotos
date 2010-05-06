rem ---------------------------------------------------------------------------
rem ******** Storage File to DELETE LAZERWEB.GDB Libraries ********************
rem ---------------------------------------------------------------------------

rem DROP ALL TRIGGERS HERE
DROP TRIGGER <<<library>>>_descript_trig;
DROP TRIGGER <<<library>>>_key_trig;
DROP TRIGGER <<<library>>>_trig;
DROP TRIGGER <<<library>>>fields_trig;
DROP TRIGGER <<<library>>>price_keys_trig;

rem remove resource that states this is an active library
DELETE FROM RESOURCES WHERE NAME=<<<LIBRARY>>> and 
	RESOURCE_TYPE='3';

rem delete protected library resources
rem NOTE: must do for both upper and lower-case
DELETE FROM RESOURCES WHERE RESOURCE_TYPE='7' and
	NAME LIKE '<<<LIBRARY>>>.%';

DROP PROCEDURE <<<LIBRARY>>>_cat_values;

rem **** quit and restart service
DROP TABLE <<<library>>>price_break;

rem **** quit and restart service
DROP TABLE <<<library>>>price;
DROP TABLE <<<library>>>price_keys;

rem **** quit and restart service
DROP TABLE <<<library>>>stats;

rem **** quit and restart service
DROP TABLE <<<library>>>description;

rem **** quit and restart service
DROP TABLE <<<library>>>category;
DROP TABLE <<<library>>>_descript;
DROP TABLE <<<library>>>fields;

rem **** quit and restart service
DROP TABLE <<<library>>>;
DROP TABLE <<<library>>>_key;


rem remove user group resource(s) 

rem remove product tables association
delete from product_tables where table_name='<<<library>>>';

rem remove the merchant info if it exists, do this carefully and investigate.
delete from merchants where merchant_id='<<<library>>>';