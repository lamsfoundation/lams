-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_laasse10_assessment_overall_feedback DROP FOREIGN KEY FK_tl_laasse10_assessment_overall_feedback_1;
ALTER TABLE tl_laasse10_assessment_overall_feedback ADD CONSTRAINT FK_tl_laasse10_assessment_overall_feedback_1 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_assessment_question DROP FOREIGN KEY FK_NEW_1720029621_F52D1F9330E79035;
ALTER TABLE tl_laasse10_assessment_question ADD CONSTRAINT FK_NEW_1720029621_F52D1F9330E79035 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_assessment_question DROP FOREIGN KEY FK_NEW_1720029621_F52D1F93758092FB;
ALTER TABLE tl_laasse10_assessment_question ADD CONSTRAINT FK_NEW_1720029621_F52D1F93758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laasse10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_assessment_question DROP FOREIGN KEY FK_NEW_1720029621_F52D1F93EC0D3147;
ALTER TABLE tl_laasse10_assessment_question ADD CONSTRAINT FK_NEW_1720029621_F52D1F93EC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_laasse10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_assessment_result DROP FOREIGN KEY FK_tl_laasse10_assessment_result_2;
ALTER TABLE tl_laasse10_assessment_result ADD CONSTRAINT FK_tl_laasse10_assessment_result_2 FOREIGN KEY (`user_uid`)
REFERENCES `tl_laasse10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_assessment_result DROP FOREIGN KEY FK_tl_laasse10_assessment_result_3;
ALTER TABLE tl_laasse10_assessment_result ADD CONSTRAINT FK_tl_laasse10_assessment_result_3 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_assessment_unit DROP FOREIGN KEY FK_tl_laasse10_assessment_unit_1;
ALTER TABLE tl_laasse10_assessment_unit ADD CONSTRAINT FK_tl_laasse10_assessment_unit_1 FOREIGN KEY (`question_uid`)
REFERENCES `tl_laasse10_assessment_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

 
ALTER TABLE tl_laasse10_question_option DROP FOREIGN KEY FK_tl_laasse10_question_option_1;
ALTER TABLE tl_laasse10_question_option ADD CONSTRAINT FK_tl_laasse10_question_option_1 FOREIGN KEY (`question_uid`)
REFERENCES `tl_laasse10_assessment_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_question_result DROP FOREIGN KEY FK_tl_laasse10_question_result_1;
ALTER TABLE tl_laasse10_question_result ADD CONSTRAINT FK_tl_laasse10_question_result_1	FOREIGN KEY (`result_uid`)
REFERENCES `tl_laasse10_assessment_result` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_question_result DROP FOREIGN KEY FK_NEW_1720029621_693580A438BF8DFE;
ALTER TABLE tl_laasse10_question_result ADD CONSTRAINT FK_NEW_1720029621_693580A438BF8DFE FOREIGN KEY (`assessment_question_uid`)
REFERENCES `tl_laasse10_assessment_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_option_answer DROP FOREIGN KEY FK_tl_laasse10_option_answer_1;
ALTER TABLE tl_laasse10_option_answer ADD CONSTRAINT FK_tl_laasse10_option_answer_1 FOREIGN KEY (`question_result_uid`)
REFERENCES `tl_laasse10_question_result` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
	

ALTER TABLE tl_laasse10_question_reference DROP FOREIGN KEY FK_tl_laasse10_question_reference_1;
ALTER TABLE tl_laasse10_question_reference ADD CONSTRAINT FK_tl_laasse10_question_reference_1 FOREIGN KEY (`question_uid`)
REFERENCES `tl_laasse10_assessment_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_question_reference DROP FOREIGN KEY FK_tl_laasse10_question_reference_2;
ALTER TABLE tl_laasse10_question_reference ADD CONSTRAINT FK_tl_laasse10_question_reference_2 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
 
 
ALTER TABLE tl_laasse10_user DROP FOREIGN KEY FK_NEW_1720029621_30113BFC309ED320;
ALTER TABLE tl_laasse10_user ADD CONSTRAINT FK_NEW_1720029621_30113BFC309ED320 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laasse10_user DROP FOREIGN KEY FK_NEW_1720029621_30113BFCEC0D3147;
ALTER TABLE tl_laasse10_user ADD CONSTRAINT FK_NEW_1720029621_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_laasse10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_session DROP FOREIGN KEY FK_NEW_1720029621_24AA78C530E79035;
ALTER TABLE tl_laasse10_session ADD CONSTRAINT FK_NEW_1720029621_24AA78C530E79035 FOREIGN KEY (`assessment_uid`)
REFERENCES `tl_laasse10_assessment` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laasse10_assessment DROP FOREIGN KEY FK_NEW_1720029621_89093BF758092FB;
ALTER TABLE tl_laasse10_assessment ADD CONSTRAINT FK_NEW_1720029621_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laasse10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;
								
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;