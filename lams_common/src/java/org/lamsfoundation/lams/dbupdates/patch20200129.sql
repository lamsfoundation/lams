SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

--  LDEV-4959 Prevent leader and non-leader from creating two GradebookUserLessons at the same time

--Take care about potential duplicates. For this move all entries to tmp table first.
CREATE TABLE tmp_table SELECT * FROM lams_gradebook_user_lesson;
TRUNCATE TABLE lams_gradebook_user_lesson;
--Change key to unique
ALTER TABLE `lams_gradebook_user_lesson` 
	DROP FOREIGN KEY `FK_lams_gradebook_user_lesson_1`,
	DROP INDEX `lesson_id`;
ALTER TABLE `lams_gradebook_user_lesson` 
	ADD UNIQUE INDEX `lesson_id` (`lesson_id`,`user_id`),
	ADD CONSTRAINT `FK_lams_gradebook_user_lesson_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`);
--Move entries back to lams_gradebook_user_lesson
INSERT IGNORE INTO lams_gradebook_user_lesson SELECT * from tmp_table;
DROP TABLE tmp_table;

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;