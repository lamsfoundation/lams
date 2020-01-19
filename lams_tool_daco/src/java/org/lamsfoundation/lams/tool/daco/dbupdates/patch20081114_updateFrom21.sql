-- SQL statements to update from LAMS 2.1/2.1.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here


-- SQL statements to update from LAMS 2.1/2.1.1

UPDATE lams_tool SET supports_outputs=1 WHERE tool_signature='ladaco10';

-- Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
