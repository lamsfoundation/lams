-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- 	LDEV-3083  update executable script extensions
UPDATE lams_configuration set config_value = '.bat,.bin,.com,.cmd,.exe,.msi,.msp,.ocx,.pif,.scr,.sct,.sh,.shs,.vbs,.php,.jsp,.asp,.aspx,.pl,.do,.py,.tcl,.cgi,.shtml,.stm,.cfm,.adp' where config_key = 'ExecutableExtensions';

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
