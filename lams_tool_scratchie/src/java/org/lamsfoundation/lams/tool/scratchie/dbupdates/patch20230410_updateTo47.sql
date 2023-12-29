SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch202230220.sql to patch20240410.sql
-- It should upgrade this tool to version 4.7

-- LDEV-5361 Add option to prevent learners from advancing if they have not found all answers
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN require_all_answers TINYINT DEFAULT '1' AFTER double_click;

-- LDEV-5385 Allow real numbers for marks
ALTER TABLE tl_lascrt11_session MODIFY COLUMN mark FLOAT DEFAULT '0';

SET FOREIGN_KEY_CHECKS=1;