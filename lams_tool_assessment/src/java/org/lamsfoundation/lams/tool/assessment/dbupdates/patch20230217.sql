-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5366 Allow autoexpanding justification panel on question answer
INSERT INTO `tl_laasse10_configuration` (`config_key`, `config_value`) VALUES ('autoexpandJustification', 'false');

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
