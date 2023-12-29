SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20221116.sql to patch20230330.sql
-- It should upgrade this tool to version 4.7

-- LDEV-5331 Add submission deadline
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN submission_deadline DATETIME AFTER reflect_on_activity;

-- LDEV-5378 Add max mark for whole dokumaran activity
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN max_mark TINYINT UNSIGNED NOT NULL DEFAULT '100' AFTER reflect_on_activity;

SET FOREIGN_KEY_CHECKS=1;