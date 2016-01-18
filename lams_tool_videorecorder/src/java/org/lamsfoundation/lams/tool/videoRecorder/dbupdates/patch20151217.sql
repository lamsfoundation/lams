-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades
 
ALTER IGNORE TABLE tl_lavidr10_session DROP FOREIGN KEY FK_NEW_75587508_B7C198E2FC940906;
ALTER TABLE tl_lavidr10_session ADD CONSTRAINT FK_NEW_75587508_B7C198E2FC940906 FOREIGN KEY (`videoRecorder_uid`)
REFERENCES `tl_lavidr10_videorecorder` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER IGNORE TABLE tl_lavidr10_user DROP FOREIGN KEY FK_NEW_75587508_CB8A58FFA3B0FADF;
ALTER TABLE tl_lavidr10_user ADD CONSTRAINT FK_NEW_75587508_CB8A58FFA3B0FADF FOREIGN KEY (`videoRecorder_session_uid`)
REFERENCES `tl_lavidr10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
 
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;