-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3765 Create a table for storing commands sent to Learners
-- No foreign keys as maybe in the future it will be possible to send commands to a learner in any lesson
-- or to all learners in a given lesson.

CREATE TABLE lams_learning_command (
	uid BIGINT(20) NOT NULL AUTO_INCREMENT,
	lesson_id BIGINT(20),
	user_name VARCHAR(191),
	create_date DATETIME NOT NULL,
	command_text TEXT,
	PRIMARY KEY (uid),
	INDEX idx_lesson_id (lesson_id),
	INDEX idx_user_name (user_name),
	INDEX idx_create_date (create_date)
)ENGINE=InnoDB;

COMMIT;
SET AUTOCOMMIT = 1;
