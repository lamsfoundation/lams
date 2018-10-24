-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4288 Create tables to store archived gradebook entries 

CREATE TABLE lams_gradebook_user_lesson_archive (
  uid bigint(20) NOT NULL,
  lesson_id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  mark double,
  feedback text,
  PRIMARY KEY (uid),
  KEY lesson_id (lesson_id, user_id),
  CONSTRAINT FK_lams_gradebook_user_lesson_archive_1 FOREIGN KEY (lesson_id) REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_gradebook_user_lesson_archive_2 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_gradebook_user_activity_archive (
  uid bigint(20) NOT NULL,
  activity_id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  mark double,
  feedback text,
  marked_in_gradebook tinyint(1) NOT NULL DEFAULT 0,
  update_date datetime,
  PRIMARY KEY (uid),
  KEY activity_id (activity_id, user_id),
  CONSTRAINT FK_lams_gradebook_user_activity_archive_1 FOREIGN KEY (activity_id) REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_gradebook_user_activity_archive_2 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
