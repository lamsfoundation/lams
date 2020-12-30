-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5123 Log learner clicks on MCQ questions in Assessment

CREATE TABLE lams_learner_interaction_event (
	uid BIGINT UNSIGNED auto_increment,
	event_type TINYINT UNSIGNED NOT NULL,
	occurred_date_time DATETIME NOT NULL,
	user_id BIGINT NOT NULL,
	qb_tool_question_uid BIGINT,
	option_uid BIGINT,
	PRIMARY KEY (uid),
	INDEX (event_type),
	CONSTRAINT lams_learner_interaction_event_FK1 FOREIGN KEY (user_id) REFERENCES lams_user (user_id)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT lams_learner_interaction_event_FK2 FOREIGN KEY (qb_tool_question_uid) REFERENCES lams_qb_tool_question (tool_question_uid)
		ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
