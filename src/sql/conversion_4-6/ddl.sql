ALTER TABLE product_tables ADD customer_name VARCHAR(50) NOT NULL;

COMMIT;

update product_tables set customer_name='Hanes Brands' 
where table_name in ('ia_bali','ia_bt','ia_hanes',
'ia_jms','ia_plytx','ia_wndbr','hb_uw','hb_socks','hb_slush','hb_polo','hb_ob','hb_logos','hb_leggs','hb_hos',
'hb_hanes_ult','hb_hanes_sleep','hb_hanes_mag','hb_hanes_champ','hb_duo','hb_champ',
'hb_cas','hb_c9');

update product_tables set customer_name='Associated Brands' where table_name='abi';

update product_tables set customer_name='Kodak' where table_name in ('kdk_begs',
'kdk_dai','kdk_ink','kdk_sec','kpro');

update product_tables set customer_name='Welch Allyn' where table_name='welch';

update product_tables set customer_name='World Kitchen' where table_name='worldkit';

update product_tables set customer_name='Lazer' where table_name='lw_demo';

COMMIT;

ALTER TABLE states add valid_state INTEGER DEFAULT 1 NOT NULL;
ALTER TABLE countries add valid_country INTEGER DEFAULT 1 NOT NULL;

COMMIT;