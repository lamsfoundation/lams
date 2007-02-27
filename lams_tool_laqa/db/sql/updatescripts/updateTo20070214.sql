-- Update the Q&A tables from version 20061113 to 20070214
-- This is for the LAMS 2.0.1 release.

ALTER TABLE tl_laqa11_que_usr 
CHANGE COLUMN username username VARCHAR(255),
CHANGE COLUMN fullname fullname VARCHAR(255);

UPDATE lams_tool
set modified_date_time = now()
, classpath_addition = './lams-tool-laqa11.jar'
, context_file = '/org/lamsfoundation/lams/tool/qa/qaApplicationContext.xml'
where tool_signature = 'laqa11';
