-- Update the Eadventure tables to 20070227
-- This is for the LAMS 2.0.1 release.

UPDATE lams_tool set modified_date_time = now(), classpath_addition = 'lams-tool-eueadv10.jar', context_file = '/org/eucm/lams/tool/eadventure/eadventureApplicationContext.xml' where tool_signature = 'eueadv10';
