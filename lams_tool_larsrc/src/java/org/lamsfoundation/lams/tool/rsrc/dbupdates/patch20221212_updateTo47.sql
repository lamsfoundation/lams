SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files from patch20220211.sql to patch20221212.sql
-- It should upgrade this tool to version 4.7

-- LDEV-5186 Merge two "notify on submit" column into one
UPDATE tl_larsrc11_resource SET assigment_submit_notify = 1 WHERE file_upload_notify = 1;

-- LDEV-5287 Use a single instructions field instead of multiple ones
ALTER TABLE tl_larsrc11_resource_item CHANGE COLUMN description instructions TEXT;

-- set concat max len to its max
-- https://dev.mysql.com/doc/refman/8.0/en/server-system-variables.html#sysvar_group_concat_max_len
SET SESSION group_concat_max_len=18446744073709551615;

UPDATE tl_larsrc11_resource_item AS r JOIN
    (SELECT item_uid, GROUP_CONCAT(description ORDER BY sequence_id SEPARATOR '<br><br>') AS merged_instructions FROM tl_larsrc11_item_instruction GROUP BY item_uid)
    AS i ON r.uid = i.item_uid
    SET r.instructions = CONCAT(IF(r.instructions IS NULL OR TRIM(r.instructions) = '', '', CONCAT(TRIM(r.instructions), '<br><br>')), i.merged_instructions);

DROP TABLE tl_larsrc11_item_instruction;

-- remove zipped website and IMS package item types

ALTER TABLE tl_larsrc11_resource_item DROP COLUMN organization_xml,
DROP COLUMN ims_schema,
									  DROP COLUMN init_item;

-- remove auto run functionality
ALTER TABLE tl_larsrc11_resource DROP COLUMN allow_auto_run;

-- reintroduce zipped website item types
ALTER TABLE tl_larsrc11_resource_item ADD COLUMN init_item VARCHAR(255) AFTER item_type;

SET FOREIGN_KEY_CHECKS=1;