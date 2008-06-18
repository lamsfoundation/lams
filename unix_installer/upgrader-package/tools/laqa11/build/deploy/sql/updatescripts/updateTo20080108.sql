-- Update the Q&A tables to version 20080108
-- This is for the LAMS 2.1 release.

UPDATE tl_laqa11_content 
SET lockWhenFinished = 0 
WHERE qa_content_id = (
	SELECT default_tool_content_id 
	FROM lams_tool
	WHERE tool_signature = 'laqa11'
);

UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = 'laqa11';

-- update the tool version - special code that should only be executed if the upgrade is being done manually.
-- if it is being done via the tool deployer then it will update the version automatically.
-- UPDATE lams_tool SET tool_version = "20080108" WHERE tool_signature = "laqa11";
