-- Update the TaskList tables to 20070227
-- This is for the LAMS 2.0.1 release.

UPDATE lams_tool set modified_date_time = now(), classpath_addition = 'lams-tool-latask10.jar', context_file = '/org/lamsfoundation/lams/tool/taskList/taskListApplicationContext.xml' where tool_signature = 'latask10';
