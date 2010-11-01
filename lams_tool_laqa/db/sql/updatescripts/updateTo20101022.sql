-- Update the Q&A tables to version 2010091010
-- This is for the LAMS 2.3.5 release.

ALTER TABLE tl_laqa11_que_content 
	ADD COLUMN answer_required TINYINT(1) NOT NULL DEFAULT 0;
	
-- update the tool version - special code that should only be executed if the upgrade is being done manually.
-- if it is being done via the tool deployer then it will update the version automatically.
UPDATE lams_tool SET tool_version = "20101022" WHERE tool_signature = "laqa11";
