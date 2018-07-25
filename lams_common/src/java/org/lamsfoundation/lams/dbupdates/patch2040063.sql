-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

--  LDEV-3826 Cognitive Skills Wizard in Q and A has been removed in Bootstrap so the admin screen is no longer needed.
UPDATE lams_tool 
SET admin_url = null
WHERE tool_signature = 'laqa11';

COMMIT;
SET AUTOCOMMIT = 1;