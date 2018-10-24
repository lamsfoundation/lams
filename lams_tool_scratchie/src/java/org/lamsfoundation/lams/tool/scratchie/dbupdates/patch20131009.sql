-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

-- LKC-35 Be able import learning design with Scratchie tool exported from another server
ALTER TABLE tl_lascrt11_scratchie_item DROP FOREIGN KEY FK_NEW_610529188_F52D1F93758092FB;
ALTER TABLE tl_lascrt11_scratchie_item DROP COLUMN create_by;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
