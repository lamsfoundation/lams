SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20210809.sql to 20210819.sql
-- It should upgrade this tool to version 4.6

--LDEV-4921 Allow question title hiding in learner
INSERT INTO `tl_lascrt11_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

--LDEV-4976 Add cascade so when a row from parent table gets removed, a row from inheriting table gets removed too
ALTER TABLE tl_lascrt11_answer_log ADD CONSTRAINT FK_tl_lascrt11_answer_log_1 FOREIGN KEY (uid) 
	REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE;

--LDEV-5032 Allow hiding learner names in confidence levels in Scratchie
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN confidence_levels_anonymous TINYINT DEFAULT 0 AFTER confidence_levels_activity_uiid;

--LDEV-5136 Require double click, instead of a single click, to reveal an item
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN double_click TINYINT DEFAULT 0 AFTER time_limit;

--LDEV-5185 Add advanced time limits to Scratchie
ALTER TABLE tl_lascrt11_scratchie CHANGE COLUMN time_limit relative_time_limit SMALLINT UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE tl_lascrt11_scratchie ADD COLUMN absolute_time_limit DATETIME AFTER relative_time_limit;

ALTER TABLE tl_lascrt11_session ADD COLUMN time_limit_adjustment SMALLINT AFTER time_limit_launched_date;

-- LDEV-5208 Add advanced option whether to show discussion widget in TBL monitoring
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN discussion_sentiment_enabled TINYINT DEFAULT 0 AFTER burning_questions_enabled;

 -- LDEV-5230 Remove "add extra mark" option
DELETE FROM tl_lascrt11_configuration WHERE config_key = 'isEnabledExtraPointOption';

ALTER TABLE tl_lascrt11_scratchie DROP COLUMN extra_point;

SET FOREIGN_KEY_CHECKS=1;