-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3756 Noticeboard comments: set thread to top
ALTER TABLE lams_comment 
ADD COLUMN sticky SMALLINT(6) NULL DEFAULT 0,
ADD INDEX IX_comment_level_sticky (comment_level ASC, sticky ASC);

COMMIT;
SET AUTOCOMMIT = 1;