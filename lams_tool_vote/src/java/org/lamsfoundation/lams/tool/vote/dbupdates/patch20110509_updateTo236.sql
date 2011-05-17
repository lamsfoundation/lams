SET AUTOCOMMIT = 0;

-- LDEV-2657 date/time restrictions
alter table tl_lavote11_content add column submission_deadline datetime default null;

UPDATE lams_tool SET tool_version = "20110509" WHERE tool_signature = "lavote11";

COMMIT;
SET AUTOCOMMIT = 1;
