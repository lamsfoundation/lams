-- Adding a new tool type for tool adapters
ALTER TABLE lams_tool ADD COLUMN tool_adapter TINYINT(1) DEFAULT 0;