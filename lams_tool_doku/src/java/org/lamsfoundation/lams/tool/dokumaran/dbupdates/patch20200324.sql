SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170303.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1

-- LDEV-4994 Add description field

ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN description MEDIUMTEXT;

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;