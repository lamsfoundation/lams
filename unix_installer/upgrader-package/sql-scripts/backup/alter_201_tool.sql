-- Script to be run for LAMS 2.0.1 release, on LAMS 2.0 tables.
-- We need to record the classpath and xml changes for a tool, so that a monitoringl/learning update doesn't overwrite
-- tools that we don't know about.

ALTER TABLE lams_tool
ADD COLUMN modified_date_time DATETIME
, ADD COLUMN classpath_addition VARCHAR(255)
, ADD COLUMN context_file VARCHAR(255);

UPDATE lams_tool
SET modified_date_time  = create_date_time
WHERE modified_date_time is null;

COMMIT;
