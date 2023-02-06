-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--  LDEV-5357 Use complex UUIDs for all files
ALTER TABLE lams_cr_node RENAME COLUMN portrait_uuid TO uuid,
						 RENAME INDEX IDX_portrait_uuid TO IDX_uuid;

UPDATE lams_cr_node SET uuid = UUID_TO_BIN(LOWER(CONCAT(
    HEX(RANDOM_BYTES(4)), '-',
    HEX(RANDOM_BYTES(2)), '-4',
    SUBSTR(HEX(RANDOM_BYTES(2)), 2, 3), '-',
    CONCAT(HEX(FLOOR(ASCII(RANDOM_BYTES(1)) / 64)+8),SUBSTR(HEX(RANDOM_BYTES(2)), 2, 3)), '-',
    HEX(RANDOM_BYTES(6))
))) WHERE uuid IS NULL;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
