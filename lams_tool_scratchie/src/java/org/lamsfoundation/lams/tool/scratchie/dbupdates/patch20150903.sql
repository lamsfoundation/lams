-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3548 Prevent Scratchie tool from storing duplicate scratches done by the same user
ALTER TABLE `tl_lascrt11_answer_log` ADD UNIQUE `FK_NEW_lascrt11_30113BFC309ED321`(`scratchie_answer_uid`, `session_id`);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
