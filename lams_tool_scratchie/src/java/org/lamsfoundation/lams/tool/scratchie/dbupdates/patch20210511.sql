-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5208 Add advanced option whether to show discussion widget in TBL monitoring

ALTER TABLE tl_lascrt11_scratchie ADD COLUMN discussion_sentiment_enabled TINYINT DEFAULT 0 AFTER burning_questions_enabled;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
