delete from product_tables where table_name='test_library';

drop table test_libraryprice_break;
drop table test_libraryprice;
drop trigger test_libraryprice_keys_trig;
drop generator test_libraryprice_keys_gen;
drop table test_libraryprice_keys;
drop table test_librarystats;
drop table test_librarydescription;
drop table test_librarycategory;
drop trigger test_library_trig;
drop generator test_library_gen;
drop table test_library;
drop trigger test_library_paths_trig;
drop generator test_library_paths_gen;
drop table test_library_paths;
drop trigger test_library_descript_trig;
drop generator test_library_descript_gen;
drop table test_library_descript;
drop trigger test_library_key_trig;
drop generator test_library_key_gen;
drop table test_library_key;
drop trigger test_libraryfields_trig;
drop generator test_libraryfields_gen;
drop table test_libraryfields;

commit;
