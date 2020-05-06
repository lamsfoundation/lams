SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-4962 Use random UUIDs v4 for user portraits 

ALTER TABLE lams_cr_node ADD COLUMN temp BIGINT;

UPDATE lams_cr_node AS n JOIN lams_user AS u USING (portrait_uuid)
SET n.temp = u.user_id,
	n.portrait_uuid = UUID_TO_BIN(UUID_V4());

UPDATE lams_cr_node AS n JOIN lams_user AS u ON n.temp = u.user_id
SET u.portrait_uuid = n.portrait_uuid;

ALTER TABLE lams_cr_node DROP COLUMN temp;

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;