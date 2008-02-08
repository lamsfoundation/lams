DROP TABLE IF EXISTS webct_lams_lesson;
CREATE TABLE webct_lams_lesson
(
	id bigint NOT NULL auto_increment,
	pt_id bigint NOT NULL,
	lesson_id bigint NOT NULL,
	learning_context_id bigint NOT NULL,
	sequence_id bigint,
	owner_id varchar(255),
	owner_first_name varchar(255),
	owner_last_name varchar(255),
	title varchar(255) NOT NULL,
	description text,
	hidden bit NOT NULL DEFAULT 0,
	schedule bit NOT NULL DEFAULT 0,
	start_date_time DATETIME,
	end_date_time DATETIME,
  PRIMARY KEY (id),
  UNIQUE KEY (lesson_id)
);
CREATE UNIQUE INDEX webct_lams_lesson_lesson_id ON webct_lams_lesson (lesson_id ASC, pt_id ASC);
COMMIT;