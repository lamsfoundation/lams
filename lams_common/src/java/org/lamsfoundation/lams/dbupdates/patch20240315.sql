-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5452 Allow removing questions in live lesson

ALTER TABLE lams_learner_interaction_event DROP FOREIGN KEY lams_learner_interaction_event_FK2;
ALTER TABLE lams_learner_interaction_event ADD CONSTRAINT `lams_learner_interaction_event_FK` FOREIGN KEY (`qb_tool_question_uid`) REFERENCES `lams_qb_tool_question` (`tool
_question_uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;