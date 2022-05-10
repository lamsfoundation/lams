-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5210 Add optional prefix to Whiteboard canvas IDs

  
INSERT INTO tl_lawhiteboard11_configuration (config_key, config_value) VALUES ('WhiteboardIdPrefix', NULL);
  

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
