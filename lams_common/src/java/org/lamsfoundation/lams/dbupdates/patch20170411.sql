-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4312 Handle unsuccessful mark submission to integrated servers

INSERT INTO lams_log_event_type VALUES (7, 'TYPE_LEARNER_LESSON_COMPLETE');
INSERT INTO lams_log_event_type VALUES (8, 'TYPE_LEARNER_LESSON_MARK_SUBMIT');

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
