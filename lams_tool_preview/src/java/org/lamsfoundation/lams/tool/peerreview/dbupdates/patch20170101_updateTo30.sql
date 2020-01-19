-- SQL statements to update to LAMS 3.0

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

ALTER TABLE `tl_laprev11_user`
ADD UNIQUE INDEX `prev11uniqusersession` (`user_id` ASC, `session_uid` ASC);

--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_laprev11_session ADD UNIQUE (session_id);

-- LDEV-3640 Add necessary cascades
ALTER TABLE tl_laprev11_peerreview DROP FOREIGN KEY FK_NEW_1661738138_89093BF758092FB;
ALTER TABLE tl_laprev11_peerreview ADD CONSTRAINT FK_NEW_1661738138_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laprev11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_laprev11_session DROP FOREIGN KEY FK_NEW_1661738138_24AA78C530E79035;
ALTER TABLE tl_laprev11_session ADD CONSTRAINT FK_NEW_1661738138_24AA78C530E79035 FOREIGN KEY (`peerreview_uid`)
REFERENCES `tl_laprev11_peerreview` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laprev11_user DROP FOREIGN KEY FK_NEW_1661738138_30113BFC309ED320;
ALTER TABLE tl_laprev11_user ADD CONSTRAINT FK_NEW_1661738138_30113BFC309ED320 FOREIGN KEY (`peerreview_uid`)
REFERENCES `tl_laprev11_peerreview` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laprev11_user DROP FOREIGN KEY FK_NEW_1661738138_30113BFCEC0D3147;
ALTER TABLE tl_laprev11_user ADD CONSTRAINT FK_NEW_1661738138_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_laprev11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV-3767 Peer review: two new review methods. Allow for self review
ALTER TABLE tl_laprev11_peerreview ADD COLUMN self_review TINYINT(4) DEFAULT '0';

-- LDEV-3767 Peer review: two new review methods. Allow monitor to email results to users
ALTER TABLE tl_laprev11_peerreview ADD COLUMN notify_users_of_results TINYINT(4) DEFAULT '1';

-- LDEV-4161: enhancements. Differentiate between showing results left for user and results left by user
ALTER TABLE tl_laprev11_peerreview ADD COLUMN show_ratings_left_by_user TINYINT(4) DEFAULT '0';

-- LDEV-4180
ALTER TABLE tl_laprev11_peerreview MODIFY COLUMN lock_on_finished TINYINT(1),
								   MODIFY COLUMN content_in_use TINYINT(1),
								   MODIFY COLUMN define_later TINYINT(1),
								   MODIFY COLUMN show_ratings_left_for_user TINYINT(1) DEFAULT 1,
								   MODIFY COLUMN reflect_on_activity TINYINT(1),
								   MODIFY COLUMN self_review TINYINT(1) DEFAULT 0,
								   MODIFY COLUMN notify_users_of_results TINYINT(1) DEFAULT 1,
								   MODIFY COLUMN show_ratings_left_by_user TINYINT(1) DEFAULT 0;
									 	
ALTER TABLE tl_laprev11_user MODIFY COLUMN session_finished TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='laprev11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;