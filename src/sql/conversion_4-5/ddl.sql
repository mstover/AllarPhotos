ALTER TABLE abi ADD date_modified TIMESTAMP;
COMMIT;
update abi set date_modified=date_cataloged;

ALTER TABLE ema_kpg ADD date_modified TIMESTAMP;
COMMIT;
update ema_kpg set date_modified=date_cataloged;

ALTER TABLE hb_c9 ADD date_modified TIMESTAMP;
COMMIT;
update hb_c9 set date_modified=date_cataloged;

ALTER TABLE hb_cas ADD date_modified TIMESTAMP;
COMMIT;
update hb_cas set date_modified=date_cataloged;

ALTER TABLE hb_champ ADD date_modified TIMESTAMP;
COMMIT;
update hb_champ set date_modified=date_cataloged;

ALTER TABLE hb_duo ADD date_modified TIMESTAMP;
COMMIT;
update hb_duo set date_modified=date_cataloged;

ALTER TABLE hb_hanes_champ ADD date_modified TIMESTAMP;
COMMIT;
update hb_hanes_champ set date_modified=date_cataloged;

ALTER TABLE hb_hanes_mag ADD date_modified TIMESTAMP;
COMMIT;
update hb_hanes_mag set date_modified=date_cataloged;

ALTER TABLE hb_hanes_sleep ADD date_modified TIMESTAMP;
COMMIT;
update hb_hanes_sleep set date_modified=date_cataloged;

ALTER TABLE hb_hanes_ult ADD date_modified TIMESTAMP;
COMMIT;
update hb_hanes_ult set date_modified=date_cataloged;

ALTER TABLE hb_hos ADD date_modified TIMESTAMP;
COMMIT;
update hb_hos set date_modified=date_cataloged;

ALTER TABLE hb_leggs ADD date_modified TIMESTAMP;
COMMIT;
update hb_leggs set date_modified=date_cataloged;

ALTER TABLE hb_logos ADD date_modified TIMESTAMP;
COMMIT;
update hb_logos set date_modified=date_cataloged;

ALTER TABLE hb_ob ADD date_modified TIMESTAMP;
COMMIT;
update hb_ob set date_modified=date_cataloged;

ALTER TABLE hb_polo ADD date_modified TIMESTAMP;
COMMIT;
update hb_polo set date_modified=date_cataloged;

ALTER TABLE hb_slush ADD date_modified TIMESTAMP;
COMMIT;
update hb_slush set date_modified=date_cataloged;

ALTER TABLE hb_socks ADD date_modified TIMESTAMP;
COMMIT;
update hb_socks set date_modified=date_cataloged;

ALTER TABLE hb_uw ADD date_modified TIMESTAMP;
COMMIT;
update hb_uw set date_modified=date_cataloged;

ALTER TABLE ia_bali ADD date_modified TIMESTAMP;
COMMIT;
update ia_bali set date_modified=date_cataloged;

ALTER TABLE ia_hanes ADD date_modified TIMESTAMP;
COMMIT;
update ia_hanes set date_modified=date_cataloged;

ALTER TABLE ia_jms ADD date_modified TIMESTAMP;
COMMIT;
update ia_jms set date_modified=date_cataloged;

ALTER TABLE ia_bt ADD date_modified TIMESTAMP;
COMMIT;
update ia_bt set date_modified=date_cataloged;

ALTER TABLE ia_plytx ADD date_modified TIMESTAMP;
COMMIT;
update ia_plytx set date_modified=date_cataloged;

ALTER TABLE ia_wndbr ADD date_modified TIMESTAMP;
COMMIT;
update ia_wndbr set date_modified=date_cataloged;

ALTER TABLE kdk_begs ADD date_modified TIMESTAMP;
COMMIT;
update kdk_begs set date_modified=date_cataloged;

ALTER TABLE kdk_dai ADD date_modified TIMESTAMP;
COMMIT;
update kdk_dai set date_modified=date_cataloged;

ALTER TABLE kdk_ink ADD date_modified TIMESTAMP;
COMMIT;
update kdk_ink set date_modified=date_cataloged;

ALTER TABLE kdk_kids ADD date_modified TIMESTAMP;
COMMIT;
update kdk_kids set date_modified=date_cataloged;

ALTER TABLE kdk_rev ADD date_modified TIMESTAMP;
COMMIT;
update kdk_rev set date_modified=date_cataloged;

ALTER TABLE kdk_sec ADD date_modified TIMESTAMP;
COMMIT;
update kdk_sec set date_modified=date_cataloged;

ALTER TABLE kpro ADD date_modified TIMESTAMP;
COMMIT;
update kpro set date_modified=date_cataloged;

ALTER TABLE lw_demo ADD date_modified TIMESTAMP;
COMMIT;
update lw_demo set date_modified=date_cataloged;

ALTER TABLE playtex ADD date_modified TIMESTAMP;
COMMIT;
update playtex set date_modified=date_cataloged;

