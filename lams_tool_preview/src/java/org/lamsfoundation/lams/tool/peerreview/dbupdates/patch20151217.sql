-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;