-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades


ALTER TABLE tl_lantbk11_session DROP FOREIGN KEY FKB7C198E2FC940906;
ALTER TABLE tl_lantbk11_session ADD CONSTRAINT FKB7C198E2FC940906 FOREIGN KEY (`notebook_uid`)
REFERENCES `tl_lantbk11_notebook` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lantbk11_user DROP FOREIGN KEY FKCB8A58FFA3B0FADF;
ALTER TABLE tl_lantbk11_user ADD CONSTRAINT FKCB8A58FFA3B0FADF FOREIGN KEY (`notebook_session_uid`)
REFERENCES `tl_lantbk11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;