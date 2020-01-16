SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

--  LDEV-4846 Fix missin QB question content folder IDs

UPDATE lams_qb_question SET content_folder_id = '01234567-89ab-cdef-0123-4567890abcde' WHERE content_folder_id IS NULL;

ALTER TABLE lams_qb_question MODIFY COLUMN content_folder_id char(36) NOT NULL;

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;