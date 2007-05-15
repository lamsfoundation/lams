-- Script to be run for LAMS 2.0.3 release, on LAMS 2.0.2 tables.
-- Change waiting_flag as we want it to be a ternary value, rather than a boolean

ALTER TABLE lams_learner_progress
MODIFY COLUMN waiting_flag TINYINT;

COMMIT;
