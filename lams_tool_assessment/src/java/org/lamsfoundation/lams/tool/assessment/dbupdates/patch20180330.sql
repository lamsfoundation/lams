-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4540 Allow teachers to release other groups' answers in TBL monitor

ALTER TABLE tl_laasse10_assessment ADD COLUMN allow_disclose_answers TINYINT(1) DEFAULT 0 AFTER allow_overall_feedback;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;