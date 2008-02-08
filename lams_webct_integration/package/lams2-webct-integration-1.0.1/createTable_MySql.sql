DROP TABLE IF EXISTS ${dbTable};
CREATE TABLE ${dbTable}
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
CREATE UNIQUE INDEX ${dbTable}_lesson_id ON ${dbTable} (lesson_id ASC, pt_id ASC);
COMMIT;