-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-LDEV-5425 Remove "End of activity" notebook from *all* tools
ALTER TABLE tl_lantbk11_user ADD COLUMN notebook_entry mediumtext;
ALTER TABLE tl_lantbk11_user ADD COLUMN notebook_entry_modified_date datetime DEFAULT NULL;

UPDATE tl_lantbk11_user ntbkuser
INNER JOIN lams_notebook_entry entry ON ntbkuser.entry_uid = entry.uid
SET ntbkuser.notebook_entry = entry.entry, ntbkuser.notebook_entry_modified_date = entry.create_date;

ALTER TABLE tl_lantbk11_user DROP COLUMN entry_uid;
DROP TABLE lams_notebook_entry;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;