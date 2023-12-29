SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20221220.sql to patch20230309.sql
-- It should upgrade this tool to version 4.7

-- LDEV-5351 Tell whether and which teacher marked the question manually
ALTER TABLE tl_laasse10_question_result
    ADD COLUMN marked_by BIGINT,
	ADD CONSTRAINT FK_tl_laasse10_question_result_3 FOREIGN KEY (marked_by) REFERENCES tl_laasse10_user(uid)
		ON UPDATE CASCADE ON DELETE SET NULL;

-- LDEV-5359 Add question sections
CREATE TABLE tl_laasse10_section(
                                    uid MEDIUMINT UNSIGNED NOT NULL auto_increment,
                                    assessment_uid BIGINT,
                                    display_order TINYINT UNSIGNED NOT NULL DEFAULT '1',
                                    name VARCHAR(100),
                                    question_count TINYINT UNSIGNED NOT NULL,
                                    CONSTRAINT FK_tl_laasse10_section_1  FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment(uid)
                                        ON UPDATE CASCADE ON DELETE CASCADE,
                                    PRIMARY KEY (uid)
);

-- LDEV-5360 Add marker's comment column to question result
ALTER TABLE tl_laasse10_question_result
    ADD COLUMN marker_comment VARCHAR(100);

-- LDEV-5360 Use core Users and not AssessmentUsers for marker column
UPDATE tl_laasse10_question_result AS r JOIN tl_laasse10_user AS u ON r.marked_by = u.uid
    SET r.marked_by = u.user_id;

-- LDEV-5366 Allow autoexpanding justification panel on question answer
INSERT INTO `tl_laasse10_configuration` (`config_key`, `config_value`) VALUES ('autoexpandJustification', 'false');

-- LDEV-5360 Use core Users and not AssessmentUsers for marker column
ALTER TABLE tl_laasse10_question_result
DROP FOREIGN KEY FK_tl_laasse10_question_result_3;
ALTER TABLE tl_laasse10_question_result
    ADD CONSTRAINT FK_tl_laasse10_question_result_3 FOREIGN KEY (marked_by) REFERENCES lams_user(user_id)
        ON UPDATE CASCADE ON DELETE SET NULL;

-- LDEV-5374 Show max mark next to each question
ALTER TABLE tl_laasse10_assessment ADD COLUMN display_max_mark TINYINT NOT NULL DEFAULT 0 AFTER display_summary;

SET FOREIGN_KEY_CHECKS=1;