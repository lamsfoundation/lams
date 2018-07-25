-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lascrb11_heading DROP FOREIGN KEY FK428A22FFB3FA1495;
ALTER TABLE tl_lascrb11_heading ADD CONSTRAINT FK428A22FFB3FA1495 FOREIGN KEY (`scribe_uid`)
REFERENCES `tl_lascrb11_scribe` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lascrb11_report_entry DROP FOREIGN KEY FK5439FACA1C266FAE;
ALTER TABLE tl_lascrb11_report_entry ADD CONSTRAINT FK5439FACA1C266FAE FOREIGN KEY (`scribe_session_uid`)
REFERENCES `tl_lascrb11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrb11_report_entry DROP FOREIGN KEY FK5439FACAEA50D086;
ALTER TABLE tl_lascrb11_report_entry ADD CONSTRAINT FK5439FACAEA50D086 FOREIGN KEY (`scribe_heading_uid`)
REFERENCES `tl_lascrb11_heading` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

 
ALTER TABLE tl_lascrb11_session DROP FOREIGN KEY FK89732793B3FA1495;
ALTER TABLE tl_lascrb11_session ADD CONSTRAINT FK89732793B3FA1495 FOREIGN KEY (`scribe_uid`)
REFERENCES `tl_lascrb11_scribe` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrb11_session DROP FOREIGN KEY FK89732793E46919FF;
ALTER TABLE tl_lascrb11_session ADD CONSTRAINT FK89732793E46919FF FOREIGN KEY (`appointed_scribe_uid`)
REFERENCES `tl_lascrb11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lascrb11_user DROP FOREIGN KEY FK187DAFEE1C266FAE;
ALTER TABLE tl_lascrb11_user ADD CONSTRAINT FK187DAFEE1C266FAE FOREIGN KEY (`scribe_session_uid`)
REFERENCES `tl_lascrb11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;