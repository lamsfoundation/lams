-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-4827 Add configuration settings for Question Bank
INSERT INTO lams_configuration VALUES
('QbQtiEnable', 				'true',     'config.qb.qti.enable', 			   'config.header.qb', 'BOOLEAN', 1),
('QbWordEnable', 				'true',     'config.qb.word.enable', 			   'config.header.qb', 'BOOLEAN', 1),
('QbCollectionsCreateEnable', 	'true',     'config.qb.collections.create.enable', 'config.header.qb', 'BOOLEAN', 1),
('QbMonitorsReadOnly', 			'false',    'config.qb.monitors.read.only', 	   'config.header.qb', 'BOOLEAN', 1),
('QbStatsMinParticipants',   	'2',      	'config.qb.stats.min.participants',    'config.header.qb', 'LONG', 1),
('QbStatsGroupSize',   			'27',      	'config.qb.stats.group.size',   	   'config.header.qb', 'LONG', 1);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;