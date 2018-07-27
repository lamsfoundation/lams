-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3292 Add a vital index for notebook entries
CREATE INDEX ext_sig_user ON lams_notebook_entry(external_id, external_id_type, external_signature, user_id); 

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
