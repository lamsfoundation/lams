-- Update the Notebook tables to 20080220
-- This is for the LAMS 2.1 release.

ALTER TABLE tl_lafrum11_message
ADD COLUMN authored_parent_uid bigint;

alter table tl_lafrum11_message add index IX_msg_auth_parent (authored_parent_uid);


-- update the tool version - special code that should only be executed if the upgrade is being done manually.
-- if it is being done via the tool deployer then it will update the version automatically.
-- update lams_tool set tool_version = "20080220" where tool_signature = "lafrum11";