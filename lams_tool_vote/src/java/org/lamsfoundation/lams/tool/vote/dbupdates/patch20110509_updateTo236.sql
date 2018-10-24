SET AUTOCOMMIT = 0;

-- LDEV-2657 date/time restrictions
alter table tl_lavote11_content add column submission_deadline datetime default null;

ALTER TABLE tl_lavote11_content ADD COLUMN external_inputs_added SMALLINT DEFAULT 0,	
								ADD COLUMN max_external_inputs SMALLINT,
								ADD COLUMN assigned_data_flow_object TINYINT(1);
								
UPDATE lams_tool SET tool_version = "20110509" WHERE tool_signature = "lavote11";

COMMIT;
SET AUTOCOMMIT = 1;
