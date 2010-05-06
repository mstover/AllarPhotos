delete from product_tables where table_name='<library>';

drop table <library>price_break;
drop table <library>price;
drop trigger <library>price_keys_trig;
drop generator <library>price_keys_gen;
drop table <library>price_keys;
drop table <library>stats;
drop table <library>description;
drop table <library>category;
drop trigger <library>_trig;
drop generator <library>_gen;
drop table <library>;
drop trigger <library>_paths_trig;
drop generator <library>_paths_gen;
drop table <library>_paths;
drop trigger <library>_descript_trig;
drop generator <library>_descript_gen;
drop table <library>_descript;
drop trigger <library>_key_trig;
drop generator <library>_key_gen;
drop table <library>_key;
drop trigger <library>fields_trig;
drop generator <library>fields_gen;
drop table <library>fields;

commit;
