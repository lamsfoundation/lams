-- Script to be run for LAMS 2.0.3 release, on LAMS 2.0.2 tables.
-- We will be adding a new unique index on the lams_learning_transition table, so we need to check that this will work
-- ie that there aren't duplicates already. If there are duplicates, then the installer MUST STOP!

CREATE TEMPORARY TABLE tmp_check_tran 
SELECT transition_id,  concat(to_activity_id, '-', from_activity_id) AS activity_ids
FROM lams_learning_transition;

CREATE TEMPORARY TABLE tmp_check_tran_count
SELECT activity_ids, count(*) AS number_of_occurances
FROM tmp_check_tran
GROUP BY activity_ids;

SELECT * from tmp_check_tran_count
WHERE number_of_occurances > 1

