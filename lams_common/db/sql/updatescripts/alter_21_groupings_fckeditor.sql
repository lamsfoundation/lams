-- FOR LAMS 2.1 Release: See LDEV-1488
-- We found after upgrading demo to LAMS 2.1 that some of the existing designs had the group activities missing
-- their system tool id. This script will fix this. 

update lams_learning_activity set system_tool_id = 1 where learning_activity_type_id = '2' and system_tool_id is null;

-- See LDEV-1349
alter table lams_supported_locale
ADD COLUMN fckeditor_code VARCHAR(10);