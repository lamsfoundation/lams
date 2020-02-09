SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-4962 Use complex UUIDs for user portraits 

ALTER TABLE lams_cr_node ADD COLUMN portrait_uuid BINARY(16) AFTER node_id,
						 ADD UNIQUE INDEX IDX_portrait_uuid (portrait_uuid);

UPDATE lams_cr_node AS n JOIN lams_user AS u ON n.node_id = u.portrait_uuid
SET n.portrait_uuid = UUID_TO_BIN(UUID());

ALTER TABLE lams_user ADD COLUMN temp BINARY(16) AFTER portrait_uuid;

UPDATE lams_cr_node AS n JOIN lams_user AS u ON n.node_id = u.portrait_uuid
SET u.temp = n.portrait_uuid;

ALTER TABLE lams_user DROP COLUMN portrait_uuid;
ALTER TABLE lams_user RENAME COLUMN temp TO portrait_uuid;

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;

