SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20210304.sql
-- It should upgrade this tool to version 4.6

-- LDEV-5186 Merge two "notify on submit" column into one
UPDATE tl_larsrc11_resource SET assigment_submit_notify = 1 WHERE file_upload_notify = 1;
						  
SET FOREIGN_KEY_CHECKS=1;