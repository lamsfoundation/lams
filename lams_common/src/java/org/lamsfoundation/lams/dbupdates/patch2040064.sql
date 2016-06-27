-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3839: Lesson restart

-- Tables for archiving learner progress
CREATE TABLE lams_learner_progress_archive (
  learner_progress_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  lesson_id bigint(20) NOT NULL,
  attempt_id TINYINT NOT NULL DEFAULT 1,
  lesson_completed_flag tinyint(1) NOT NULL DEFAULT 0,
  start_date_time datetime NOT NULL,
  finish_date_time datetime,
  current_activity_id bigint(20),
  PRIMARY KEY (learner_progress_id),
  UNIQUE KEY IX_lams_learner_progress_archive_1 (user_id, lesson_id, attempt_id),
  CONSTRAINT FK_lams_learner_progress_archive_1 FOREIGN KEY (user_id)
  	REFERENCES lams_user (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_lams_learner_progress_archive_2 FOREIGN KEY (lesson_id)
  	REFERENCES lams_lesson (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_lams_learner_progress_archive_3 FOREIGN KEY (current_activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET utf8mb4;


CREATE TABLE lams_progress_attempted_archive (
  learner_progress_id bigint(20) NOT NULL,
  activity_id bigint(20) NOT NULL,
  start_date_time datetime,
  PRIMARY KEY (learner_progress_id, activity_id),
  CONSTRAINT FK_lams_progress_current_archive_1 FOREIGN KEY (learner_progress_id)
  	REFERENCES lams_learner_progress_archive (learner_progress_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_lams_progress_current_archive_2 FOREIGN KEY (activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET utf8mb4;


CREATE TABLE lams_progress_completed_archive (
  learner_progress_id bigint(20) NOT NULL,
  activity_id bigint(20) NOT NULL,
  completed_date_time datetime,
  start_date_time datetime,
  PRIMARY KEY (learner_progress_id,activity_id),
  CONSTRAINT FK_lams_progress_completed_archive_1 FOREIGN KEY (learner_progress_id)
  	REFERENCES lams_learner_progress_archive (learner_progress_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_lams_progress_completed_archive_2 FOREIGN KEY (activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- make restart an option
ALTER TABLE lams_lesson CHANGE COLUMN learner_restart force_restart tinyint(1) DEFAULT '0',
						ADD COLUMN allow_restart tinyint(1) DEFAULT '0';


COMMIT;
SET AUTOCOMMIT = 1;