SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-5027 Convert a boolean value of LTI user ID parameter name into string

ALTER TABLE lams_ext_server_org_map ADD COLUMN user_id_parameter_name VARCHAR(255);
UPDATE lams_ext_server_org_map SET user_id_parameter_name = 'user_id' WHERE use_alternative_user_id_parameter_name = 0;
UPDATE lams_ext_server_org_map SET user_id_parameter_name = 'lis_person_sourcedid' WHERE use_alternative_user_id_parameter_name = 1;
ALTER TABLE lams_ext_server_org_map DROP COLUMN use_alternative_user_id_parameter_name;

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;

