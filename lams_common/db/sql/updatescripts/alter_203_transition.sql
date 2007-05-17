-- Script to be run for LAMS 2.0.3 release, on LAMS 2.0.2 tables.
-- Add a new unique index to the transition table as duplicates cause an error in authoring (LDEV-1229)

ALTER TABLE lams_learning_transition
ADD UNIQUE UQ_transition_activities (from_activity_id, to_activity_id);

COMMIT;
