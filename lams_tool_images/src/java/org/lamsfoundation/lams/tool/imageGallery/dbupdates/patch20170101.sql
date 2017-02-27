-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180

ALTER TABLE tl_laimag10_imagegallery MODIFY COLUMN lock_on_finished TINYINT(1),
									 MODIFY COLUMN content_in_use TINYINT(1),
									 MODIFY COLUMN define_later TINYINT(1),
									 MODIFY COLUMN allow_share_images TINYINT(1),
									 MODIFY COLUMN allow_vote TINYINT(1),
									 MODIFY COLUMN reflect_on_activity TINYINT(1),
									 MODIFY COLUMN allow_rank TINYINT(1),
									 MODIFY COLUMN image_submit_notify TINYINT(1) DEFAULT 0;
									 
ALTER TABLE tl_laimag10_imagegallery_item MODIFY COLUMN create_by_author TINYINT(1),
									 	  MODIFY COLUMN is_hide TINYINT(1);
									 
ALTER TABLE tl_laimag10_image_vote MODIFY COLUMN is_voted TINYINT(1);

ALTER TABLE tl_laimag10_item_log MODIFY COLUMN complete TINYINT(1);

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='laimag10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;