ALTER TABLE welch ADD date_modified TIMESTAMP;
COMMIT;
update welch set date_modified=date_cataloged;

ALTER TABLE welchallyn ADD date_modified TIMESTAMP;
COMMIT;
update welchallyn set date_modified=date_cataloged;

ALTER TABLE welch_allyn ADD date_modified TIMESTAMP;
COMMIT;
update welch_allyn set date_modified=date_cataloged;

ALTER TABLE worldkit ADD date_modified TIMESTAMP;
COMMIT;
update worldkit set date_modified=date_cataloged;

set term !!;

CREATE OR ALTER PROCEDURE findAllProducts (table_name VARCHAR(32))
returns (prod_id INTEGER,family varchar(32), primary_name VARCHAR(50),product_path VARCHAR(1024),name VARCHAR(32), val VARCHAR(32765),num_field VARCHAR(32),num_val FLOAT, date_cataloged TIMESTAMP,date_modified TIMESTAMP)
AS
declare variable temp_prod_id INTEGER;
BEGIN
for execute statement 'select product_id from ' || :table_name INTO :temp_prod_id DO
   BEGIN
        execute statement 'SELECT product_id,CAST(''' || :table_name || ''' AS varchar(32)),primary_label, paths.path, CAST(null as varchar(32)) label,CAST(null as varchar(32765)),CAST(null as varchar(32)),CAST(null as FLOAT),DATE_CATALOGED,DATE_MODIFIED FROM ' || :table_name || ' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  where product_id=' || :temp_prod_id
                INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified;
        SUSPEND;
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)),CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,CAST(k.keyword as varchar(32765)),CAST(null as varchar(32)),CAST(null as FLOAT),CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                  :table_name || '_key k on c.keyword_id=k.keyword_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)), CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,k.description,CAST(null as varchar(32)),CAST(null as FLOAT),CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                 :table_name || '_descript k on c.description_id=k.description_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                 BEGIN
                  SUSPEND;
                 END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)),CAST(null as varchar(50)),CAST(null as varchar(1024)), CAST(null as varchar(32)),CAST(null as varchar(32765)),f.name,c.val_col,CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'stats c inner join ' || :table_name || 
                    'fields f on c.field_id=f.field_id where c.product_id=' || :temp_prod_id INTO :prod_id,:family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                  BEGIN
                  SUSPEND;
                  END
   END
END !!


set term ; !!

grant execute on procedure findAllProducts to damuser;

set term !!;

CREATE OR ALTER PROCEDURE findProductsById (table_name VARCHAR(32),id INTEGER)
returns (prod_id INTEGER,family varchar(32), primary_name VARCHAR(50),product_path VARCHAR(1024),name VARCHAR(32), val VARCHAR(32765),num_field VARCHAR(32),num_val FLOAT, date_cataloged TIMESTAMP, date_modified TIMESTAMP)
AS
declare variable temp_prod_id INTEGER;
BEGIN
execute statement 'SELECT product_id,CAST(''' || :table_name || ''' AS varchar(32)),primary_label, paths.path, CAST(null as varchar(32)) label,CAST(null as varchar(32765)),CAST(null as varchar(32)),CAST(null as FLOAT),DATE_CATALOGED,DATE_MODIFIED FROM ' || :table_name || ' inner join ' || :table_name || '_paths paths on paths.path_id=' || :table_name || '.path_id  where product_id =' || id
                INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified;
        SUSPEND;
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)),CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,CAST(k.keyword as varchar(32765)),CAST(null as varchar(32)),CAST(null as FLOAT),CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'category c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                  :table_name || '_key k on c.keyword_id=k.keyword_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                  BEGIN
                  SUSPEND;
                  END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)), CAST(null as varchar(50)),CAST(null as varchar(1024)), f.name,k.description,CAST(null as varchar(32)),CAST(null as FLOAT),CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'description c inner join ' || :table_name || 'fields f on c.field_id=f.field_id inner join ' ||
                 :table_name || '_descript k on c.description_id=k.description_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                 BEGIN
                  SUSPEND;
                 END
        for execute statement 'select product_id,CAST(''' || :table_name || ''' AS varchar(32)),CAST(null as varchar(50)),CAST(null as varchar(1024)), CAST(null as varchar(32)),CAST(null as varchar(32765)),f.name,c.val_col,CAST(null AS TIMESTAMP),CAST(null AS TIMESTAMP) from ' || :table_name || 'stats c inner join ' || :table_name || 
                    'fields f on c.field_id=f.field_id where c.product_id=' || :id INTO :prod_id, :family, :primary_name, :product_path, :name, :val, :num_field, :num_val, :date_cataloged, :date_modified DO
                  BEGIN
                  SUSPEND;
                  END
END !!


set term ; !!

grant execute on procedure findProductsById to damuser;

COMMIT;