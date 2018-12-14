-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--     LDEV-4726 Update and add new creative common licenses 4.0 for sequences
--  Add an order_id column so we can sequence them how we like
ALTER TABLE lams_license 
ADD COLUMN order_id TINYINT NULL DEFAULT '0';

UPDATE lams_license SET order_id = license_id;
UPDATE lams_license SET order_id = 8 WHERE license_id = 6;

UPDATE lams_license 
SET name = 'CC Attribution-ShareAlike 4.0', 
url = 'https://creativecommons.org/licenses/by-sa/4.0/', 
picture_url = '/images/license/by-sa.svg'
WHERE license_id = 5;

UPDATE lams_license 
SET name = 'CC Attribution-No Derivatives 4.0', 
url = 'https://creativecommons.org/licenses/by-nd/4.0/', 
picture_url = '/images/license/by-nd.svg'
WHERE license_id = 2;

UPDATE lams_license 
SET name = 'CC Attribution-Noncommercial 4.0', 
url = 'https://creativecommons.org/licenses/by-nc/4.0/', 
picture_url = '/images/license/by-nc.eu.svg'
WHERE license_id = 4;

UPDATE lams_license 
SET name = 'LAMS Recommended: CC Attribution-Noncommercial-ShareAlike 4.0', 
url = 'https://creativecommons.org/licenses/by-nc-sa/4.0/', 
picture_url = '/images/license/by-nc-sa.eu.svg'
WHERE license_id = 1;

UPDATE lams_license 
SET name = 'CC Attribution-Noncommercial-No Derivatives 4.0', 
url = 'https://creativecommons.org/licenses/by-nc-nd/4.0/', 
picture_url = '/images/license/by-nc-nd.svg'
WHERE license_id = 3;

INSERT into lams_license 
VALUES (7, 'CC Attribution 4.0', 'by', 'https://creativecommons.org/licenses/by/4.0/', 0, '/images/license/by.svg', 6);

INSERT into lams_license 
VALUES (8, 'Public Domain', 'CC0', 'https://creativecommons.org/publicdomain/zero/1.0/', 0, '/images/license/publicdomain.svg', 7);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;