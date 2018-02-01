SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV-4507 Make sure that duplicate groups are not created
ALTER TABLE lams_group ADD UNIQUE KEY UQ_lams_group_1 (grouping_id, order_id);
 
COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
