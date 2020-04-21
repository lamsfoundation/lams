-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-4755 Add configuratio settings for ClamAV antivirus
INSERT INTO lams_configuration VALUES
('AntivirusEnable', 'false',     'config.av.enable', 'config.header.antivirus', 'BOOLEAN', 1),
('AntivirusHost',   'localhost', 'config.av.host',   'config.header.antivirus', 'STRING',  0),
('AntivirusPort',   '3310',      'config.av.port',   'config.header.antivirus', 'LONG',    0);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;