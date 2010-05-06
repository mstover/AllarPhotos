ALTER TABLE user_requests add address1 VARCHAR(50);

ALTER TABLE user_requests add address2 VARCHAR(50);

ALTER TABLE user_requests add city VARCHAR(50);

ALTER TABLE user_requests add state VARCHAR(50);

ALTER TABLE user_requests add country VARCHAR(50);

ALTER TABLE user_requests add zip VARCHAR(12);

COMMIT;