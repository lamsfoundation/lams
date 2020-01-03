SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170221.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


--  LDEV-4244 Add new advanced option - shuffle scratchie Items
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN shuffle_items TINYINT(1) DEFAULT 0;



-- LDEV-4451 add 'Display confidence levels' advanced option
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN confidence_levels_activity_uiid INT(11);



-- LDEV-4458 Add option to overwrite default scoring system
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN preset_marks varchar(255);




-- LDEV-3224 Ability to change, add, remove questions even after student have reached it
ALTER TABLE tl_lascrt11_user DROP FOREIGN KEY FK_NEW_610529188_30113BFC309ED320;
ALTER TABLE tl_lascrt11_user DROP COLUMN scratchie_uid, DROP INDEX FK_NEW_610529188_30113BFC309ED320;

UPDATE lams_tool SET tool_version='20180223' WHERE tool_signature='lascrt11';



-- LDEV-4530 Turn on/off showing scratchies to learners on results page
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN show_scratchies_in_results TINYINT(1) DEFAULT 1;




-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lascrt11_scratchie MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_lascrt11_scratchie MODIFY reflect_instructions MEDIUMTEXT;
ALTER TABLE tl_lascrt11_scratchie_answer MODIFY description MEDIUMTEXT;
ALTER TABLE tl_lascrt11_scratchie_item MODIFY description MEDIUMTEXT;



-- LDEV-4558 Bump tool version so we can import tool content from older versions
UPDATE lams_tool SET tool_version='20180828' WHERE tool_signature='lascrt11';



-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lascrt11';



-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;