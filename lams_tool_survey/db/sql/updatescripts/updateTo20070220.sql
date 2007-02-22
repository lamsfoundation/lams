-- Update the Survey tables from version 20070208 to 20070220
-- This is for the LAMS 2.0.1 release.

UPDATE lams_tool
set modified_date_time = now()
, classpath_addition = './lams-tool-lasurv11.jar'
, context_file = '/org/lamsfoundation/lams/tool/survey/surveyApplicationContext.xml'
where tool_signature = 'lasurv11';
