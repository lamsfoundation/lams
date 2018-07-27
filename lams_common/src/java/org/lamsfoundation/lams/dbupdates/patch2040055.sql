-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3660 Remove export portfolio columns
ALTER TABLE lams_tool DROP COLUMN export_pfolio_learner_url,
					  DROP COLUMN export_pfolio_class_url;
					  
ALTER TABLE lams_system_tool DROP COLUMN export_pfolio_learner_url,
					  		 DROP COLUMN export_pfolio_class_url;
					  		 
ALTER TABLE lams_lesson DROP COLUMN learner_exportport_avail;

ALTER TABLE lams_organisation DROP COLUMN enable_export_portfolio;

COMMIT;
SET AUTOCOMMIT = 1;