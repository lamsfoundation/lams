-- Update the Wiki tables to 20070214
-- This is for the LAMS 2.0.1 release.

ALTER TABLE tl_lawiki10_user CHANGE COLUMN username username VARCHAR(255), CHANGE COLUMN fullname fullname VARCHAR(255);

UPDATE lams_tool set modified_date_time = now(), classpath_addition = 'lams-tool-lawiki10.jar', context_file = '/org/lams/lams/tool/wiki/applicationContext.xml' where tool_signature = 'lawiki10';
