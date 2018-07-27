-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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
							 
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;