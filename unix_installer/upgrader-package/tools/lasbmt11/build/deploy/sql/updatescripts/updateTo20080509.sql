-- Update the File Submit tables to 20080509
-- This is for the LAMS 2.1 release.

ALTER TABLE tl_lasbmt11_report MODIFY COLUMN marks float;
--UPDATE lams_tool SET tool_version = "20080509" WHERE tool_signature = "lasbmt11";