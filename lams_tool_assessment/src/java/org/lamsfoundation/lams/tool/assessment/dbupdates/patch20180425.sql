-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_laasse10_assessment MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment MODIFY reflect_instructions MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_overall_feedback MODIFY feedback MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY question MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY general_feedback MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY feedback MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY feedback_on_correct MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY feedback_on_partially_correct MEDIUMTEXT;
ALTER TABLE tl_laasse10_assessment_question MODIFY feedback_on_incorrect MEDIUMTEXT;
ALTER TABLE tl_laasse10_question_option MODIFY question MEDIUMTEXT;
ALTER TABLE tl_laasse10_question_option MODIFY option_string MEDIUMTEXT;
ALTER TABLE tl_laasse10_question_option MODIFY feedback MEDIUMTEXT;
ALTER TABLE tl_laasse10_question_result MODIFY answer_string MEDIUMTEXT;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;