-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE lams_progress_attempted DROP FOREIGN KEY FK_lams_progress_current_1, DROP FOREIGN KEY FK_lams_progress_current_2;
ALTER TABLE lams_progress_attempted ADD CONSTRAINT FK_lams_progress_current_1 FOREIGN KEY (learner_progress_id)
			REFERENCES lams_learner_progress (learner_progress_id) ON DELETE CASCADE ON UPDATE CASCADE,
  		ADD CONSTRAINT FK_lams_progress_current_2 FOREIGN KEY (activity_id)
  			REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lams_progress_completed DROP FOREIGN KEY FK_lams_progress_completed_1, DROP FOREIGN KEY FK_lams_progress_completed_2;
ALTER TABLE lams_progress_completed ADD CONSTRAINT FK_lams_progress_completed_1 FOREIGN KEY (learner_progress_id)
			REFERENCES lams_learner_progress (learner_progress_id) ON DELETE CASCADE ON UPDATE CASCADE,
  		ADD CONSTRAINT FK_lams_progress_completed_2 FOREIGN KEY (activity_id)
  			REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;