-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- patch20140102.sql
-- LDEV-3123 Make all DB tables names to be lowercase
SELECT DATABASE() INTO @db_name FROM DUAL; 

SELECT Count(*)
INTO @exists
FROM information_schema.tables
WHERE table_schema = @db_name
    AND table_type = 'BASE TABLE'
    AND BINARY table_name = 'tl_laimag10_imageGallery';

SET @query = If(@exists>0,
    'RENAME TABLE tl_laimag10_imageGallery TO tl_laimag10_imagegallery',
    'SELECT \'nothing to rename\' status');

PREPARE stmt FROM @query;

EXECUTE stmt;


SELECT Count(*)
INTO @exists
FROM information_schema.tables
WHERE table_schema = @db_name
    AND table_type = 'BASE TABLE'
    AND BINARY table_name = 'tl_laimag10_imageGallery_item';

SET @query = If(@exists>0,
    'RENAME TABLE tl_laimag10_imageGallery_item TO tl_laimag10_imagegallery_item',
    'SELECT \'nothing to rename\' status');

PREPARE stmt FROM @query;

EXECUTE stmt; 


-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_laimag10_imagegallery DROP COLUMN online_instructions;
ALTER TABLE tl_laimag10_imagegallery DROP COLUMN offline_instructions;
ALTER TABLE tl_laimag10_imagegallery DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_laimag10_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='laimag10';

-- patch20150217.sql
-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_laimag10_imagegallery MODIFY COLUMN reflect_instructions text;

-- patch20150416.sql
-- LDEV-3450 Set max and min rates limits
ALTER TABLE tl_laimag10_imagegallery ADD COLUMN minimum_rates integer DEFAULT 0; 
ALTER TABLE tl_laimag10_imagegallery ADD COLUMN maximum_rates integer DEFAULT 0;

UPDATE lams_tool SET tool_version='20150416' WHERE tool_signature='laimag10';

-- patch20150930.sql
--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_laimag10_session ADD UNIQUE (session_id);

-- patch20151006.sql
--  LDEV-3568 Move old rating data into the new DB model
INSERT INTO lams_rating_criteria (rating_criteria_type_id, comments_enabled, order_id, tool_content_id)
SELECT 3, true, 0, content_id
FROM tl_laimag10_imagegallery WHERE allow_comment_images=TRUE;

INSERT INTO lams_rating_comment (rating_criteria_id, item_id, user_id, comment)
SELECT criteria.rating_criteria_id, comment.imageGallery_item_uid, comment.create_by, comment.comment
FROM tl_laimag10_image_comment comment, tl_laimag10_imagegallery_item image, tl_laimag10_imagegallery imagegallery, lams_rating_criteria criteria 
	WHERE image.uid=comment.imageGallery_item_uid AND imagegallery.uid=image.imageGallery_uid AND imagegallery.content_id=criteria.tool_content_id AND criteria.order_id=0;


INSERT INTO lams_rating_criteria (title, rating_criteria_type_id, comments_enabled, order_id, tool_content_id)
SELECT "Rating", 3, false, 1, content_id
FROM tl_laimag10_imagegallery WHERE allow_rank=TRUE;

INSERT INTO lams_rating (rating_criteria_id, item_id, user_id, rating)
SELECT criteria.rating_criteria_id, rating.imageGallery_item_uid, rating.create_by, rating.rating
FROM tl_laimag10_image_rating rating, tl_laimag10_imagegallery_item image, tl_laimag10_imagegallery imagegallery, lams_rating_criteria criteria 
	WHERE image.uid=rating.imageGallery_item_uid AND imagegallery.uid=image.imageGallery_uid AND imagegallery.content_id=criteria.tool_content_id AND criteria.order_id=1;

ALTER TABLE tl_laimag10_imagegallery DROP COLUMN allow_comment_images;
DROP TABLE tl_laimag10_image_rating;
DROP TABLE tl_laimag10_image_comment;

UPDATE lams_tool SET tool_version='20151006' WHERE tool_signature='laimag10';

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades
 
ALTER TABLE tl_laimag10_image_vote DROP FOREIGN KEY FK_tl_laimag10_image_vote_2;
ALTER TABLE tl_laimag10_image_vote ADD CONSTRAINT FK_tl_laimag10_image_vote_2 FOREIGN KEY (`create_by`)
REFERENCES `tl_laimag10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_laimag10_image_vote DROP FOREIGN KEY FK_tl_laimag10_image_vote_3;
ALTER TABLE tl_laimag10_image_vote ADD CONSTRAINT FK_tl_laimag10_image_vote_3 FOREIGN KEY (`imageGallery_item_uid`)
REFERENCES `tl_laimag10_imagegallery_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laimag10_imagegallery_item DROP FOREIGN KEY FK_NEW_1821149711_F52D1F9330E79035;
ALTER TABLE tl_laimag10_imagegallery_item ADD CONSTRAINT FK_NEW_1821149711_F52D1F9330E79035 FOREIGN KEY (`imageGallery_uid`)
REFERENCES `tl_laimag10_imagegallery` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laimag10_imagegallery_item DROP FOREIGN KEY FK_NEW_1821149711_F52D1F93758092FB;
ALTER TABLE tl_laimag10_imagegallery_item ADD CONSTRAINT FK_NEW_1821149711_F52D1F93758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laimag10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_laimag10_item_log DROP FOREIGN KEY FK_NEW_1821149711_693580A438BF8DFE;
ALTER TABLE tl_laimag10_item_log ADD CONSTRAINT FK_NEW_1821149711_693580A438BF8DFE FOREIGN KEY (`imageGallery_item_uid`)
REFERENCES `tl_laimag10_imagegallery_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laimag10_item_log DROP FOREIGN KEY FK_NEW_1821149711_693580A441F9365D;
ALTER TABLE tl_laimag10_item_log ADD CONSTRAINT FK_NEW_1821149711_693580A441F9365D FOREIGN KEY (`user_uid`)
REFERENCES `tl_laimag10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laimag10_session DROP FOREIGN KEY FK_NEW_1821149711_24AA78C530E79035;
ALTER TABLE tl_laimag10_session ADD CONSTRAINT FK_NEW_1821149711_24AA78C530E79035 FOREIGN KEY (`imageGallery_uid`)
REFERENCES `tl_laimag10_imagegallery` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laimag10_user DROP FOREIGN KEY FK_NEW_1821149711_30113BFCEC0D3147;
ALTER TABLE tl_laimag10_user ADD CONSTRAINT FK_NEW_1821149711_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_laimag10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
							  
ALTER TABLE tl_laimag10_user DROP FOREIGN KEY FK_NEW_1821149711_30113BFC309ED320;
ALTER TABLE tl_laimag10_user ADD CONSTRAINT FK_NEW_1821149711_30113BFC309ED320 FOREIGN KEY (`imageGallery_uid`)
REFERENCES `tl_laimag10_imagegallery` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
							  

ALTER TABLE tl_laimag10_imagegallery DROP FOREIGN KEY FK_NEW_1821149711_89093BF758092FB;
ALTER TABLE tl_laimag10_imagegallery ADD CONSTRAINT FK_NEW_1821149711_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laimag10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;
							 
-- patch20170101.sql
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

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;