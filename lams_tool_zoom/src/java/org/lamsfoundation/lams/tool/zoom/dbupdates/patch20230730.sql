-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here


ALTER TABLE tl_lazoom10_api
    DROP COLUMN api_key,
    DROP COLUMN api_secret,
    ADD COLUMN account_id VARCHAR(32) NOT NULL,
    ADD COLUMN client_id VARCHAR(32) NOT NULL,
    ADD COLUMN client_secret VARCHAR(32) NOT NULL;


-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;