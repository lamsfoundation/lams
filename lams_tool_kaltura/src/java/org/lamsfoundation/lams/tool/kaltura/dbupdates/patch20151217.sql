-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lakalt11_comment DROP FOREIGN KEY FK_tl_lakalt11_comment_2;
ALTER TABLE tl_lakalt11_comment ADD CONSTRAINT FK_tl_lakalt11_comment_2 FOREIGN KEY (`create_by`)
REFERENCES `tl_lakalt11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lakalt11_comment DROP FOREIGN KEY FK_tl_lakalt11_comment_3;
ALTER TABLE tl_lakalt11_comment ADD CONSTRAINT FK_tl_lakalt11_comment_3 FOREIGN KEY (`kaltura_item_uid`)
REFERENCES `tl_lakalt11_kaltura_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lakalt11_item_log DROP FOREIGN KEY FK_tl_lakalt11_item_log_1;
ALTER TABLE tl_lakalt11_item_log ADD CONSTRAINT FK_tl_lakalt11_item_log_1 FOREIGN KEY (`kaltura_item_uid`)
REFERENCES `tl_lakalt11_kaltura_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lakalt11_item_log DROP FOREIGN KEY FK_tl_lakalt11_item_log_2;
ALTER TABLE tl_lakalt11_item_log ADD CONSTRAINT FK_tl_lakalt11_item_log_2 FOREIGN KEY (`user_uid`)
REFERENCES `tl_lakalt11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lakalt11_kaltura DROP FOREIGN KEY FK_NEW_174079131_89093BF758092FB;
ALTER TABLE tl_lakalt11_kaltura ADD CONSTRAINT FK_NEW_174079131_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_lakalt11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lakalt11_kaltura_item DROP FOREIGN KEY FK_tl_lakalt11_item_1;
ALTER TABLE tl_lakalt11_kaltura_item ADD CONSTRAINT FK_tl_lakalt11_item_1 FOREIGN KEY (`create_by`)
REFERENCES `tl_lakalt11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lakalt11_kaltura_item DROP FOREIGN KEY FK_tl_lakalt11_item_2;
ALTER TABLE tl_lakalt11_kaltura_item ADD CONSTRAINT FK_tl_lakalt11_item_2 FOREIGN KEY (`kaltura_uid`)
REFERENCES `tl_lakalt11_kaltura` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lakalt11_rating DROP FOREIGN KEY FK_tl_lakalt11_rating_2;
ALTER TABLE tl_lakalt11_rating ADD CONSTRAINT FK_tl_lakalt11_rating_2 FOREIGN KEY (`create_by`)
REFERENCES `tl_lakalt11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lakalt11_rating DROP FOREIGN KEY FK_tl_lakalt11_rating_3;
ALTER TABLE tl_lakalt11_rating ADD CONSTRAINT FK_tl_lakalt11_rating_3 FOREIGN KEY (`kaltura_item_uid`)
REFERENCES `tl_lakalt11_kaltura_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lakalt11_session DROP FOREIGN KEY FK_NEW_1533935501_B7C198E2FC940906;
ALTER TABLE tl_lakalt11_session ADD CONSTRAINT FK_NEW_1533935501_B7C198E2FC940906 FOREIGN KEY (`kaltura_uid`)
REFERENCES `tl_lakalt11_kaltura` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lakalt11_user DROP FOREIGN KEY FK_tl_lakalt11_user_1;
ALTER TABLE tl_lakalt11_user ADD CONSTRAINT FK_tl_lakalt11_user_1 FOREIGN KEY (`kaltura_session_uid`)
REFERENCES `tl_lakalt11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lakalt11_user DROP FOREIGN KEY FK_tl_lakalt11_user_2;
ALTER TABLE tl_lakalt11_user ADD CONSTRAINT FK_tl_lakalt11_user_2 FOREIGN KEY (`kaltura_uid`)
REFERENCES `tl_lakalt11_kaltura` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;