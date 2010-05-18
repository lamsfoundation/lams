SET AUTOCOMMIT = 0;

-- SIF-10 Addding a release date to the lesson table so we can 
-- track release events
alter table lams_lesson add column release_date DATETIME;
alter table lams_lesson add index idx_release_date (release_date);

COMMIT;
SET AUTOCOMMIT = 1;