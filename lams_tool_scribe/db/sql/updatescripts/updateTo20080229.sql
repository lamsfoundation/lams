UPDATE lams_tool SET modified_date_time = NOW() WHERE tool_signature = "lascrb11";
UPDATE lams_tool SET tool_version = "20080229" WHERE tool_signature = "lascrb11";

ALTER TABLE tl_lascrb11_scribe ADD COLUMN aggregated_reports bit default 0;
