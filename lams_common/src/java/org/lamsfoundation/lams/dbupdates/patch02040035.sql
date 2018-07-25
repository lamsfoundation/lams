SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

--  LDEV-3383: Remove Web authentication method
UPDATE lams_user SET authentication_method_id = 1 WHERE authentication_method_id = 2;
DELETE FROM lams_authentication_method WHERE authentication_method_id = 2;

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;