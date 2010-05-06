ALTER TABLE user_requests ADD company VARCHAR(50);
COMMIT;
ALTER TABLE user_requests ADD phone VARCHAR(20);
COMMIT;
ALTER TABLE user_requests ADD reason VARCHAR(32765);
COMMIT;