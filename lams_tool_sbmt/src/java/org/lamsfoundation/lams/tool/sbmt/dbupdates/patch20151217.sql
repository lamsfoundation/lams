-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lasbmt11_content DROP FOREIGN KEY FKAEF329AC172BC670;
ALTER TABLE tl_lasbmt11_content ADD CONSTRAINT FKAEF329AC172BC670 FOREIGN KEY (`created_by`)
REFERENCES `tl_lasbmt11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lasbmt11_session DROP FOREIGN KEY FKEC8C77C9785A173A;
ALTER TABLE tl_lasbmt11_session ADD CONSTRAINT FKEC8C77C9785A173A FOREIGN KEY (`content_id`)
REFERENCES `tl_lasbmt11_content` (`content_id`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lasbmt11_submission_details DROP FOREIGN KEY FK1411A53C93C861A;
ALTER TABLE tl_lasbmt11_submission_details ADD CONSTRAINT FK1411A53C93C861A FOREIGN KEY (`session_id`)
REFERENCES `tl_lasbmt11_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasbmt11_submission_details DROP FOREIGN KEY FK1411A53CFFD5A38B;
ALTER TABLE tl_lasbmt11_submission_details ADD CONSTRAINT FK1411A53CFFD5A38B FOREIGN KEY (`learner_id`)
REFERENCES `tl_lasbmt11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


DELETE FROM tl_lasbmt11_report WHERE report_id NOT IN (SELECT submission_id FROM tl_lasbmt11_submission_details);
ALTER TABLE tl_lasbmt11_report ADD FOREIGN KEY (`report_id`)
REFERENCES `tl_lasbmt11_submission_details` (`submission_id`) ON DELETE CASCADE ON UPDATE CASCADE;

DELETE FROM tl_lasbmt11_user WHERE session_id NOT IN (SELECT session_id FROM tl_lasbmt11_session);
ALTER TABLE tl_lasbmt11_user ADD FOREIGN KEY (`session_id`)
REFERENCES `tl_lasbmt11_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;