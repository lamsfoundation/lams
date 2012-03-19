-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-2829  Lesson intro page
ALTER TABLE lams_lesson ADD COLUMN enable_lesson_intro TINYINT(1) DEFAULT 0;
ALTER TABLE lams_lesson ADD COLUMN display_design_image TINYINT(1) DEFAULT 0;

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;