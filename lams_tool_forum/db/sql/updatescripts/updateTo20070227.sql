-- Update the Notebook tables to 20070227
-- This is for the LAMS 2.0.1 release.

UPDATE lams_tool set modified_date_time = now(), classpath_addition = 'lams-tool-lafrum11.jar', context_file = '/org/lamsfoundation/lams/tool/forum/forumApplicationContext.xml' where tool_signature = 'lafrum11';
