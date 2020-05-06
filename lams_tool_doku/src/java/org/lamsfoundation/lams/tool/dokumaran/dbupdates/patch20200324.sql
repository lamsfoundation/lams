SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- LDEV-4994 Add description field

ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN description MEDIUMTEXT;

-- LDEV-5008 Rename doKumaran tool 
-- Set default tool content
UPDATE tl_ladoku11_dokumaran SET description = 'Instructions', instructions = 'Document' where UID = 1;


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
