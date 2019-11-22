SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-4914 Do not allow NULLs in Lesson settings

-- set any null values to default value of 0
UPDATE lams_lesson SET
	learner_presence_avail = IFNULL(learner_presence_avail, 0),
    learner_im_avail = IFNULL(learner_im_avail, 0),
    live_edit_enabled = IFNULL(live_edit_enabled, 0),
    enable_lesson_notifications = IFNULL(enable_lesson_notifications, 0),
    locked_for_edit = IFNULL(locked_for_edit, 0),
    marks_released = IFNULL(marks_released, 0),
    enable_lesson_intro = IFNULL(enable_lesson_intro, 0),
    display_design_image = IFNULL(display_design_image, 0),
    force_restart = IFNULL(force_restart, 0),
    allow_restart = IFNULL(allow_restart, 0),
    gradebook_on_complete = IFNULL(gradebook_on_complete, 0);


ALTER TABLE lams_lesson
	MODIFY COLUMN learner_presence_avail TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN learner_im_avail TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN live_edit_enabled TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN enable_lesson_notifications TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN locked_for_edit TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN marks_released TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN enable_lesson_intro TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN display_design_image TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN force_restart TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN allow_restart TINYINT(1) NOT NULL DEFAULT 0,
	MODIFY COLUMN gradebook_on_complete TINYINT(1) NOT NULL DEFAULT 0;

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
