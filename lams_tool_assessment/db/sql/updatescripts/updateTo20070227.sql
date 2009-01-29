-- Update the Assessment tables to 20070227
-- This is for the LAMS 2.0.1 release.

UPDATE lams_tool set modified_date_time = now(), classpath_addition = 'lams-tool-laasse10.jar', context_file = '/org/lamsfoundation/lams/tool/assessment/assessmentApplicationContext.xml' where tool_signature = 'laasse10';
