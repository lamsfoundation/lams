-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;