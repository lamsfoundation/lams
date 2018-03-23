SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV-4421 Need an item column to support Share Resources comments
-- No point having the unique index comment_ext_sig_user any longer
-- as it would need to include external_secondary_id which could be null
-- and two null values are always treated as distinct. Keeping an index on 
-- signature and main id for lookup efficiency.
ALTER TABLE lams_comment_session
ADD COLUMN external_secondary_id BIGINT(20);

ALTER TABLE lams_comment_session
DROP INDEX comment_ext_sig_user; 

ALTER TABLE lams_comment_session
ADD INDEX comment_ext_sig (external_id ASC, external_signature ASC);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
