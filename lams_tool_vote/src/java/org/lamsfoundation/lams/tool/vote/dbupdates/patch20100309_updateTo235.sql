SET AUTOCOMMIT = 0;

-- LDEV-2525 Include a minimum number of votes option
alter table tl_lavote11_content add column minNominationCount VARCHAR(20) NOT NULL DEFAULT '1';

COMMIT;
SET AUTOCOMMIT = 1;