-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5208 Add Discussion Sentiment widget

CREATE TABLE lams_discussion_sentiment (
	uid BIGINT AUTO_INCREMENT,
	lesson_id BIGINT NOT NULL,
	tool_content_id BIGINT NOT NULL,
	burning_question_uid BIGINT,
	user_id BIGINT,
	selected_option TINYINT UNSIGNED,
	PRIMARY KEY (uid),
	INDEX IDX_lams_discussion_sentiment_burning_question_uid (burning_question_uid),
	CONSTRAINT FK_lams_discussion_sentiment_1 FOREIGN KEY (lesson_id) REFERENCES lams_lesson (lesson_id)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_discussion_sentiment_2 FOREIGN KEY (tool_content_id) REFERENCES lams_tool_content (tool_content_id)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_discussion_sentiment_3 FOREIGN KEY (user_id) REFERENCES lams_user (user_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);
	

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
