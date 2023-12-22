SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains file patch20221116.sql
-- It should upgrade this tool to version 4.7

-- LDEV-5331 Add submission deadline
ALTER TABLE tl_lawhiteboard11_whiteboard ADD COLUMN submission_deadline DATETIME AFTER reflect_on_activity;

SET FOREIGN_KEY_CHECKS=1;