-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3674 Remove Flash themes
ALTER TABLE lams_user DROP FOREIGN KEY FK_lams_user_4,
					  DROP COLUMN flash_theme_id,
					  CHANGE COLUMN html_theme_id theme_id bigint(20);
DELETE FROM lams_theme WHERE theme_type = 2;
ALTER TABLE lams_theme DROP COLUMN theme_type;
					  
-- Use SVG icons by default
UPDATE lams_learning_activity SET library_activity_ui_image = REPLACE(library_activity_ui_image,'.swf','.svg');

-- No more Flash dumps and themes
DELETE FROM lams_configuration WHERE config_key IN ('DumpDir', 'DefaultFlashTheme');
UPDATE lams_configuration SET config_key = 'DefaultTheme' WHERE config_key = 'DefaultHTMLTheme';

COMMIT;
SET AUTOCOMMIT = 1;