-- Update the Survey tables from version 20061102 to 20070220
-- This is for the LAMS 2.0.1 release.

ALTER TABLE tl_lasurv11_answer CHANGE COLUMN answer_text answer_text text;

UPDATE lams_tool set modified_date_time = now(), classpath_addition = 'lams-tool-lasurv11.jar', context_file = '/org/lamsfoundation/lams/tool/survey/surveyApplicationContext.xml' where tool_signature = 'lasurv11';
