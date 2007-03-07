-- Update the Notebook tables to 20070227
-- This is for the LAMS 2.0.1 release.

UPDATE lams_tool
set modified_date_time = now()
, classpath_addition = 'lams-tool-lantbk11.jar'
, context_file = '/org/lamsfoundation/lams/tool/notebook/notebookApplicationContext.xml'
where tool_signature = 'lantbk11';
