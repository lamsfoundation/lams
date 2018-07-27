-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3629 Drop an obsolete column, otherwise it would require conversion.
-- It is in a separate script so it does not fail if the next script fails for any reason and needs to be repeated.

ALTER TABLE lams_user DROP COLUMN openid_url;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;