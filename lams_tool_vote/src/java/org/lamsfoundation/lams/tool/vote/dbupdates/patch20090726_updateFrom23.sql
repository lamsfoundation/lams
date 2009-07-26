-- SQL statements to update from LAMS 2.3

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

ALTER TABLE tl_lavote11_content ADD COLUMN data_flow_object_used TINYINT(1) DEFAULT 0,
								ADD COLUMN max_inputs SMALLINT,
								ADD COLUMN assigned_data_flow_object TINYINT(1);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;