SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- --------------------Put all sql statements below here-------------------------

ALTER TABLE `tl_laprev11_user`
ADD UNIQUE INDEX `prev11uniqusersession` (`user_id` ASC, `session_uid` ASC);

-- --------------------Put all sql statements above here-------------------------
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;