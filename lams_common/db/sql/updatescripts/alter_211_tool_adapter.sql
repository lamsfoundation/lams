-- Adding a new tool type for tool adapters
ALTER TABLE lams_tool ADD COLUMN ext_lms_id VARCHAR(255) DEFAULT null;