-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

-- LDEV-2941 Enable the option "Open URL in pop-up" for the example URL by default
UPDATE tl_larsrc11_resource_item SET open_url_new_window=1 WHERE uid='1';


----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;